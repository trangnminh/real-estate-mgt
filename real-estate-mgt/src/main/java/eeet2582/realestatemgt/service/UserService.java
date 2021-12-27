package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with userId=" + userId + " does not exist!"));
    }

    // Transactional means "all or nothing", if the transaction fails midway nothing is saved
    @Transactional
    public void saveUserById(Long userId, String fullName, String email, String password, String phoneNumber, String dob, String gender) {
        // If ID is provided, try to find the current user, else make new one
        AppUser user = (userId != null) ? getUserById(userId) : new AppUser();

        // Do input checking here

        // Save the cleaned user
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setDob(LocalDate.parse(dob, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        user.setGender(gender);
        userRepository.save(user);
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
