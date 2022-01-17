package eeet2582.realestatemgt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eeet2582.realestatemgt.data.gson.*;
import eeet2582.realestatemgt.model.*;
import eeet2582.realestatemgt.model.helper.HouseLocation;
import eeet2582.realestatemgt.repository.*;
import eeet2582.realestatemgt.service.UserHouseLocationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BootstrapData {

    private final UserHouseLocationUtil userHouseLocationUtil;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final LocationRepository locationRepository;
    private final DepositRepository depositRepository;
    private final MeetingRepository meetingRepository;
    private final RentalRepository rentalRepository;
    private final PaymentRepository paymentRepository;

    @Bean
    CommandLineRunner run() {
        return args -> {
            LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer();
            LocalTimeDeserializer localTimeDeserializer = new LocalTimeDeserializer();
            IdToUserDeserializer idToUserDeserializer = new IdToUserDeserializer(userHouseLocationUtil);
            IdToHouseDeserializer idToHouseDeserializer = new IdToHouseDeserializer(userHouseLocationUtil);
            IdToRentalDeserializer idToRentalDeserializer = new IdToRentalDeserializer(rentalRepository);

            try {
                populateUsers(localDateDeserializer, localTimeDeserializer);
                populateHouses(localDateDeserializer, localTimeDeserializer);
                populateMeetings(localDateDeserializer, localTimeDeserializer, idToUserDeserializer, idToHouseDeserializer);
                populateDeposits(localDateDeserializer, localTimeDeserializer, idToUserDeserializer, idToHouseDeserializer);
                populateRentals(localDateDeserializer, localTimeDeserializer, idToUserDeserializer, idToHouseDeserializer);
                populatePayments(localDateDeserializer, localTimeDeserializer, idToUserDeserializer, idToHouseDeserializer, idToRentalDeserializer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    private void populatePayments(LocalDateDeserializer localDateDeserializer, LocalTimeDeserializer localTimeDeserializer, IdToUserDeserializer idToUserDeserializer, IdToHouseDeserializer idToHouseDeserializer, IdToRentalDeserializer idToRentalDeserializer) throws IOException {
        if (paymentRepository.count() == 0) {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/payment.json"));
            Type type = new TypeToken<List<Payment>>() {
            }.getType();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalDate.class, localDateDeserializer);
            builder.registerTypeAdapter(LocalTime.class, localTimeDeserializer);
            builder.registerTypeAdapter(AppUser.class, idToUserDeserializer);
            builder.registerTypeAdapter(House.class, idToHouseDeserializer);
            builder.registerTypeAdapter(Rental.class, idToRentalDeserializer);

            Gson gson = builder.create();
            List<Payment> payments = gson.fromJson(reader, type);

            paymentRepository.saveAll(payments);
        }
    }

    private void populateRentals(LocalDateDeserializer localDateDeserializer, LocalTimeDeserializer localTimeDeserializer, IdToUserDeserializer idToUserDeserializer, IdToHouseDeserializer idToHouseDeserializer) throws IOException {
        if (rentalRepository.count() == 0) {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/rental.json"));
            Type type = new TypeToken<List<Rental>>() {
            }.getType();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalDate.class, localDateDeserializer);
            builder.registerTypeAdapter(LocalTime.class, localTimeDeserializer);
            builder.registerTypeAdapter(AppUser.class, idToUserDeserializer);
            builder.registerTypeAdapter(House.class, idToHouseDeserializer);

            Gson gson = builder.create();
            List<Rental> rentals = gson.fromJson(reader, type);

            rentalRepository.saveAll(rentals);
        }
    }

    private void populateDeposits(LocalDateDeserializer localDateDeserializer, LocalTimeDeserializer localTimeDeserializer, IdToUserDeserializer idToUserDeserializer, IdToHouseDeserializer idToHouseDeserializer) throws IOException {
        if (depositRepository.count() == 0) {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/deposit.json"));
            Type type = new TypeToken<List<Deposit>>() {
            }.getType();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalDate.class, localDateDeserializer);
            builder.registerTypeAdapter(LocalTime.class, localTimeDeserializer);
            builder.registerTypeAdapter(AppUser.class, idToUserDeserializer);
            builder.registerTypeAdapter(House.class, idToHouseDeserializer);

            Gson gson = builder.create();
            List<Deposit> deposits = gson.fromJson(reader, type);
            depositRepository.saveAll(deposits);
        }
    }

    private void populateMeetings(LocalDateDeserializer localDateDeserializer, LocalTimeDeserializer localTimeDeserializer, IdToUserDeserializer idToUserDeserializer, IdToHouseDeserializer idToHouseDeserializer) throws IOException {
        if (meetingRepository.count() == 0) {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/meeting.json"));
            Type type = new TypeToken<List<Meeting>>() {
            }.getType();

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalDate.class, localDateDeserializer);
            builder.registerTypeAdapter(LocalTime.class, localTimeDeserializer);
            builder.registerTypeAdapter(AppUser.class, idToUserDeserializer);
            builder.registerTypeAdapter(House.class, idToHouseDeserializer);

            Gson gson = builder.create();
            List<Meeting> meetings = gson.fromJson(reader, type);
            meetingRepository.saveAll(meetings);
        }
    }

    private void populateHouses(LocalDateDeserializer localDateDeserializer, LocalTimeDeserializer localTimeDeserializer) throws IOException {
        if (houseRepository.count() == 0) {
            Type type = new TypeToken<List<House>>() {
            }.getType();
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LocalDate.class, localDateDeserializer);
            builder.registerTypeAdapter(LocalTime.class, localTimeDeserializer);
            Gson gson = builder.create();

            int limit = 1; // Number of files to read
            // Increase limit to get more rows
            for (int i = 1; i <= limit; i++) {
                Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/gen/house_" + i + ".json"));

                List<House> houses = gson.fromJson(reader, type);

                for (House house : houses) {
                    try {
                        houseRepository.save(house);
                    } catch (DataIntegrityViolationException e) {
                        HouseLocation location = userHouseLocationUtil.getHouseLocation(
                                house.getLocation().getCity(),
                                house.getLocation().getDistrict()
                        );
                        house.setLocation(location);
                        houseRepository.save(house);
                    }
                }
            }
        }
    }

    private void populateUsers(LocalDateDeserializer localDateDeserializer, LocalTimeDeserializer localTimeDeserializer) throws IOException {
        if (userRepository.count() == 0) {
            Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/user.json"));
            Type type = new TypeToken<List<AppUser>>() {
            }.getType();
            GsonBuilder builder = new GsonBuilder();

            builder.registerTypeAdapter(LocalDate.class, localDateDeserializer);
            builder.registerTypeAdapter(LocalTime.class, localTimeDeserializer);

            Gson gson = builder.create();
            List<AppUser> users = gson.fromJson(reader, type);

            userRepository.saveAll(users);
        }
    }
}
