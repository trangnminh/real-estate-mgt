package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class HouseController {

    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService){
        this.houseService = houseService;
    }

    @GetMapping("/houses")
    public List<House> getAllHouses() {
        return houseService.getAllHouses();
    }

    @GetMapping("house/{houseId}")
    public House getHouseById(@PathVariable("houseId") Long houseId) {
        return houseService.getHouseById(houseId);
    }

    @DeleteMapping("house/{houseId}")
    public void deleteHouseById(@PathVariable("houseId") Long houseId) {
        houseService.deleteHouseById(houseId);
    }
}
