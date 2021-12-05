package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> findByUserHouse_UserId(Long userId);
    List<Deposit> findByUserHouse_HouseId(Long houseId);
    void deleteByUserHouse_UserId(Long userId);
    void deleteByUserHouse_HouseId(Long houseId);
}
