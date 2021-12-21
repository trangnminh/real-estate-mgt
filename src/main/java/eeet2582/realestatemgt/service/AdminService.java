package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.helper.UserHouse;
import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.repository.DepositRepository;
import eeet2582.realestatemgt.repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Handle Deposit and Meeting operations
@Service
public class AdminService {

    @Autowired
    private final DepositRepository depositRepository;

    @Autowired
    private final MeetingRepository meetingRepository;

    public AdminService(DepositRepository depositRepository, MeetingRepository meetingRepository) {
        this.depositRepository = depositRepository;
        this.meetingRepository = meetingRepository;
    }

    // --- DEPOSIT --- //

    public List<Deposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    // Return paginated payments of the provided user or house or just all payments
    public Page<Deposit> getFilteredDepositsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, String sortBy, String orderBy) {
        Pageable pageable;

        if (orderBy.equals("asc")) {
            if (sortBy.equals("dateTime")) {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending().and(Sort.by("time").ascending()));
            } else {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
            }
        } else {
            if (sortBy.equals("dateTime")) {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").descending().and(Sort.by("time").descending()));
            } else {
                pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            }
        }

        if (userId != null) {
            return depositRepository.findByUserHouse_UserId(userId, pageable);
        } else if (houseId != null) {
            return depositRepository.findByUserHouse_HouseId(houseId, pageable);
        } else {
            return depositRepository.findAll(pageable);
        }
    }

    // Get one by ID, try to reuse the exception
    public Deposit getDepositById(Long depositId) {
        if (!depositRepository.existsById(depositId))
            throw new IllegalStateException("Deposit with depositId=" + depositId + " does not exist!");

        return depositRepository.getById(depositId);
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    public void saveDepositById(Long depositId, Long userId, Long houseId, Double amount, String date, String time, String note) {
        // If ID is provided, try to find the current item, else make new one
        Deposit deposit = (depositId != null) ? getDepositById(depositId) : new Deposit();

        // Do input checking here

        // Save the cleaned item
        deposit.setUserHouse(new UserHouse(userId, houseId));
        deposit.setAmount(amount);
        deposit.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        deposit.setTime(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
        deposit.setNote(note);

        depositRepository.save(deposit);
    }

    public void deleteDepositById(Long depositId) {
        Deposit deposit = getDepositById(depositId);
        depositRepository.delete(deposit);
    }

    @Transactional
    public void deleteDepositsByUserId(Long userId) {
        depositRepository.deleteByUserHouse_UserId(userId);
    }

    @Transactional
    public void deleteDepositsByHouseId(Long houseId) {
        depositRepository.deleteByUserHouse_HouseId(houseId);
    }

    // --- MEETING--- //

    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    // Return paginated meetings of the provided user or house or just all meetings
    public Page<Meeting> getFilteredMeetingsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, String orderBy) {
        Pageable pageable;

        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending().and(Sort.by("time").ascending()));
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").descending().and(Sort.by("time").descending()));
        }

        if (userId != null) {
            return meetingRepository.findByUserHouse_UserId(userId, pageable);
        } else if (houseId != null) {
            return meetingRepository.findByUserHouse_HouseId(houseId, pageable);
        } else {
            return meetingRepository.findAll(pageable);
        }
    }

    // Get one by ID, try to reuse the exception
    public Meeting getMeetingById(Long meetingId) {
        if (!meetingRepository.existsById(meetingId))
            throw new IllegalStateException("Meeting with meetingId=" + meetingId + " does not exist!");

        return meetingRepository.getById(meetingId);
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    public void saveMeetingById(Long meetingId, Long userId, Long houseId, String date, String time, String note) {
        // If ID is provided, try to find the current item, else make new one
        Meeting meeting = (meetingId != null) ? getMeetingById(meetingId) : new Meeting();

        // Do input checking here

        // Save the cleaned item
        meeting.setUserHouse(new UserHouse(userId, houseId));
        meeting.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        meeting.setTime(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
        meeting.setNote(note);

        meetingRepository.save(meeting);
    }

    public void deleteMeetingById(Long meetingId) {
        Meeting meeting = getMeetingById(meetingId);
        meetingRepository.delete(meeting);
    }

    @Transactional
    public void deleteMeetingsByUserId(Long userId) {
        meetingRepository.deleteByUserHouse_UserId(userId);
    }

    @Transactional
    public void deleteMeetingsByHouseId(Long houseId) {
        meetingRepository.deleteByUserHouse_HouseId(houseId);
    }
}
