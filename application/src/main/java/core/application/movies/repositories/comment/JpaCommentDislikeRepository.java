package core.application.movies.repositories.comment;

import core.application.movies.models.entities.CommentDislike;
import core.application.movies.models.entities.CommentEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentDislikeRepository extends JpaRepository<CommentDislike, Long> {
    Boolean existsByUser_UserIdAndComment_CommentId(UUID userId, Long commentId);

    void deleteByCommentAndUser_UserId(CommentEntity comment, UUID userId);
}
