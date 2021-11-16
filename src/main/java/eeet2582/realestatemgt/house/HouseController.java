package eeet2582.realestatemgt.house;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/houses")
public class HouseController {

    private static final List<House> HOUSES = Arrays.asList(
            new House(1L, "Lovely master bedroom at New Saigon", "Single master bedroom with private toilet", "Nguyen Huu Tho, Phuoc Kien, Nha Be, HCMC"),
            new House(2L, "Shared bedroom with two beautiful girls", "Shared bedroom in two-story house", "Nguyen Huu Tho, Phuoc Kien, Nha Be, HCMC")
            );

    @GetMapping(path = "{houseId}")
    public House getHouse(@PathVariable("houseId") Long houseId) {
        return HOUSES.stream()
                .filter(house -> houseId.equals(house.getHouseId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("House " + houseId + " does not exist!"));
    }
}
