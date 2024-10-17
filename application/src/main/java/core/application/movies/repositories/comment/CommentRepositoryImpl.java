package core.application.movies.repositories.comment;

import core.application.movies.models.dto.response.CommentRespDTO;
import core.application.movies.models.entities.CommentEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CommentRepositoryImpl implements CommentRepository {

    private final JpaCommentRepository jpaCommentRepository;

    @Override
    public CommentEntity saveNewComment(String movieId, UUID userId, CommentEntity comment) {
        return jpaCommentRepository.save(comment);
    }

    @Override
    public Optional<CommentEntity> findByCommentId(Long commentId) {
        return jpaCommentRepository.findById(commentId);
    }

    @Override
    public Boolean existsByMovieIdAndUserId(String movieId, UUID userId) {
        return jpaCommentRepository.existsByMovie_MovieIdAndUser_UserId(movieId, userId);
    }

    @Override
    public Page<CommentRespDTO> findByMovieId(String movieId, UUID userId, Pageable pageable) {
        return jpaCommentRepository.findByMovieId(movieId, userId, pageable);
    }

    @Override
    public Page<CommentRespDTO> findByMovieIdOnDateDescend(String movieId, UUID userId, Pageable pageable) {
        return jpaCommentRepository.findByMovieIdOrderByCreatedAt(movieId, userId, pageable);
    }

    @Override
    public Page<CommentRespDTO> findByMovieIdOnLikeDescend(String movieId, UUID userId, Pageable pageable) {
        return jpaCommentRepository.findByMovieIdOrderByLikeDesc(movieId, userId, pageable);
    }

    @Override
    public Page<CommentRespDTO> findByMovieIdOnDislikeDescend(String movieId, UUID userId, Pageable pageable) {
        return jpaCommentRepository.findByMovieIdOrderByDislikeDesc(movieId, userId, pageable);
    }

    @Override
    public List<CommentEntity> selectAll() {
        return jpaCommentRepository.findAll();
    }

    @Override
    public void update(CommentEntity comment) {
    }

    @Override
    public void deleteComment(Long commentId) {
        jpaCommentRepository.deleteById(commentId);
    }
}
