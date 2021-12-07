package eeet2582.realestatemgt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eeet2582.realestatemgt.helper.StringToDateParser;
import eeet2582.realestatemgt.helper.StringToTimeParser;
import eeet2582.realestatemgt.helper.IdToRentalParser;
import eeet2582.realestatemgt.model.Payment;
import eeet2582.realestatemgt.model.Rental;
import eeet2582.realestatemgt.repository.PaymentRepository;
import eeet2582.realestatemgt.repository.RentalRepository;
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

// Add Rental and Payment objects from file
@Configuration
public class RentalConfig {

    @Bean
    CommandLineRunner rentalRunner(RentalRepository rentalRepository, PaymentRepository paymentRepository) {
        return args -> {
            try {
                StringToDateParser stringToDateParser = new StringToDateParser();
                StringToTimeParser stringToTimeParser = new StringToTimeParser();

                // First read rentals
                Reader rentalReader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/rental.json"));
                Type rentalType = new TypeToken<List<Rental>>() {}.getType();
                GsonBuilder rentalBuilder = new GsonBuilder();

                rentalBuilder.registerTypeAdapter(LocalDate.class, stringToDateParser);

                Gson rentalGson = rentalBuilder.create();
                List<Rental> rentals = rentalGson.fromJson(rentalReader, rentalType);

                rentalRepository.saveAll(rentals);

                // ...then read payments and convert JSON data field "rental" to Rental objects
                Reader paymentReader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/data/payment.json"));
                Type paymentType = new TypeToken<List<Payment>>() {}.getType();
                GsonBuilder paymentBuilder = new GsonBuilder();

                paymentBuilder.registerTypeAdapter(LocalDate.class, stringToDateParser);
                paymentBuilder.registerTypeAdapter(LocalTime.class, stringToTimeParser);
                paymentBuilder.registerTypeAdapter(Rental.class, new IdToRentalParser(rentalRepository));

                Gson paymentGson = paymentBuilder.create();
                List<Payment> payments = paymentGson.fromJson(paymentReader, paymentType);

                paymentRepository.saveAll(payments);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
