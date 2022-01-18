package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    Page<Rental> findByUser(AppUser user, Pageable pageable);

    Page<Rental> findByHouse(House house, Pageable pageable);
}
