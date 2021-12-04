package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handle "admin" classes Deposit, Meeting, Rental (that depend on User and House)
@RestController
@RequestMapping("api/v1")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Deposit
    @GetMapping("/deposits")
    public List<Deposit> getAllDeposits() {
        return adminService.getAllDeposits();
    }

    @DeleteMapping("/deposit/{depositId}")
    public void deleteDepositById(@PathVariable("depositId") Long depositId) { adminService.deleteDepositById(depositId); }

    @GetMapping("/deposits/byUser/{userId}")
    public List<Deposit> getDepositsByUserId(@PathVariable("userId") Long userId) {
        return adminService.getDepositsByUserId(userId);
    }

    @GetMapping("/deposits/byHouse/{houseId}")
    public List<Deposit> getDepositsByHouseId(@PathVariable("houseId") Long houseId) {
        return adminService.getDepositsByHouseId(houseId);
    }

    // Meeting
    @GetMapping("/meetings")
    public List<Meeting> getAllMeetings() {
        return adminService.getAllMeetings();
    }

    @DeleteMapping("/meeting/{meetingId}")
    public void deleteMeetingById(@PathVariable("meetingId") Long meetingId) { adminService.deleteMeetingById(meetingId); }

    @GetMapping("/meetings/byUser/{userId}")
    public List<Meeting> getMeetingsByUserId(@PathVariable("userId") Long userId) {
        return adminService.getMeetingsByUserId(userId);
    }

    @GetMapping("/meetings/byHouse/{houseId}")
    public List<Meeting> getMeetingsByHouseId(@PathVariable("houseId") Long houseId) {
        return adminService.getMeetingsByHouseId(houseId);
    }
}
