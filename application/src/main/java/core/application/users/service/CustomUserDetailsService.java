package core.application.users.service;

import core.application.users.models.entities.UserEntity;
import core.application.users.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        System.out.println(userEmail);
        Optional<UserEntity> userEntity = userRepository.findByUserEmail(userEmail);
        if (userEntity.isPresent()) {
            System.out.println(userEntity.get());
            return new CustomUserDetails(userEntity);
        } else {
            throw new UsernameNotFoundException("User not found: " + userEmail);
        }
    }
}
