package core.application.security;

import core.application.users.models.entities.UserEntity;
import core.application.users.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 사용자 세부정보를 로드하는 서비스 클래스
 *
 * {@link UserDetailsService} 인터페이스를 구현하여,
 * 이메일을 사용하여 사용자 정보를 데이터베이스에서 검색
 * 사용자 정보가 존재하면 {@link CustomUserDetails} 객체를 반환하며,
 * 사용자가 존재하지 않는 경우 {@link UsernameNotFoundException} 예외 발생
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * {@link CustomUserDetailsService} 객체를 생성
     *
     * @param userRepository 사용자 정보를 관리하는 {@link UserRepository} 객체
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 주어진 이메일을 사용하여 사용자 세부정보를 로드
     *
     * @param userEmail 사용자 이메일
     * @return {@link UserDetails} 사용자 세부정보
     * @throws UsernameNotFoundException 사용자가 존재하지 않을 경우 발생
     */
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