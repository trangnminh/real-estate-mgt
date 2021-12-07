package eeet2582.realestatemgt.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.repository.HouseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class HouseConfig {

    @Bean
    CommandLineRunner houseRunner(HouseRepository houseRepository) {
        return args -> {
            try {
                Gson gson = new Gson();

                Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/house.json"));

                List<House> houses =
                        gson.fromJson(reader,
                                new TypeToken<List<House>>() {}.getType());
                houseRepository.saveAll(houses);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
