package eeet2582.realestatemgt.appuser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AppUser> getAll() {
        return userRepository.findAll();
    }

    public AppUser getById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with userId=" + userId + " does not exist!"));
    }

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
