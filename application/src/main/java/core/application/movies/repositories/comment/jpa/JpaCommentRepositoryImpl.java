package core.application.movies.repositories.comment.jpa;

import core.application.movies.models.dto.response.CommentRespDTO;
import core.application.movies.models.entities.CommentEntity;
import core.application.movies.repositories.comment.CommentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Profile("!mybatis")
public class JpaCommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository jpaRepository;

    @Override
    public CommentEntity saveNewComment(String movieId, UUID userId, CommentEntity comment) {
        return jpaRepository.save(comment);
    }

    @Override
    public Optional<CommentEntity> findByCommentId(Long commentId) {
        return jpaRepository.findById(commentId);
    }

    @Override
    public Boolean existsByMovieIdAndUserId(String movieId, UUID userId) {
        return jpaRepository.existsByMovieIdAndUserId(movieId, userId);
    }

    @Override
    public List<CommentRespDTO> findByMovieId(String movieId, UUID userId, int page) {
        return List.of();
    }

    @Override
    public List<CommentRespDTO> findByMovieIdOnDateDescend(String movieId, UUID userId, int page) {
        return List.of();
    }

    @Override
    public List<CommentRespDTO> findByMovieIdOnLikeDescend(String movieId, UUID userId, int offset) {
        return List.of();
    }

    @Override
    public List<CommentRespDTO> findByMovieIdOnDislikeDescend(String movieId, UUID userId, int offset) {
        return List.of();
    }

    @Override
    public Page<CommentRespDTO> findByMovieIdOrderBy(String movieId, UUID userId, Pageable pageable) {
        return jpaRepository.findByMovieIdOrderBy(movieId, userId, pageable);
    }

    @Override
    public List<CommentEntity> selectAll() {
        return jpaRepository.findAll();
    }

    @Override
    public long countByMovieId(String movieId) {
        return 0;
    }

    @Override
    public void update(CommentEntity comment) {
        jpaRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        jpaRepository.deleteById(commentId);
    }
}
