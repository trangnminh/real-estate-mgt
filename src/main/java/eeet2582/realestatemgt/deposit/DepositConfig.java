package eeet2582.realestatemgt.deposit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eeet2582.realestatemgt.helper.LocalDateAdapter;
import eeet2582.realestatemgt.helper.LocalTimeAdapter;
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

@Configuration
public class DepositConfig {

    @Bean
    CommandLineRunner depositRunner(DepositRepository depositRepository) {
        return args -> {
            try {
                Reader reader = Files.newBufferedReader(Paths.get("data/deposit.json"));
                Type listType = new TypeToken<List<Deposit>>() {}.getType();
                GsonBuilder gsonBuilder = new GsonBuilder();

                gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
                gsonBuilder.registerTypeAdapter(LocalTime.class, new LocalTimeAdapter());

                Gson gson = gsonBuilder.create();
                List<Deposit> deposits = gson.fromJson(reader, listType);

                depositRepository.saveAll(deposits);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
