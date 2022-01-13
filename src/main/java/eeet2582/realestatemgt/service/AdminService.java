package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.helper.UserHouse;
import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.repository.DepositRepository;
import eeet2582.realestatemgt.repository.HouseRepository;
import eeet2582.realestatemgt.repository.MeetingRepository;
import eeet2582.realestatemgt.repository.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

// Handle Deposit and Meeting operations
@Service
public class AdminService {

    public static final String SENDER_MAIL = "eeet2582.realestatemgt@gmail.com";
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private final MeetingRepository meetingRepository;

    @Autowired
    private final DepositRepository depositRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final JavaMailSender mailSender;

    public AdminService(MeetingRepository meetingRepository,
                        DepositRepository depositRepository,
                        UserRepository userRepository,
                        HouseRepository houseRepository,
                        JavaMailSender mailSender) {
        this.meetingRepository = meetingRepository;
        this.depositRepository = depositRepository;
        this.userRepository = userRepository;
        this.houseRepository = houseRepository;
        this.mailSender = mailSender;
    }

    // --- DEPOSIT --- //

    public List<Deposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    // Return paginated payments of the provided user or house or just all payments
    public Page<Deposit> getFilteredDepositsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, String sortBy, String orderBy) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;
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

    // Add new one
    public Deposit addNewDeposit(Deposit deposit) {
        LOGGER.info("addNewDeposit: " + deposit.toString());
        return depositRepository.save(deposit);
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    public Deposit updateDepositById(Long depositId, @NotNull Deposit newDeposit) {
        Deposit oldDeposit = getDepositById(depositId);

        if (newDeposit.getUserHouse().getHouseId() != null && newDeposit.getUserHouse().getUserId() != null) {
            oldDeposit.setUserHouse(new UserHouse(newDeposit.getUserHouse().getUserId(), newDeposit.getUserHouse().getHouseId()));
        }

        if (newDeposit.getAmount() != null && !Objects.equals(newDeposit.getAmount(), oldDeposit.getAmount())) {
            oldDeposit.setAmount(newDeposit.getAmount());
        }

        if (newDeposit.getDate() != null && !oldDeposit.getDate().isEqual(newDeposit.getDate())) {
            oldDeposit.setDate(newDeposit.getDate());
        }

        if (newDeposit.getTime() != null && !oldDeposit.getTime().equals(newDeposit.getTime())) {
            oldDeposit.setTime(newDeposit.getTime());
        }

        if (newDeposit.getNote() != null && !newDeposit.getNote().isBlank() && !oldDeposit.getNote().equals(newDeposit.getNote())) {
            oldDeposit.setNote(newDeposit.getNote());
        }

        return oldDeposit;
    }

    public void deleteDepositById(Long depositId) {
        Deposit deposit = getDepositById(depositId);
        depositRepository.delete(deposit);
    }

    @Transactional
    public void deleteDepositsByUserId(Long userId) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;
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
    public Page<Meeting> getFilteredMeetingsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, @NotNull String orderBy) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;

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
    public Meeting createMeetingTopic(Long userId, Long houseId, String date, String time, String note) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;

        // If ID is provided, try to find the current item, else make new one
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
    // Save the meeting retrieved from Kafka into database
    @Transactional
    @KafkaListener(topics = "meeting", groupId = "group_id")
    public void saveMeetingById(@NotNull Meeting meeting) {
        LOGGER.info("saveMeetingById: " + meeting);
        meetingRepository.save(meeting);
    }

    // Create the email body for meeting reminder
    public String createEmailBody(@NotNull String date,
                                  @NotNull String time,
                                  @NotNull String userFullName,
                                  @NotNull String houseName,
                                  @NotNull String houseAddress) {
        return "Dear " + userFullName + ",\n\n" +
                "We are writing to confirm your meeting for House " +
                "[" + houseName + "]" +
                " on " + date + " at " + time + ".\n" +
                "Please be present at address [" + houseAddress + "] 10 minutes prior to the meeting time.\n" +
                "Let us know if you wish to make any changes.\n\n" +
                "Kind regards,\n" + "Real Estate Agency";
    }

    @KafkaListener(topics = "meeting", groupId = "email_group")
    public void sendSimpleEmail(@NotNull Meeting meeting) {
        AppUser user = userRepository.getById(meeting.getUserHouse().getUserId());
        House house = houseRepository.getById(meeting.getUserHouse().getHouseId());

        SimpleMailMessage sendMessage = new SimpleMailMessage();
        sendMessage.setFrom(SENDER_MAIL);
        sendMessage.setTo(user.getEmail());

        String dateString = meeting.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timeString = meeting.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        sendMessage.setText(createEmailBody(dateString, timeString, user.getFullName(), house.getName(), house.getAddress()));
        sendMessage.setSubject("[HOUSE MEETING] " + dateString + " " + timeString);
        mailSender.send(sendMessage);
    }

    public void deleteMeetingById(Long meetingId) {
        Meeting meeting = getMeetingById(meetingId);
        meetingRepository.delete(meeting);
    }

    @Transactional
    public void deleteMeetingsByUserId(Long userId) {
        // convert auth0Id user to simpler userId in database
        Long auth0Id = userRepository.checkAuthUserFound(userId);
        userId = auth0Id != null ? auth0Id : userId;
        meetingRepository.deleteByUserHouse_UserId(userId);
    }

    @Transactional
    public void deleteMeetingsByHouseId(Long houseId) {
        meetingRepository.deleteByUserHouse_HouseId(houseId);
    }
}
