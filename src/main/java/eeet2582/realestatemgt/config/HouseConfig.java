package eeet2582.realestatemgt.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eeet2582.realestatemgt.model.house.House;
import eeet2582.realestatemgt.model.house.HouseLocation;
import eeet2582.realestatemgt.repository.HouseRepository;
import eeet2582.realestatemgt.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class HouseConfig {

    @Bean
    CommandLineRunner houseRunner(HouseRepository houseRepository, LocationRepository locationRepository) {
        return args -> {
            try {
                if (houseRepository.count() == 0) {
                    Gson gson = new Gson();

                    int limit = 1; // Number of files to read

                    // Increase limit to get more rows
                    for (int i = 1; i <= limit; i++) {
//                        Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/house/house_" + i + ".json"));
                        Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/house.json"));

                        List<House> houses =
                                gson.fromJson(reader,
                                        new TypeToken<List<House>>() {
                                        }.getType());

                        for (House house : houses) {
                            try {
                                houseRepository.save(house);
                            } catch (DataIntegrityViolationException e) {
                                HouseLocation location =
                                        locationRepository.findByCityAndDistrict(
                                                        house.getLocation().getCity(), house.getLocation().getDistrict())
                                                .orElseThrow(() -> new IllegalStateException("Location not found!"));
                                house.setLocation(location);
                                houseRepository.save(house);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
