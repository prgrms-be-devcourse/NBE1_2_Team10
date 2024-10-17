package core.application.movies.repositories.comment;

import core.application.movies.models.entities.CommentEntity;
import core.application.movies.models.entities.CommentLike;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Boolean existsByUser_UserIdAndComment_CommentId(UUID userId, Long commentId);

    void deleteByCommentAndUser_UserId(CommentEntity commentId, UUID userId);
}
