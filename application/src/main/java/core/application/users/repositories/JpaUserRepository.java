package core.application.users.repositories;

import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    Boolean existsByUserEmail(String email);

    Optional<UserEntity> findByUserEmail(String email);

    Optional<UserEntity> findByUserEmailAndUserPw(String email, String pw);

    List<UserEntity> findByRole(UserRole role);
}
