package eeet2582.realestatemgt.house;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/houses")
public class HouseController {

    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService){
        this.houseService = houseService;
    }

    @GetMapping
    public List<House> getAll() {
        return houseService.getAll();
    }

    @GetMapping("/{houseId}")
    public House getById(@PathVariable("houseId") Long houseId) {
        return houseService.getById(houseId);
    }

    @DeleteMapping("/{houseId}")
    public void deleteById(@PathVariable("houseId") Long houseId) {
        houseService.deleteById(houseId);
    }
}
