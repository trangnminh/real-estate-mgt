package eeet2582.realestatemgt.repository;

import eeet2582.realestatemgt.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Query("select case when (user.userId=?1) then true else false end from AppUser user")
    boolean checkIfIdMatch(Long userId);

    @Query("select user.userId from AppUser user where user.auth0Id=?1")
    Long checkAuthUserFound(Long auth0Id);

    Optional<AppUser> findAppUserByAuth0Id(Long userId);
}
