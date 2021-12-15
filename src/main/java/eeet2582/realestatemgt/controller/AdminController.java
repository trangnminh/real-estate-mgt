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

    @GetMapping("/deposit/{depositId}")
    public Deposit getDepositById(@PathVariable("depositId") Long depositId) {
        return adminService.getDepositById(depositId);
    }

    @PutMapping("/deposit/{depositId}")
    public void updateDepositById(@RequestBody Deposit deposit) {
        adminService.updateDepositById(deposit);
    }

    @GetMapping("/deposits/byUser/{userId}")
    public List<Deposit> getDepositsByUserId(@PathVariable("userId") Long userId) {
        return adminService.getDepositsByUserId(userId);
    }

    @GetMapping("/deposits/byHouse/{houseId}")
    public List<Deposit> getDepositsByHouseId(@PathVariable("houseId") Long houseId) {
        return adminService.getDepositsByHouseId(houseId);
    }

    @DeleteMapping("/deposits/byUser/{userId}")
    public void deleteDepositsByUserId(@PathVariable("userId") Long userId) {
        adminService.deleteDepositsByUserId(userId);
    }

    @DeleteMapping("/deposits/byHouse/{houseId}")
    public void deleteDepositsByHouseId(@PathVariable("houseId") Long houseId) {
        adminService.deleteDepositsByHouseId(houseId);
    }

    // Meeting
    @GetMapping("/meetings")
    public List<Meeting> getAllMeetings() {
        return adminService.getAllMeetings();
    }

    @DeleteMapping("/meeting/{meetingId}")
    public void deleteMeetingById(@PathVariable("meetingId") Long meetingId) { adminService.deleteMeetingById(meetingId); }

    @GetMapping("/meeting/{meetingId}")
    public Meeting getMeetingById(@PathVariable("meetingId") Long meetingId) { return adminService.getMeetingById(meetingId); }

    @PutMapping("/meeting/{meetingId}")
    public void updateMeetingById(@RequestBody Meeting meeting) {
        adminService.updateMeetingById(meeting);
    }

    @GetMapping("/meetings/byUser/{userId}")
    public List<Meeting> getMeetingsByUserId(@PathVariable("userId") Long userId) {
        return adminService.getMeetingsByUserId(userId);
    }

    @GetMapping("/meetings/byHouse/{houseId}")
    public List<Meeting> getMeetingsByHouseId(@PathVariable("houseId") Long houseId) {
        return adminService.getMeetingsByHouseId(houseId);
    }

    @DeleteMapping("/meetings/byUser/{userId}")
    public void deleteMeetingsByUserId(@PathVariable("userId") Long userId) {
        adminService.deleteMeetingsByUserId(userId);
    }

    @DeleteMapping("/meetings/byHouse/{houseId}")
    public void deleteMeetingsByHouseId(@PathVariable("houseId") Long houseId) {
        adminService.deleteMeetingsByHouseId(houseId);
    }
}
