package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*")
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

    @PostMapping(
            path = "/house",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void addNewHouse(@ModelAttribute House house, @RequestParam("file") MultipartFile file) {
        houseService.addNewHouse(house,file);
    }
}
