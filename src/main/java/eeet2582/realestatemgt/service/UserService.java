package eeet2582.realestatemgt.service;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public AppUser getUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with userId=" + userId + " does not exist!"));
    }

    @Transactional
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId))
            throw new IllegalStateException("User with userId=" + userId + " does not exist!");

        // Delete all classes that depend on current user
        adminService.deleteDepositsByUserId(userId);
        adminService.deleteMeetingsByUserId(userId);
        rentalService.deleteRentalsByUserId(userId);

        userRepository.deleteById(userId);
    }
}
