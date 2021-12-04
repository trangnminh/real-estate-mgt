package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.repository.HouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// HouseService only wires HouseRepository, everything else is handled by child services
@Service
public class HouseService {

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final RentalService rentalService;

    public HouseService(HouseRepository houseRepository,
                        AdminService adminService,
                        RentalService rentalService) {
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

        houseRepository.deleteById(houseId);
    }
}
