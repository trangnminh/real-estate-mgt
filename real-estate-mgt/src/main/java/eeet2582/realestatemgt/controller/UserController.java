package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Implemented: Get all, get one, add, update, delete
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Just get all users (for debug)
    @GetMapping("")
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    // Return users matching query with sort, order and pagination
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/search")
    public Page<AppUser> getFilteredUsers(@RequestParam(value = "query", defaultValue = "") String query,
                                          @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                          @RequestParam(value = "sortBy", defaultValue = "fullName") String sortBy,
                                          @RequestParam(value = "orderBy", defaultValue = "asc") String orderBy) {
        return userService.getFilteredUsers(query, pageNo, pageSize, sortBy, orderBy);
    }

    // Get one user by ID
    @GetMapping("/{userId}")
    public AppUser getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    // Update user by ID or add new one
    @PostMapping("")
    public void saveUserById(@RequestParam(value = "userId", required = false) Long userId,
                             @RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String phoneNumber,
                             @RequestParam String dob,
                             @RequestParam String gender) {
        userService.saveUserById(userId, fullName, email, password, phoneNumber, dob, gender);
    }

    // Delete user by ID
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
