package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.Deposit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    Page<Deposit> findByUserHouse_UserId(Long userId, Pageable pageable);

    Page<Deposit> findByUserHouse_HouseId(Long houseId, Pageable pageable);

    void deleteByUserHouse_UserId(Long userId);

    void deleteByUserHouse_HouseId(Long houseId);
}
