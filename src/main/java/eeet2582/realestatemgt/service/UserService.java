package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static eeet2582.realestatemgt.config.RedisConfig.*;

// UserService only wires UserRepository, everything else is handled by child services
@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserHouseLocationUtil userHouseLocationUtil;

    @Cacheable(value = USERS)
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    // Find users matching by full name, email or phone number
    @Cacheable(value = USER_SEARCH)
    public Page<AppUser> getFilteredUsers(String query, int pageNo, int pageSize, String sortBy, String orderBy) {
        AppUser user = new AppUser();
        user.setFullName(query);
        user.setEmail(query);
        user.setPhoneNumber(query);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("fullName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("phoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<AppUser> example = Example.of(user, matcher);

        Pageable pageable;
        if (orderBy.equals("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
        return userRepository.findAll(example, pageable);
    }

    // Find users by full name
    @Cacheable(value = USER_SEARCH_BY_NAME)
    public List<AppUser> getUsersByName(String name, int pageNo) {
        Pageable limit = PageRequest.of(pageNo, 10);
        return userRepository.findAppUserByFullName(name, limit);
    }

    // Get one by ID, try to reuse the exception
    public AppUser getUserById(Long userId) {
        return userHouseLocationUtil.getUserById(userId);
    }

    // Add new one
    @Caching(evict = {
            @CacheEvict(value = USERS, allEntries = true),
            @CacheEvict(value = USER_SEARCH, allEntries = true),
            @CacheEvict(value = USER_SEARCH_BY_NAME, allEntries = true)
    })
    public AppUser addNewUser(AppUser user) {
        // Do input checking here
        if (user.getAuth0Id() != null && userRepository.checkAuthUserFound(user.getAuth0Id()) == null) {
            // Save the cleaned user
            return userRepository.save(user);
        } else if (user.getAuth0Id() == null) {
            return userRepository.save(user);
        }
        return null;
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = USER, key = USER_ID),
            @CacheEvict(value = USERS, allEntries = true),
            @CacheEvict(value = USER_SEARCH, allEntries = true),
            @CacheEvict(value = USER_SEARCH_BY_NAME, allEntries = true)
    })
    public AppUser updateUserById(Long userId, @NotNull AppUser newUser) {
        AppUser oldUser = getUserById(userId);

        if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isBlank() && !oldUser.getPhoneNumber().equals(newUser.getPhoneNumber())) {
            oldUser.setPhoneNumber(newUser.getPhoneNumber());
        }
        if (newUser.getDob() != null && !oldUser.getDob().equals(newUser.getDob())) {
            oldUser.setDob(newUser.getDob());
        }
        if (newUser.getGender() != null && !newUser.getGender().isBlank() && !oldUser.getGender().equals(newUser.getGender())) {
            oldUser.setGender(newUser.getGender());
        }

        return oldUser;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = USER, key = USER_ID),
            @CacheEvict(value = USERS, allEntries = true),
            @CacheEvict(value = USER_SEARCH, allEntries = true),
            @CacheEvict(value = USER_SEARCH_BY_NAME, allEntries = true)
    })
    public void deleteUserById(Long userId) {
        AppUser user = getUserById(userId);
        userRepository.delete(user);
    }
}
