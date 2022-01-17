package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.bucket.BucketName;
import eeet2582.realestatemgt.filestore.FileStore;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.form.HouseForm;
import eeet2582.realestatemgt.model.helper.HouseLocation;
import eeet2582.realestatemgt.model.helper.HouseSearchForm;
import eeet2582.realestatemgt.repository.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static eeet2582.realestatemgt.config.RedisConfig.*;

// HouseService only wires HouseRepository, everything else is handled by child services
@Service
@RequiredArgsConstructor
public class HouseService {

    public static final int HOUSE_BATCH_SIZE = 200;
    private static final ContentType IMAGE_PNG = ContentType.IMAGE_PNG;
    private static final ContentType IMAGE_JPEG = ContentType.IMAGE_JPEG;

    private static final List<String> STATUS_LIST = new ArrayList<>() {
        {
            add("available");
            add("reserved");
            add("rented");
        }
    };

    private static final List<String> TYPE_LIST = new ArrayList<>() {
        {
            add("apartment");
            add("serviced");
            add("street");
        }
    };

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final FileStore fileStore;

    @Autowired
    private final UserHouseLocationUtil userHouseLocationUtil;

    // Find houses by search form
    @Cacheable(value = HOUSE_SEARCH)
    public List<House> getHousesBySearchForm(HouseSearchForm form) {
        // Find by location, if null default to Saigon 7
        HouseLocation location = userHouseLocationUtil.getHouseLocation(form.getCity(), form.getDistrict());
        List<House> matchLocation = houseRepository.findByLocation_CityAndLocation_District(location.getCity(), location.getDistrict());

        // Find by price range, if null default to 200 - 1000
        double priceFrom = (form.getPriceFrom() != null && form.getPriceFrom() > 0) ? form.getPriceFrom() : 200;
        double priceTo = (form.getPriceTo() != null && form.getPriceTo() > 0) ? form.getPriceTo() : 1000;
        List<House> matchPrice = houseRepository.findByPriceBetween(priceFrom, priceTo);

        // Find by status, if null takes all types
        List<String> statusList = (form.getStatusList() != null && !form.getStatusList().isEmpty()) ?
                form.getStatusList() : STATUS_LIST;
        List<House> matchStatus = houseRepository.findByStatusIn(statusList);

        // Find by type, if null takes all types
        List<String> typeList = (form.getTypeList() != null && !form.getTypeList().isEmpty()) ?
                form.getTypeList() : TYPE_LIST;
        List<House> matchType = houseRepository.findByTypeIn(typeList);

        // Find by query (match name or address)
        String query = (form.getQuery() != null) ? form.getQuery() : "";
        House house = new House();
        house.setName(query);
        house.setAddress(query);
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("address", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<House> example = Example.of(house, matcher);
        List<House> matchQuery = houseRepository.findAll(example);

        // Find intersection between all lists
        matchQuery.retainAll(matchLocation);
        matchQuery.retainAll(matchPrice);
        matchQuery.retainAll(matchStatus);
        matchQuery.retainAll(matchType);
        return matchQuery;
    }

    // Get one by ID, try to reuse the exception
    public House getHouseById(Long houseId) {
        return userHouseLocationUtil.getHouseById(houseId);
    }

    // Add new one
    @CacheEvict(value = HOUSE_SEARCH, allEntries = true)
    public House addNewHouse(@NotNull HouseForm form, @NotNull MultipartFile[] files) {
        // Get house images
        List<String> imageList = addImagesToBucket(files, null);
        HouseLocation location = userHouseLocationUtil.getHouseLocation(form.getCity(), form.getDistrict());

        House house = new House(
                form.getName(),
                form.getPrice(),
                form.getDescription(),
                form.getAddress(),
                form.getLongitude(),
                form.getLatitude(),
                imageList,
                form.getType(),
                form.getNumberOfBeds(),
                form.getSquareFeet(),
                form.getStatus(),
                location
        );

        return houseRepository.save(house);
    }

    // Add images to cloud database
    public List<String> addImagesToBucket(@NotNull MultipartFile[] files, String imageFolder) {
        // Check if the file is empty
        Arrays.stream(files).forEach(file -> {
            if (file.isEmpty()) {
                throw new IllegalStateException("Cannot upload empty file!");
            }
        });

        // Check if the file is an image
        Arrays.stream(files).forEach(file -> {
            if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                    IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
                throw new IllegalStateException("File uploaded is not an image!");
            }
        });

        // Get file metadata
        // Save Image in S3 and then save House in the database
        if (imageFolder == null) {
            imageFolder = UUID.randomUUID().toString();
        }
        String path = String.format("%s/%s", BucketName.HOUSE_IMAGE.getBucketName(), "dataset/" + imageFolder);
        final String imageFolderCopy = imageFolder;

        List<String> imageList = new ArrayList<>();
        List<Optional<Map<String, String>>> metadataList = new ArrayList<>();

        Arrays.stream(files).forEach(file -> {
            String fileName = String.format("%s", file.getOriginalFilename());
            try {
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-Type", file.getContentType());
                metadata.put("Content-Length", String.valueOf(file.getSize()));
                metadataList.add(Optional.of(metadata));
                fileStore.upload(path, fileName, metadataList, file.getInputStream());
                imageList.add("https://realestatemgt.s3.ap-southeast-1.amazonaws.com/dataset/" + imageFolderCopy + "/" + fileName);
            } catch (IOException e) {
                throw new IllegalStateException("Error " + e.getMessage());
            }
        });
        return imageList;
    }

    // Update house by multiple attributes
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = HOUSE_SEARCH, allEntries = true),
            @CacheEvict(value = HOUSE, key = HOUSE_ID)
    })
    public House updateHouseById(Long houseId, @NotNull HouseForm form) {
        House house = getHouseById(houseId);
        HouseLocation location = userHouseLocationUtil.getHouseLocation(form.getCity(), form.getDistrict());
        house.setLocation(location);

        if (form.getName() != null && !form.getName().isBlank() && !house.getName().equals(form.getName())) {
            house.setName(form.getName());
        }

        if (form.getPrice() != null && !Objects.equals(house.getPrice(), form.getPrice())) {
            house.setPrice(form.getPrice());
        }

        if (form.getDescription() != null && !form.getDescription().isBlank() && !house.getDescription().equals(form.getDescription())) {
            house.setDescription(form.getDescription());
        }

        if (form.getAddress() != null && !form.getAddress().isBlank() && !house.getAddress().equals(form.getAddress())) {
            house.setAddress(form.getAddress());
        }

        if (form.getLongitude() != null && !Objects.equals(house.getLongitude(), form.getLongitude())) {
            house.setLongitude(form.getLongitude());
        }

        if (form.getLatitude() != null && !Objects.equals(house.getLatitude(), form.getLatitude())) {
            house.setLatitude(form.getLatitude());
        }

        if (form.getType() != null && !form.getType().isBlank() && !house.getType().equals(form.getType())) {
            house.setType(form.getType());
        }

        if (form.getNumberOfBeds() != null && !Objects.equals(house.getNumberOfBeds(), form.getNumberOfBeds())) {
            house.setNumberOfBeds(form.getNumberOfBeds());
        }

        if (form.getSquareFeet() != null && !Objects.equals(house.getSquareFeet(), form.getSquareFeet())) {
            house.setSquareFeet(form.getSquareFeet());
        }

        if (form.getStatus() != null && !form.getStatus().isBlank() && !house.getStatus().equals(form.getStatus())) {
            house.setStatus(form.getStatus());
        }

        // Delete one or multiple images in a folder
        if (form.getImage().size() != 0) {
            List<String> imagePath = new ArrayList<>();

            List<String> newImageURL = form.getImage();
            List<String> oldImageURL = house.getImage();

            if (oldImageURL.size() == 0) {
                throw new IllegalStateException("Image list is empty!");
            }

            oldImageURL.removeAll(newImageURL);
            for (String path : oldImageURL) {
                imagePath.add(path.substring(path.indexOf("com/") + 4));
            }

            fileStore.deletePicturesInFolder(imagePath);
            house.setImage(newImageURL);
        }
        return house;
    }

    // Delete house and bucket images
    @Caching(evict = {
            @CacheEvict(value = HOUSE_SEARCH, allEntries = true),
            @CacheEvict(value = HOUSE, key = HOUSE_ID)
    })
    public String deleteHouseById(Long houseId) {
        House houseObj = getHouseById(houseId);

        String path = houseObj.getImage()
                .get(0).substring(houseObj.getImage()
                        .get(0).indexOf("t/") + 2, houseObj.getImage().get(0).lastIndexOf("/"));
        String key = fileStore.delete(path);
        houseRepository.deleteById(houseId);
        return key;
    }

    // Add more images to already saved house
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = HOUSE_SEARCH, allEntries = true),
            @CacheEvict(value = HOUSE, key = HOUSE_ID)
    })
    public void addMoreImagesToHouse(Long houseId, MultipartFile @NotNull [] files) {
        House oldHouse = getHouseById(houseId);

        // Upload more images in a folder
        if (files.length != 0) {
            String imageFolder;
            if (oldHouse.getImage().size() == 0) {
                imageFolder = UUID.randomUUID().toString();
            } else {
                imageFolder = oldHouse.getImage().get(0).substring(oldHouse.getImage().get(0).indexOf("t/") + 2, oldHouse.getImage().get(0).lastIndexOf("/"));
            }
            List<String> imageList = oldHouse.getImage();
            imageList.addAll(addImagesToBucket(files, imageFolder));
            oldHouse.setImage(imageList);
        }
    }
}
