package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.bucket.BucketName;
import eeet2582.realestatemgt.filestore.FileStore;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.repository.HouseRepository;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

// HouseService only wires HouseRepository, everything else is handled by child services
@Service
public class HouseService {

    private static final ContentType IMAGE_PNG = ContentType.IMAGE_PNG;
    private static final ContentType IMAGE_JPEG = ContentType.IMAGE_JPEG;

    @Autowired
    private final FileStore fileStore;

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final RentalService rentalService;

    public HouseService(FileStore fileStore, HouseRepository houseRepository,
                        AdminService adminService,
                        RentalService rentalService) {
        this.fileStore = fileStore;
        this.houseRepository = houseRepository;
        this.adminService = adminService;
        this.rentalService = rentalService;
    }

    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }

    public House getHouseById(Long houseId) {
        return houseRepository
                .findById(houseId)
                .orElseThrow(() -> new IllegalStateException("House with houseId=" + houseId  + " does not exist!"));
    }

    public void deleteHouseById(Long houseId) {
        if (!houseRepository.existsById(houseId))
            throw new IllegalStateException("House with houseId=" + houseId + " does not exist!");

        // Delete all classes that depend on current house
        adminService.deleteDepositsByHouseId(houseId);
        adminService.deleteMeetingsByHouseId(houseId);
        rentalService.deleteRentalsByHouseId(houseId);

        House houseObj = houseRepository.findById(houseId).orElseThrow(() -> new IllegalStateException("House with houseId=" + houseId  + " does not exist!"));
        String fileName = houseObj.getImage().substring(houseObj.getImage().lastIndexOf("/")+1);
        fileStore.delete(fileName);

        houseRepository.deleteById(houseId);
    }

    public void addNewHouse(@NotNull House house, @NotNull MultipartFile file) {
        //check if the file is empty
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file");
        }
        //Check if the file is an image
        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_JPEG.getMimeType())
                .contains(file.getContentType())) {
            throw new IllegalStateException("FIle uploaded is not an image");
        }
        //get file metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        //Save Image in S3 and then save House in the database
        String path = String.format("%s/%s", BucketName.HOUSE_IMAGE.getBucketName(), "socal_pics");
        String fileName = String.format("%s", file.getOriginalFilename());
        try {
            fileStore.upload(path, fileName, Optional.of(metadata), file.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload file", e);
        }
        House houseObj = House.builder()
                .name(house.getName())
                .price(house.getPrice())
                .description(house.getDescription())
                .address(house.getAddress())
                .longitude(house.getLongitude())
                .latitude(house.getLatitude())
                .image("https://real-estate-mgt-app.s3.ap-southeast-1.amazonaws.com/socal_pics/"+fileName)
                .type(house.getType())
                .numberOfBeds(house.getNumberOfBeds())
                .squareFeet(house.getSquareFeet())
                .status(house.getStatus()).build();
        houseRepository.save(houseObj);
    }}
