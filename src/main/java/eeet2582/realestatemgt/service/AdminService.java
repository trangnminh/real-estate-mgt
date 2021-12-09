package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Handle Deposit and Meeting operations
@Service
public class AdminService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final DepositRepository depositRepository;

    @Autowired
    private final MeetingRepository meetingRepository;

    public AdminService(UserRepository userRepository,
                        HouseRepository houseRepository,
                        DepositRepository depositRepository,
                        MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.houseRepository = houseRepository;
        this.depositRepository = depositRepository;
        this.meetingRepository = meetingRepository;
    }

    // Deposit
    public List<Deposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    public void deleteDepositById(Long depositId) {
        if (!depositRepository.existsById(depositId))
            throw new IllegalStateException("Deposit with depositId=" + depositId + " does not exist!");

        depositRepository.deleteById(depositId);
    }

    public List<Deposit> getDepositsByUserId(Long userId) {
        if (!userRepository.existsById(userId))
            throw new IllegalStateException("User with userId=" + userId + " does not exist!");

        return depositRepository.findByUserHouse_UserId(userId);
    }

    public List<Deposit> getDepositsByHouseId(Long houseId) {
        if (!houseRepository.existsById(houseId))
            throw new IllegalStateException("House with houseId=" + houseId + " does not exist!");

        return depositRepository.findByUserHouse_HouseId(houseId);
    }

    public void deleteDepositsByUserId(Long userId) {
        depositRepository.deleteByUserHouse_UserId(userId);
    }

    public void deleteDepositsByHouseId(Long houseId) {
        depositRepository.deleteByUserHouse_HouseId(houseId);
    }

    // Meeting
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    public void deleteMeetingById(Long meetingId) {
        if (!meetingRepository.existsById(meetingId))
            throw new IllegalStateException("Meeting with meetingId=" + meetingId + " does not exist!");

        meetingRepository.deleteById(meetingId);
    }

    public List<Meeting> getMeetingsByUserId(Long userId) {
        if (!userRepository.existsById(userId))
            throw new IllegalStateException("User with userId=" + userId + " does not exist!");

        return meetingRepository.findByUserHouse_UserId(userId);
    }

    public List<Meeting> getMeetingsByHouseId(Long houseId) {
        if (!houseRepository.existsById(houseId))
            throw new IllegalStateException("House with houseId=" + houseId + " does not exist!");

        return meetingRepository.findByUserHouse_HouseId(houseId);
    }

    public void deleteMeetingsByUserId(Long userId) {
        meetingRepository.deleteByUserHouse_UserId(userId);
    }

    public void deleteMeetingsByHouseId(Long houseId) {
        meetingRepository.deleteByUserHouse_HouseId(houseId);
    }
}
