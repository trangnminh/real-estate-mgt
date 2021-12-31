package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.helper.UserHouse;
import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.repository.DepositRepository;
import eeet2582.realestatemgt.repository.MeetingRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public static final String SENDER_MAIL = "eeet2582.realestatemgt@gmail.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);
    @Autowired
    private final MeetingRepository meetingRepository;
    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final DepositRepository depositRepository;
    private UserService userService;
    private HouseService houseService;

    public AdminService(DepositRepository depositRepository, MeetingRepository meetingRepository, JavaMailSender mailSender) {
        this.depositRepository = depositRepository;
        this.meetingRepository = meetingRepository;
        this.mailSender = mailSender;
    }

    public UserService getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public HouseService getHouseService() {
        return houseService;
    }

    @Autowired
    public void setHouseService(HouseService houseService) {
        this.houseService = houseService;
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

    // Create a new meeting or update one from params
    public Meeting createMeetingTopic(Long meetingId, Long userId, Long houseId, String date, String time, String note) {
        // If ID is provided, try to find the current item, else make new one
        Meeting meeting = (meetingId != null) ? getMeetingById(meetingId) : new Meeting();

        // Do input checking here

        // Save the cleaned item
        meeting.setUserHouse(new UserHouse(userId, houseId));
        meeting.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        meeting.setTime(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
        meeting.setNote(note);
        return meeting;
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    // Save the meeting retrieved from Kafka into database
    @Transactional
    @KafkaListener(topics = "meeting", groupId = "group_id")
    public void saveMeetingById(@NotNull Meeting meeting) {
        LOGGER.info("saveMeetingById: " + meeting);
        meetingRepository.save(meeting);
    }

    // Create the email body for meeting reminder
    public String createEmailBody(@NotNull LocalTime time, @NotNull LocalDate date, @NotNull UserHouse userHouse) {
        AppUser user = userService.getUserById(userHouse.getUserId());
        House house = houseService.getHouseById(userHouse.getHouseId());

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString = date.format(dateFormat);
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        String timeString = time.format(timeFormat);

        return "Dear " + user.getFullName() + ",\n\n" +
                "We are writing to confirm your meeting for House " +
                "[" + house.getName() + "]" +
                " on " + dateString + " at " + timeString + ".\n" +
                "Please be present at address [" + house.getAddress() + "] 10 minutes prior to the meeting time.\n" +
                "Let us know if you wish to make any changes.\n\n" +
                "Kind regards,\n" + "Real Estate Agency";
    }

    @KafkaListener(topics = "meeting", groupId = "email_group")
    public void sendSimpleEmail(@NotNull Meeting meeting) throws IOException {
        AppUser user = userService.getUserById(meeting.getUserHouse().getUserId());
        House house = houseService.getHouseById(meeting.getUserHouse().getHouseId());

        SimpleMailMessage sendMessage = new SimpleMailMessage();
        sendMessage.setFrom(SENDER_MAIL);
        sendMessage.setTo(user.getEmail());
        sendMessage.setText(createEmailBody(meeting.getTime(), meeting.getDate(), meeting.getUserHouse()));
        sendMessage.setSubject("[HOUSE MEETING] " + meeting.getDate() + " " + meeting.getTime());
        mailSender.send(sendMessage);
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