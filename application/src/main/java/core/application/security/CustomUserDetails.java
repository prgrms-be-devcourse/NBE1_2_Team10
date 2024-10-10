package core.application.security;

import core.application.users.models.entities.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * 사용자 세부정보를 담고 있는 클래스
 *
 * Spring Security에서 사용자의 정보를 담기 위해 구현된 {@link UserDetails} 인터페이스
 * 사용자의 인증 정보(이메일, 비밀번호, 권한 등)를 제공하며,
 * 비밀번호는 BCrypt 알고리즘으로 암호화하여 저장
 */
public class CustomUserDetails implements UserDetails {
    @Getter
    private final Optional<UserEntity> userEntity;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * {@link CustomUserDetails} 객체를 생성
     *
     * @param userEntity 사용자 정보가 담긴 {@link Optional<UserEntity>} 객체
     */
    public CustomUserDetails(Optional<UserEntity> userEntity) {
        this.userEntity = userEntity;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {
                return userEntity.get().getRole().toString();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return userEntity.get().getUserPw();
    }

    @Override
    public String getUsername() {
        return userEntity.get().getUserName();
    }

    public String getUserEmail() {
        return userEntity.get().getUserEmail().toString();
    }

    public UUID getUserId() {
        return userEntity.get().getUserId();
    }

    public String getUserRole() {
        return userEntity.get().getRole().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
