package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.model.form.DepositForm;
import eeet2582.realestatemgt.repository.DepositRepository;
import eeet2582.realestatemgt.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Objects;

import static eeet2582.realestatemgt.config.RedisConfig.*;

// Handle Deposit and Meeting operations
@Service
@RequiredArgsConstructor
public class AdminService {

    public static final String SENDER_MAIL = "eeet2582.realestatemgt@gmail.com";

    @Autowired
    private final UserHouseLocationUtil userHouseLocationUtil;

    @Autowired
    private final MeetingRepository meetingRepository;

    @Autowired
    private final DepositRepository depositRepository;

    @Autowired
    private final JavaMailSender mailSender;

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    // --- DEPOSIT --- //

    @Cacheable(value = DEPOSITS)
    public List<Deposit> getAllDeposits() {
        return depositRepository.findAll();
    }

    // Return paginated deposits of the provided user or house or just all deposits
    @Cacheable(value = DEPOSIT_SEARCH)
    public Page<Deposit> getFilteredDepositsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, @NotNull String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending().and(Sort.by("time").ascending()));
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").descending().and(Sort.by("time").descending()));
        }

        if (userId != null) {
            // Try to find the associated user
            AppUser user = userHouseLocationUtil.getUserById(userId);
            return depositRepository.findByUser(user, pageable);
        } else if (houseId != null) {
            // Try to find the associated house
            House house = userHouseLocationUtil.getHouseById(houseId);
            return depositRepository.findByHouse(house, pageable);
        } else {
            return depositRepository.findAll(pageable);
        }
    }

    // Get one by ID, try to reuse the exception
    @Cacheable(key = DEPOSIT_ID, value = DEPOSIT)
    public Deposit getDepositById(Long depositId) {
        if (!depositRepository.existsById(depositId))
            throw new IllegalStateException("Deposit with depositId=" + depositId + " does not exist!");

        return depositRepository.getById(depositId);
    }

    // Add new one
    @Caching(evict = {
            @CacheEvict(value = DEPOSITS, allEntries = true),
            @CacheEvict(value = DEPOSIT_SEARCH, allEntries = true)
    })
    public Deposit addNewDeposit(@NotNull DepositForm form) {
        if (form.getUserId() != null && form.getHouseId() != null) {
            // Find the associated user and house
            AppUser user = userHouseLocationUtil.getUserById(form.getUserId());
            House house = userHouseLocationUtil.getHouseById(form.getHouseId());

            // Create new entity from param
            Deposit deposit = new Deposit(
                    user,
                    house,
                    form.getAmount(),
                    LocalDate.parse(form.getDate(), dateFormat),
                    LocalTime.parse(form.getTime(), timeFormat),
                    form.getNote()
            );
            return depositRepository.save(deposit);
        }
        return null;
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = DEPOSIT, key = DEPOSIT_ID),
            @CacheEvict(value = DEPOSITS, allEntries = true),
            @CacheEvict(value = DEPOSIT_SEARCH, allEntries = true)
    })
    public Deposit updateDepositById(Long depositId, @NotNull DepositForm form) {
        if (form.getUserId() != null && form.getHouseId() != null) {
            Deposit deposit = getDepositById(depositId);

            // Find the associated user and house
            AppUser user = userHouseLocationUtil.getUserById(form.getUserId());
            House house = userHouseLocationUtil.getHouseById(form.getHouseId());
            deposit.setUser(user);
            deposit.setHouse(house);

            if (form.getAmount() != null && !Objects.equals(form.getAmount(), deposit.getAmount())) {
                deposit.setAmount(form.getAmount());
            }

            if (form.getDate() != null && !deposit.getDate().isEqual(LocalDate.parse(form.getDate(), dateFormat))) {
                deposit.setDate(LocalDate.parse(form.getDate(), dateFormat));
            }

            if (form.getTime() != null && !deposit.getTime().equals(LocalTime.parse(form.getTime(), timeFormat))) {
                deposit.setTime(LocalTime.parse(form.getTime(), timeFormat));
            }

            if (form.getNote() != null && !form.getNote().isBlank() && !deposit.getNote().equals(form.getNote())) {
                deposit.setNote(form.getNote());
            }
            return depositRepository.save(deposit);
        }
        return null;
    }

    @Caching(evict = {
            @CacheEvict(value = DEPOSIT, key = DEPOSIT_ID),
            @CacheEvict(value = DEPOSITS, allEntries = true),
            @CacheEvict(value = DEPOSIT_SEARCH, allEntries = true)
    })
    public void deleteDepositById(Long depositId) {
        Deposit deposit = getDepositById(depositId);
        depositRepository.delete(deposit);
    }

    // --- MEETING--- //

    @Cacheable(value = MEETINGS)
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    @Cacheable(key = "#range", value = MEETINGS_BY_DATE_RANGE)
    public List<Meeting> getMeetingsByDateRange(String range) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now();

        switch (range) {
            case "today":
                break;
            case "week":
                startDate = LocalDate.now(ZoneId.systemDefault())
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endDate = LocalDate.now(ZoneId.systemDefault())
                        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                break;
            case "month":
                startDate = LocalDate.now(ZoneId.systemDefault())
                        .with(TemporalAdjusters.firstDayOfMonth());
                endDate = LocalDate.now(ZoneId.systemDefault())
                        .with(TemporalAdjusters.lastDayOfMonth());
                break;
        }

        System.out.println("Search for Meetings from: " +
                startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " to " +
                endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        return meetingRepository.findByDateBetween(startDate, endDate);
    }

    // Return paginated meetings of the provided user or house or just all meetings
    @Cacheable(value = MEETING_SEARCH)
    public Page<Meeting> getFilteredMeetingsAllOrByUserIdOrByHouseId(Long userId, Long houseId, int pageNo, int pageSize, @NotNull String orderBy) {
        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").ascending().and(Sort.by("time").ascending()));
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by("date").descending().and(Sort.by("time").descending()));
        }

        if (userId != null) {
            // Try to find the associated user
            AppUser user = userHouseLocationUtil.getUserById(userId);
            return meetingRepository.findByUser(user, pageable);
        } else if (houseId != null) {
            // Try to find the associated house
            House house = userHouseLocationUtil.getHouseById(houseId);
            return meetingRepository.findByHouse(house, pageable);
        } else {
            return meetingRepository.findAll(pageable);
        }
    }

    // Get one by ID, try to reuse the exception
    @Cacheable(key = MEETING_ID, value = MEETING)
    public Meeting getMeetingById(Long meetingId) {
        if (!meetingRepository.existsById(meetingId))
            throw new IllegalStateException("Meeting with meetingId=" + meetingId + " does not exist!");

        return meetingRepository.getById(meetingId);
    }

    // Create a new meeting or update one from params
    public Meeting createMeetingTopic(Long userId, Long houseId, String date, String time, String note) {
        // If ID is provided, try to find the current item, else make new one
        Meeting meeting = new Meeting();

        // Save the cleaned item
        meeting.setUser(userHouseLocationUtil.getUserById(userId));
        meeting.setHouse(userHouseLocationUtil.getHouseById(houseId));
        meeting.setDate(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        meeting.setTime(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm")));
        meeting.setNote(note);
        return meeting;
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    // Save the meeting retrieved from Kafka into database
    @Transactional
    @KafkaListener(topics = "meeting", groupId = "group_id")
    @Caching(evict = {
            @CacheEvict(value = MEETINGS, allEntries = true),
            @CacheEvict(value = MEETINGS_BY_DATE_RANGE, allEntries = true),
            @CacheEvict(value = MEETING_SEARCH, allEntries = true)
    })
    public Meeting addNewMeeting(@NotNull Meeting meeting) {
        return meetingRepository.save(meeting);
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
        AppUser user = userHouseLocationUtil.getUserById(meeting.getUser().getUserId());
        House house = userHouseLocationUtil.getHouseById(meeting.getHouse().getHouseId());

        SimpleMailMessage sendMessage = new SimpleMailMessage();
        sendMessage.setFrom(SENDER_MAIL);
        sendMessage.setTo(user.getEmail());

        String dateString = meeting.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String timeString = meeting.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        sendMessage.setText(createEmailBody(dateString, timeString, user.getFullName(), house.getName(), house.getAddress()));
        sendMessage.setSubject("[HOUSE MEETING] " + dateString + " " + timeString);
        mailSender.send(sendMessage);
    }

    @Caching(evict = {
            @CacheEvict(value = MEETING, key = MEETING_ID),
            @CacheEvict(value = MEETINGS, allEntries = true),
            @CacheEvict(value = MEETINGS_BY_DATE_RANGE, allEntries = true),
            @CacheEvict(value = MEETING_SEARCH, allEntries = true)
    })
    public void deleteMeetingById(Long meetingId) {
        Meeting meeting = getMeetingById(meetingId);
        meetingRepository.delete(meeting);
    }
}
