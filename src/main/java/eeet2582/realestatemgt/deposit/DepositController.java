package eeet2582.realestatemgt.deposit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/deposits")
public class DepositController {

    private final DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping
    public List<Deposit> getAll() {
        return depositService.getAll();
    }

    @GetMapping("/{depositId}")
    public Deposit getById(@PathVariable("depositId") Long depositId) {
        return depositService.getById(depositId);
    }

    @DeleteMapping("/{depositId}")
    public void deleteById(@PathVariable("depositId") Long depositId) { depositService.deleteById(depositId); }

    @GetMapping("/byUser/{userId}")
    public List<Deposit> getByUserId(@PathVariable("userId") Long userId) {
        return depositService.getByUserId(userId);
    }

    @GetMapping("/byHouse/{houseId}")
    public List<Deposit> getByHouseId(@PathVariable("houseId") Long houseId) {
        return depositService.getByHouseId(houseId);
    }
}
