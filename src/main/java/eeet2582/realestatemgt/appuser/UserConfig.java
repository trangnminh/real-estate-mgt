package eeet2582.realestatemgt.appuser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner userRunner(UserRepository userRepository) {
        return args -> {
            try {
                Gson gson = new Gson();

                Reader reader = Files.newBufferedReader(Paths.get("data/user.json"));

                List<AppUser> users =
                        gson.fromJson(reader,
                                new TypeToken<List<AppUser>>() {}.getType());
                userRepository.saveAll(users);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}
