package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handle "admin" classes Deposit and Meeting (that depend on User and House)
// Implemented: (both) get all, get one, get all by userId/houseId, add update by userHouse, delete
/*
AUTHORIZED USER CAN:
- getFilteredDepositsByUserId : TODO: front-end need to check if current user id is the same id in the request params or not
- getDepositById : users can get deposit by id
- saveDeposit : users can make a deposit
- getFilteredMeetingsByUserId : TODO: front-end need to check if current user id is the same id in the request params or not
- getMeetingById : users can get meeting by id
- saveMeeting : users can add/update a meeting
*/

/*
ADMIN CAN:
- getAllDeposits : admin can get all deposits
- getFilteredDeposits : admin can get all deposits by pagination and filters without any user id or house id
- getFilteredDepositsByHouseId : admin can get deposits by house id
- deleteDepositById : admin can delete deposit by id
- getAllMeetings : admin can get all meetings
- getFilteredMeetings : admin can get all meetings by pagination and filters without any user id or house id
- getFilteredMeetingsByHouseId : admin can get all meetings by house id
- deleteMeetingById : admin can delete meeting by id
- updateDepositById : admin can update deposit by id
 */

@RestController
@RequestMapping("api/v1")
public class AdminController {

    private static final String TOPIC = "meeting";
    private final AdminService adminService;
    @Autowired
    private KafkaTemplate<String, Meeting> kafkaTemplate;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // --- DEPOSIT --- //

    // Just get all (for debug)
    @GetMapping("/deposits")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public List<Deposit> getAllDeposits() {
        return adminService.getAllDeposits();
    }

    // Return deposits with sort, order and pagination (no query)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/deposits/search")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public Page<Deposit> getFilteredDeposits(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                             @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
                                             @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return adminService.getFilteredDepositsAllOrByUserIdOrByHouseId(null, null, pageNo, pageSize, sortBy, orderBy);
    }

    // Return deposits with sort, order and pagination (by userId)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/deposits/search/byUser/{userId}")
    public Page<Deposit> getFilteredDepositsByUserId(@PathVariable("userId") Long userId,
                                                     @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                     @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
                                                     @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return adminService.getFilteredDepositsAllOrByUserIdOrByHouseId(userId, null, pageNo, pageSize, sortBy, orderBy);
    }

    // Return deposits with sort, order and pagination (by houseId)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/deposits/search/byHouse/{houseId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public Page<Deposit> getFilteredDepositsByHouseId(@PathVariable("houseId") Long houseId,
                                                      @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                      @RequestParam(value = "sortBy", defaultValue = "dateTime") String sortBy,
                                                      @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return adminService.getFilteredDepositsAllOrByUserIdOrByHouseId(null, houseId, pageNo, pageSize, sortBy, orderBy);
    }

    // Get one by ID
    @GetMapping("/deposits/{depositId}")
    public Deposit getDepositById(@PathVariable("depositId") Long depositId) {
        return adminService.getDepositById(depositId);
    }

    // Add new deposit
    @PostMapping("/deposits")
    public void addNewDeposit(@RequestBody Deposit deposit) {
        adminService.addNewDeposit(deposit);
    }

    // Update deposit by id
    @PutMapping("/deposits/{depositId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void updateUserById(@PathVariable(value = "depositId") Long depositId, @RequestBody Deposit deposit) {
        adminService.updateDepositById(depositId, deposit);
    }

    // Delete one by ID
    @DeleteMapping("/deposits/{depositId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void deleteDepositById(@PathVariable("depositId") Long depositId) {
        adminService.deleteDepositById(depositId);
    }

    // --- MEETING --- //

    // Just get all (for debug)
    @GetMapping("/meetings")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public List<Meeting> getAllMeetings() {
        return adminService.getAllMeetings();
    }

    // Return meetings with sort, order and pagination (no query, no sortBy)
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/meetings/search")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public Page<Meeting> getFilteredMeetings(@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                             @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return adminService.getFilteredMeetingsAllOrByUserIdOrByHouseId(null, null, pageNo, pageSize, orderBy);
    }

    // Return meetings with sort, order and pagination (no query, no sortBy) by userId
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/meetings/search/byUser/{userId}")
    public Page<Meeting> getFilteredMeetingsByUserId(@PathVariable("userId") Long userId,
                                                     @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                     @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                     @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return adminService.getFilteredMeetingsAllOrByUserIdOrByHouseId(userId, null, pageNo, pageSize, orderBy);
    }

    // Return meetings with sort, order and pagination (no query, no sortBy) by houseId
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/meetings/search/byHouse/{houseId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public Page<Meeting> getFilteredMeetingsByHouseId(@PathVariable("houseId") Long houseId,
                                                      @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                      @RequestParam(value = "orderBy", defaultValue = "desc") String orderBy) {
        return adminService.getFilteredMeetingsAllOrByUserIdOrByHouseId(null, houseId, pageNo, pageSize, orderBy);
    }

    // Get one by ID
    @GetMapping("/meetings/{meetingId}")
    public Meeting getMeetingById(@PathVariable("meetingId") Long meetingId) {
        return adminService.getMeetingById(meetingId);
    }

    // Update one by ID or add new one (send Kafka topic)
    @PostMapping("/meetings")
    public void saveMeetingById(@RequestParam(value = "meetingId", required = false) Long meetingId,
                                @RequestParam Long userId,
                                @RequestParam Long houseId,
                                @RequestParam String date,
                                @RequestParam String time,
                                @RequestParam String note) {
        kafkaTemplate.send(TOPIC, adminService.createMeetingTopic(meetingId, userId, houseId, date, time, note));
    }

    // Delete one by ID
    @DeleteMapping("/meetings/{meetingId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void deleteMeetingById(@PathVariable("meetingId") Long meetingId) {
        adminService.deleteMeetingById(meetingId);
    }
}
