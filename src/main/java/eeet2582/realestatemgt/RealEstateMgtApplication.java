package eeet2582.realestatemgt;

import eeet2582.realestatemgt.config.ApplicationProperties;
import eeet2582.realestatemgt.model.house.HouseLocation;
import eeet2582.realestatemgt.repository.LocationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

@EnableCaching
@Log4j2
@SpringBootApplication
@ConfigurationPropertiesScan
public class RealEstateMgtApplication {

    public static void main(String[] args) {
//        SpringApplication.run(RealEstateMgtApplication.class, args);

        final var context = SpringApplication.run(RealEstateMgtApplication.class, args);
        final var serverProps = context.getBean(ServerProperties.class);
        final var applicationProps = context.getBean(ApplicationProperties.class);
        final var port = serverProps.getPort();
        final var clientOriginUrl = applicationProps.getClientOriginUrl();
        final var audience = applicationProps.getAudience();

        if (port == null || port == 0) {
            exitWithMissingEnv(context, "PORT");
        }

        if (Objects.isNull(clientOriginUrl) || clientOriginUrl.isBlank()) {
            exitWithMissingEnv(context, "CLIENT_ORIGIN_URL");
        }

        if (Objects.isNull(audience) || audience.isEmpty()) {
            exitWithMissingEnv(context, "AUTH0_AUDIENCE");
        }
    }

    private static void exitWithMissingEnv(final ConfigurableApplicationContext context, final String env) {
        final var exitCode = SpringApplication.exit(context, () -> 1);

        log.error("[Fatal] Missing or empty environment variable: {}", env);
        System.exit(exitCode);
    }

    @Bean
    CommandLineRunner populateLocations(LocationRepository locationRepository) {
        return args -> {
            HouseLocation location = new HouseLocation("Saigon", "7");
            locationRepository.save(location);
        };
    }
}