package eeet2582.realestatemgt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eeet2582.realestatemgt.helper.StringToDateParser;
import eeet2582.realestatemgt.helper.StringToTimeParser;
import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.Meeting;
import eeet2582.realestatemgt.repository.DepositRepository;
import eeet2582.realestatemgt.repository.MeetingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// Add "admin" Deposit and Meeting objects from file
@Configuration
public class AdminConfig {

    @Bean
    CommandLineRunner adminRunner(DepositRepository depositRepository, MeetingRepository meetingRepository) {
        return args -> {
            try {
                StringToDateParser stringToDateParser = new StringToDateParser();
                StringToTimeParser stringToTimeParser = new StringToTimeParser();

                // Deposit
                Reader depositReader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/deposit.json"));
                Type depositType = new TypeToken<List<Deposit>>() {}.getType();
                GsonBuilder depositBuilder = new GsonBuilder();

                depositBuilder.registerTypeAdapter(LocalDate.class, stringToDateParser);
                depositBuilder.registerTypeAdapter(LocalTime.class, stringToTimeParser);

                Gson depositGson = depositBuilder.create();
                List<Deposit> deposits = depositGson.fromJson(depositReader, depositType);

                depositRepository.saveAll(deposits);

                // Meeting
                Reader meetingReader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/meeting.json"));
                Type meetingType = new TypeToken<List<Meeting>>() {}.getType();
                GsonBuilder meetingBuilder = new GsonBuilder();

                meetingBuilder.registerTypeAdapter(LocalDate.class, stringToDateParser);
                meetingBuilder.registerTypeAdapter(LocalTime.class, stringToTimeParser);

                Gson meetingGson = meetingBuilder.create();
                List<Meeting> meetings = meetingGson.fromJson(meetingReader, meetingType);

                meetingRepository.saveAll(meetings);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
