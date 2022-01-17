package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.helper.HouseLocation;
import eeet2582.realestatemgt.repository.HouseRepository;
import eeet2582.realestatemgt.repository.LocationRepository;
import eeet2582.realestatemgt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static eeet2582.realestatemgt.config.RedisConfig.LOCATION;


@Service
@RequiredArgsConstructor
public class UserHouseLocationUtil {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final HouseRepository houseRepository;

    @Autowired
    private final LocationRepository locationRepository;

    public AppUser getUserById(Long userId) {
        if (userRepository.checkIfIdMatch(userId) != 1) { // if user logged in with Google or Facebook
            // convert auth0Id user to simpler userId in database
            Long userIdByAuth0Id = userRepository.checkAuthUserFound(userId);
            return userRepository.findById(userIdByAuth0Id).orElseThrow(() -> new IllegalStateException("User with userId=" + userIdByAuth0Id + " does not exist!"));
        }
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with userId=" + userId + " does not exist!"));
    }

    public House getHouseById(Long houseId) {
        return houseRepository
                .findById(houseId)
                .orElseThrow(() -> new IllegalStateException("House with houseId=" + houseId + " does not exist!"));
    }

    @Cacheable(value = LOCATION)
    public HouseLocation getHouseLocation(String formCity, String formDistrict) {
        String city = (formCity != null && !formCity.trim().isEmpty()) ? formCity : "Saigon";
        String district = (formDistrict != null && !formDistrict.trim().isEmpty()) ? formDistrict : "7";
        return locationRepository.findByCityAndDistrict(city, district)
                .orElseThrow(() -> new IllegalStateException("Location not found!"));
    }
}
