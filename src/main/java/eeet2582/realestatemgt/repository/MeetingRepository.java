package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByUserHouse_UserId(Long userId);
    List<Meeting> findByUserHouse_HouseId(Long houseId);
    void deleteByUserHouse_UserId(Long userId);
    void deleteByUserHouse_HouseId(Long houseId);
}
