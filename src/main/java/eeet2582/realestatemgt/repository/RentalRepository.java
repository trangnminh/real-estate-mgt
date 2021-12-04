package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    void deleteByUserHouse_UserId(Long userId);
    void deleteByUserHouse_HouseId(Long houseId);
}
