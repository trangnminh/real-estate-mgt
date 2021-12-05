package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.AppUser;
import eeet2582.realestatemgt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<AppUser> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{userId}")
    public AppUser getUserById(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUserById(@PathVariable("userId") Long userId) { userService.deleteUserById(userId); }
}
