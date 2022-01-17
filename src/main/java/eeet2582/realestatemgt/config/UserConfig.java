package eeet2582.realestatemgt.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import eeet2582.realestatemgt.helper.LocalDateDeserializer;
import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner userRunner(UserRepository userRepository) {
        return args -> {
            try {
                if (userRepository.count() == 0) {
                    LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer();

                    Reader reader = Files.newBufferedReader(Paths.get("src/main/java/eeet2582/realestatemgt/data/user.json"));
                    Type type = new TypeToken<List<AppUser>>() {
                    }.getType();
                    GsonBuilder builder = new GsonBuilder();

                    builder.registerTypeAdapter(LocalDate.class, localDateDeserializer);

                    Gson gson = builder.create();
                    List<AppUser> users = gson.fromJson(reader, type);

                    userRepository.saveAll(users);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
