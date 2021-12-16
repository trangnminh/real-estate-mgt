package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.House;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HouseRepository extends JpaRepository<House, Long> {

    @Query("SELECT h FROM House h WHERE h.name LIKE %?1%")
    List<House> findHousesByName(String name,Pageable pageable);

}
