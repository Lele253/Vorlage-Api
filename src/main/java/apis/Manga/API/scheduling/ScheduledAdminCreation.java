package apis.Manga.API.scheduling;

import apis.Manga.API.entity.User;
import apis.Manga.API.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableScheduling
public class ScheduledAdminCreation {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public ScheduledAdminCreation(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    @Scheduled(initialDelay = 0,fixedRate = Long.MAX_VALUE)
    public ResponseEntity<Object> createAdminUserAdminAdmin() {
        Optional<User> vorhanden = userRepository.findByEmail("admin");
        if (!vorhanden.isPresent()){
        User user = new User();
        user.setEmail("admin");
        user.setUsername("Admin");
            user.setStatus("Admin");

            user.setPassword(passwordEncoder.encode("admin"));

        User createdAdmin = userRepository.save(user);
        return ResponseEntity.ok(createdAdmin);

        }
        return null;
    }


}
