package core.application.users.repositories;

import core.application.users.models.entities.DibEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDibRepository extends JpaRepository<DibEntity, Long> {
    List<DibEntity> findByUser_UserId(UUID userId);

    Optional<DibEntity> findByUser_UserIdAndMovie_MovieId(UUID userId, String movieId);

    void deleteByUser_UserId(UUID userId);

    void deleteByUser_UserIdAndMovie_MovieId(UUID userId, String movieId);
}
