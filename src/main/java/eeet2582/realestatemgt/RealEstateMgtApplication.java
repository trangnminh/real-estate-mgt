package eeet2582.realestatemgt;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ComponentScan
@EnableCaching
public class RealEstateMgtApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealEstateMgtApplication.class, args);
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
        filteredHousesConfig.setMaxSize(1);
        config.put("FilteredHouses", filteredHousesConfig);

        // House by price caches max 1000 entries (dump by LRU)
        CacheConfig filteredHousesByPriceBetweenConfig = new CacheConfig(10 * 60 * 1000, 5 * 60 * 1000);
        filteredHousesByPriceBetweenConfig.setMaxSize(1);
        config.put("FilteredHousesByPriceBetween", filteredHousesByPriceBetweenConfig);

        return new RedissonSpringCacheManager(redissonClient, config);
    }
}
