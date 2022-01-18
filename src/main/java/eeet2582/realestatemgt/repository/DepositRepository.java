package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.Deposit;
import eeet2582.realestatemgt.model.House;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    Page<Deposit> findByUser(AppUser user, Pageable pageable);

    Page<Deposit> findByHouse(House house, Pageable pageable);
}
