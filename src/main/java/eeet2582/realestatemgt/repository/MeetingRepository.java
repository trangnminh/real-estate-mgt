package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Page<Meeting> findByUserHouse_UserId(Long userId, Pageable pageable);

    Page<Meeting> findByUserHouse_HouseId(Long houseId, Pageable pageable);

    void deleteByUserHouse_UserId(Long userId);

    void deleteByUserHouse_HouseId(Long houseId);
}
