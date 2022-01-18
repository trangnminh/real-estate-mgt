package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    Page<Meeting> findByUser(AppUser user, Pageable pageable);

    Page<Meeting> findByHouse(House house, Pageable pageable);

    List<Meeting> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
