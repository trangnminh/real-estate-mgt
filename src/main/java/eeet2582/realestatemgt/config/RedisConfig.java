package eeet2582.realestatemgt.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    public static final String HOUSE = "House";
    public static final String HOUSE_SEARCH = "HouseSearch";
    public static final String HOUSE_ID = "#houseId";

    public static final String USER = "User";
    public static final String USERS = "Users";
    public static final String USER_SEARCH = "UserSearch";
    public static final String USER_SEARCH_BY_NAME = "UserSearchByName";
    public static final String USER_ID = "#userId";

    public static final String DEPOSIT = "Deposit";
    public static final String DEPOSITS = "Deposits";
    public static final String DEPOSIT_SEARCH = "DepositSearch";
    public static final String DEPOSIT_ID = "#depositId";

    public static final String MEETING = "Meeting";
    public static final String MEETINGS = "Meetings";
    public static final String MEETINGS_BY_DATE_RANGE = "MeetingsByDateRange";
    public static final String MEETING_SEARCH = "MeetingSearch";
    public static final String MEETING_ID = "#meetingId";

    public static final String RENTAL = "Rental";
    public static final String RENTALS = "Rentals";
    public static final String RENTAL_SEARCH = "RentalSearch";
    public static final String RENTAL_ID = "#rentalId";

    public static final String PAYMENT = "Payment";
    public static final String PAYMENTS = "Payments";
    public static final String PAYMENT_SEARCH_BY_RENTAL = "PaymentSearchByRental";
    public static final String PAYMENT_SEARCH_BY_USER = "PaymentSearchByUser";
    public static final String PAYMENT_ID = "#paymentId";

    public static final String LOCATION = "Location";

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() throws IOException {
        Config config = Config.fromYAML(new File("src/main/resources/redisson.yaml"));
        return Redisson.create(config);
    }

    @Bean
    CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> map = new HashMap<>();

        // Create default cache with ttl = 10 minutes and maxIdleTime = 5 minutes
        CacheConfig config = new CacheConfig(10 * 60 * 1000, 5 * 60 * 1000);

        map.put(HOUSE, config);
        map.put(HOUSE_SEARCH, config);

        map.put(USER, config);
        map.put(USERS, config);
        map.put(USER_SEARCH, config);
        map.put(USER_SEARCH_BY_NAME, config);

        map.put(DEPOSIT, config);
        map.put(DEPOSITS, config);
        map.put(DEPOSIT_SEARCH, config);

        map.put(MEETING, config);
        map.put(MEETINGS, config);
        map.put(MEETINGS_BY_DATE_RANGE, config);
        map.put(MEETING_SEARCH, config);

        map.put(RENTAL, config);
        map.put(RENTALS, config);
        map.put(RENTAL_SEARCH, config);

        map.put(PAYMENT, config);
        map.put(PAYMENTS, config);

        map.put(PAYMENT_SEARCH_BY_RENTAL, config);
        map.put(PAYMENT_SEARCH_BY_USER, config);

        map.put(LOCATION, config);

        return new RedissonSpringCacheManager(redissonClient, map);
    }
}
