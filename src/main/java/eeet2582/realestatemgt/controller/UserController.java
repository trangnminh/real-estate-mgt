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

    @PutMapping("/user/{userId}")
    public void updateUserById(@RequestBody AppUser user) {
        userService.updateUserById(user);
    }

    // get all users with pagination and sort
    @GetMapping(path="/users/find/{page}/{category}")
    public List<AppUser> sortAndPagination(@PathVariable("page") int currPage, @PathVariable("category") String category){
        return userService.sortAndPagination(currPage,category);
    }

    // get all users with name
    @GetMapping(path="/users/findByName/{page}/{name}")
    public List<AppUser> findUsersByName(@PathVariable("page") int currPage,@PathVariable("name") String name){
        return userService.findUsersByName(currPage,name);
    }

    // get all users with phone number
    @GetMapping(path="/users/findByPhone/{page}/{phone}")
    public List<AppUser> findUsersByPhone(@PathVariable("page") int currPage,@PathVariable("phone") String phone){
        return userService.findUsersByPhoneNumber(currPage,phone);
    }

    // get all users with email
    @GetMapping(path="/users/findByName/{page}/{email}")
    public List<AppUser> findUsersByEmail(@PathVariable("page") int currPage,@PathVariable("email") String email){
        return userService.findUsersByEmail(currPage,email);
    }
}
