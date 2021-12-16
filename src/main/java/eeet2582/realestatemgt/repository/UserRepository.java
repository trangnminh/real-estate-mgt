package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.model.House;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE u.fullName LIKE %?1%")
    List<AppUser> findAppUsersByName(String name, Pageable pageable);

    @Query("SELECT u FROM AppUser u WHERE u.email LIKE %?1%")
    List<AppUser> findAppUsersByEmail(String email, Pageable pageable);

    @Query("SELECT u FROM AppUser u WHERE u.phoneNumber = ?1")
    List<AppUser> findAppUsersByPhoneNumber(String phoneNumber, Pageable pageable);
}
