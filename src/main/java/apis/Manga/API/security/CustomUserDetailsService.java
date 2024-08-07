package apis.Manga.API.security;

import apis.Manga.API.entity.User;
import apis.Manga.API.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private  UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository= userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user =userRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("geht net") );

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}
