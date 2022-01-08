package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Implemented: Get all, get one, add, update, delete
/*
AUTHORIZED USER CAN:
- getUserById : TODO: front-end need to check current user id with user id in params
- updateUserById : update user by id TODO: front-end need to check current user id with user id in params
*/

/*
ADMIN CAN:
- getAllUsers : get all users info
- getFilteredUsers : get all users info with pagination and filters
- deleteUserById : delete users by id
 */

// Bugs: saveUserById

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Just get all users (for debug)
    @GetMapping("")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    // Return users matching query with sort, order and pagination
    // Params aren't mandatory, if not provided will use defaults
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('read:admin-messages')")
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

    // Add new user
    @PostMapping("")
    public void addNewUser(@RequestBody AppUser user) {
        userService.addNewUser(user);
    }

    // Update user by id
    @PutMapping("/{userId}")
    public void updateUserById(@PathVariable(value = "userId") Long userId, @RequestBody AppUser user) {
        userService.updateUserById(userId, user);
    }

    // Delete user by ID
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public void deleteUserById(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
