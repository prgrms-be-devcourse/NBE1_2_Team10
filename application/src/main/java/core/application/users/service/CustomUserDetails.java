package core.application.users.service;

import core.application.users.models.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {
    private final Optional<UserEntity> userEntity;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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
        return bCryptPasswordEncoder.encode(userEntity.get().getUserPw());
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
