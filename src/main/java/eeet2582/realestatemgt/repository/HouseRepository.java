package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.house.House;
import eeet2582.realestatemgt.model.house.HouseLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    Page<House> findByPriceBetween(Double low, Double high, Pageable pageable);

    List<House> findByLocation(HouseLocation location);

    List<House> findByPriceBetween(Double low, Double high);

    List<House> findByStatusIn(List<String> list);

    List<House> findByTypeIn(List<String> list);

    List<House> findByLocation_CityAndLocation_District(String city, String district);
}
