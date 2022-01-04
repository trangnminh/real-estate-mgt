package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.bucket.BucketName;
import eeet2582.realestatemgt.filestore.FileStore;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.repository.HouseRepository;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

// HouseService only wires HouseRepository, everything else is handled by child services
@Service
public class HouseService {

    public static final int HOUSE_BATCH_SIZE = 200;
    private static final ContentType IMAGE_PNG = ContentType.IMAGE_PNG;
    private static final ContentType IMAGE_JPEG = ContentType.IMAGE_JPEG;

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final RentalService rentalService;

    @Autowired
    private final FileStore fileStore;

    public HouseService(HouseRepository houseRepository, AdminService adminService, RentalService rentalService, FileStore fileStore) {
        this.houseRepository = houseRepository;
        this.adminService = adminService;
        this.rentalService = rentalService;
        this.fileStore = fileStore;
    }

    // Find houses matching by name, description or address
    @Cacheable(value = "FilteredHouses")
    public List<House> getFilteredHousesCache(String query, String sortBy, String orderBy, int batchNo) {
        House house = new House();
        house.setName(query);
        house.setDescription(query);
        house.setAddress(query);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("address", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<House> example = Example.of(house, matcher);

        Pageable limit = (orderBy.equals("asc")) ? PageRequest.of(batchNo, HOUSE_BATCH_SIZE, Sort.by(sortBy).ascending()) :
                PageRequest.of(batchNo, HOUSE_BATCH_SIZE, Sort.by(sortBy).descending());

        return houseRepository.findAll(example, limit).getContent();
    }

    // Find houses within a price range
    @Cacheable(value = "FilteredHousesByPriceBetween")
    public List<House> getFilteredHousesByPriceBetweenCache(Double low, Double high, String sortBy, String orderBy, int batchNo) {
        Pageable limit = (orderBy.equals("asc")) ? PageRequest.of(batchNo, HOUSE_BATCH_SIZE, Sort.by(sortBy).ascending()) :
                PageRequest.of(batchNo, HOUSE_BATCH_SIZE, Sort.by(sortBy).descending());

        return houseRepository.findByPriceBetween(low, high, limit).getContent();
    }

    // Get one by ID, try to reuse the exception
    public House getHouseById(Long houseId) {
        return houseRepository
                .findById(houseId)
                .orElseThrow(() -> new IllegalStateException("House with houseId=" + houseId + " does not exist!"));
    }

    // Add new one
    public String addNewHouse(@NotNull House house, @NotNull MultipartFile[] files) {
        List<String> imageList = addImages(files, null);
        House houseObj = House.builder()
                .name(house.getName())
                .price(house.getPrice())
                .description(house.getDescription())
                .address(house.getAddress())
                .longitude(house.getLongitude())
                .latitude(house.getLatitude())
                .image(imageList)
                .type(house.getType())
                .numberOfBeds(house.getNumberOfBeds())
                .squareFeet(house.getSquareFeet())
                .status(house.getStatus()).build();
        houseRepository.save(houseObj);
        return "House added successfully!";
    }

    // Update house by multiple attributes
    @Transactional
    public String updateHouseById(Long houseId, @NotNull House house) {
        House oldHouse = getHouseById(houseId);

        if (house.getName() != null && !Objects.equals(oldHouse.getName(), house.getName())) {
            oldHouse.setName(house.getName());
        }

        if (house.getPrice() != null && !Objects.equals(oldHouse.getPrice(), house.getPrice())) {
            oldHouse.setPrice(house.getPrice());
        }

        if (house.getDescription() != null && house.getDescription().length() > 0 && !Objects.equals(oldHouse.getDescription(), house.getDescription())) {
            oldHouse.setDescription(house.getDescription());
        }

        if (house.getAddress() != null && house.getAddress().length() > 0 && !Objects.equals(oldHouse.getAddress(), house.getAddress())) {
            oldHouse.setAddress(house.getAddress());
        }

        if (house.getLongitude() != null) {
            oldHouse.setLongitude(house.getLongitude());
        }

        if (house.getLatitude() != null) {
            oldHouse.setLatitude(house.getLatitude());
        }

        // Delete one or multiple images in a folder
        if (house.getImage().size() != 0) {
            List<String> imagePath = new ArrayList<>();
            List<String> newImageURL = house.getImage();
            List<String> oldImageURL = oldHouse.getImage();

            if (oldImageURL.size() == 0) {
                return "Image list is empty!";
            }

            for (String path : house.getImage()) {
                imagePath.add(path.substring(path.indexOf("com/") + 4));
            }
            fileStore.deletePicturesInFolder(imagePath);
            oldImageURL.removeAll(newImageURL);
            oldHouse.setImage(oldImageURL);
        }

        if (house.getType() != null && house.getType().length() > 0 && !Objects.equals(oldHouse.getType(), house.getType())) {
            oldHouse.setType(house.getType());
        }

        if (house.getNumberOfBeds() != null) {
            oldHouse.setNumberOfBeds(house.getNumberOfBeds());
        }

        if (house.getSquareFeet() != null) {
            oldHouse.setSquareFeet(house.getSquareFeet());
        }

        if (house.getStatus() != null && !Objects.equals(oldHouse.getStatus(), house.getStatus())) {
            oldHouse.setStatus(house.getStatus());
        }
        return "House updated successfully!";
    }

    public List<String> addImages(@NotNull MultipartFile[] files, String imageFolder) {
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
                e.printStackTrace();
            }
        });
        return imageList;
    }

    @Transactional
    public String addHouseImage(Long houseId, MultipartFile @NotNull [] files) {
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
            imageList.addAll(addImages(files, imageFolder));
            oldHouse.setImage(imageList);
            return "House images updated!";
        }
        return "Please insert image files!";
    }

    public String deleteHouseById(Long houseId) {
        House houseObj = getHouseById(houseId);

        // Delete all classes that depend on current house
        adminService.deleteDepositsByHouseId(houseId);
        adminService.deleteMeetingsByHouseId(houseId);
        rentalService.deleteRentalsByHouseId(houseId);

        String path = houseObj.getImage().get(0).substring(houseObj.getImage().get(0).indexOf("t/") + 2, houseObj.getImage().get(0).lastIndexOf("/"));
        String key = fileStore.delete(path);
        houseRepository.deleteById(houseId);
        return key;
    }
}
