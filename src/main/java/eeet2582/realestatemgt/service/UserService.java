package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// UserService only wires UserRepository, everything else is handled by child services
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AdminService adminService;

    @Autowired
    private final RentalService rentalService;

    public UserService(UserRepository userRepository, AdminService adminService, RentalService rentalService) {
        this.userRepository = userRepository;
        this.adminService = adminService;
        this.rentalService = rentalService;
    }

    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    // Find users matching by full name, email or phone number
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

    // Get one by ID, try to reuse the exception
    public AppUser getUserById(Long userId) {
        if (userRepository.checkIfIdMatch(userId) != 1) { // if user logged in with Google or Facebook
            return userRepository.findAppUserByAuth0Id(userId).orElseThrow(() -> new IllegalStateException("User with userId=" + userId + " does not exist!"));
        }
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with userId=" + userId + " does not exist!"));
    }

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
    public AppUser updateUserById(Long userId, @NotNull AppUser newUser) {
        AppUser oldUser = getUserById(userId);

        if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isBlank() && !oldUser.getPhoneNumber().equals(newUser.getPhoneNumber())) {
            oldUser.setPhoneNumber(newUser.getPhoneNumber());
        }
        if (newUser.getDob() != null && !oldUser.getDob().isEqual(newUser.getDob())) {
            oldUser.setDob(newUser.getDob());
        }
        if (newUser.getGender() != null && !newUser.getGender().isBlank() && !oldUser.getGender().equals(newUser.getGender())) {
            oldUser.setGender(newUser.getGender());
        }

        return oldUser;
    }

    @Transactional
    public void deleteUserById(Long userId) {
        AppUser user = getUserById(userId);

        // Delete all classes that depend on current user
        adminService.deleteDepositsByUserId(userId);
        adminService.deleteMeetingsByUserId(userId);
        rentalService.deleteRentalsByUserId(userId);

        // Finally, delete the user
        userRepository.delete(user);
    }
}
