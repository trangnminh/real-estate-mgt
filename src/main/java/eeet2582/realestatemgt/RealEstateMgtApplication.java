package eeet2582.realestatemgt;

import eeet2582.realestatemgt.config.ApplicationProperties;
import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        Config config = Config.fromYAML(new File("src/main/resources/redisson.yaml"));
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();

        // Create custom cache with ttl = 10 minutes and maxIdleTime = 6 minutes
        config.put("House", new CacheConfig(10 * 60 * 1000, 5 * 60 * 1000));

        // Queried House list caches max 1000 entries (dump by LRU)
        CacheConfig filteredHousesConfig = new CacheConfig(10 * 60 * 1000, 5 * 60 * 1000);
        filteredHousesConfig.setMaxSize(5);
        config.put("FilteredHouses", filteredHousesConfig);

        // House by price caches max 1000 entries (dump by LRU)
        CacheConfig filteredHousesByPriceBetweenConfig = new CacheConfig(10 * 60 * 1000, 5 * 60 * 1000);
        filteredHousesByPriceBetweenConfig.setMaxSize(5);
        config.put("FilteredHousesByPriceBetween", filteredHousesByPriceBetweenConfig);

        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
