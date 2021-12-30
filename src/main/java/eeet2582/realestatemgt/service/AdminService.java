package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.helper.UserHouse;
import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.repository.DepositRepository;
import eeet2582.realestatemgt.repository.HouseRepository;
import eeet2582.realestatemgt.repository.MeetingRepository;
import eeet2582.realestatemgt.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private JavaMailSender mailSender;

    public AdminService(DepositRepository depositRepository, MeetingRepository meetingRepository, UserRepository userRepository, HouseRepository houseRepository) {
        this.depositRepository = depositRepository;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.houseRepository = houseRepository;
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

    public Meeting meetingTopic(Long userId, Long houseId, String date, String time, String note) {
        // If ID is provided, try to find the current item, else make new one
//        Meeting meeting = (meetingId != null) ? getMeetingById(meetingId) : new Meeting();
        Meeting meeting = new Meeting();

        // Do input checking here

        // Save the cleaned item
        meeting.setUserHouse(new UserHouse(userId, houseId));
        meeting.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        meeting.setTime(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
        meeting.setNote(note);
        return meeting;
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    @KafkaListener(topics = "meeting", groupId = "group_id")
    public void saveMeetingById(@NotNull Meeting meeting) {
        System.out.println(meeting.getNote());
        meetingRepository.save(meeting);
    }

    public String emailBody(@NotNull LocalTime time, @NotNull LocalDate date, @NotNull UserHouse userHouse) {
        String address = houseRepository.findById(userHouse.getHouseId()).orElseThrow(
                () -> new IllegalStateException("this house id does not exist!")
        ).getAddress();
        String name = houseRepository.findById(userHouse.getHouseId()).orElseThrow(
                () -> new IllegalStateException("this house id does not exist!")
        ).getName();

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(dateFormat);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        String timeString = time.format(timeFormat);

        return "Meeting to rent House " + name + " at " + address + " will be on " + dateString + " at " + timeString;
    }

    @KafkaListener(topics = "meeting", groupId = "group_two")
    public void sendSimpleEmail(@NotNull Meeting meeting) throws IOException {
        SimpleMailMessage sendMessage = new SimpleMailMessage();
        sendMessage.setFrom("pnha1303@gmail.com");
        sendMessage.setTo(userRepository
                .findById(meeting.getUserHouse()
                        .getUserId())
                .orElseThrow(() -> new IllegalStateException("this user id does not exist!")).getEmail());
        sendMessage.setText(emailBody(meeting.getTime(), meeting.getDate(), meeting.getUserHouse()));
        sendMessage.setSubject("Showing house " + houseRepository.findById(meeting.getUserHouse().getHouseId()).orElseThrow(
                () -> new IllegalStateException("this house id does not exist!")
        ).getName());
        mailSender.send(sendMessage);
        System.out.print("Test");
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
