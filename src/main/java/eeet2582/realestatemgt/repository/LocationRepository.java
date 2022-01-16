package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.house.HouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<HouseLocation, Long> {

    Optional<HouseLocation> findByCityAndDistrict(String city, String district);
}
