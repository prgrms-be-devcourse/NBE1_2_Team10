package core.application.movies.repository;

import static org.assertj.core.api.Assertions.assertThat;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.models.entities.CommentEntity;
import core.application.movies.models.entities.CommentLike;
import core.application.movies.repositories.comment.CommentRepository;
import core.application.movies.repositories.comment.JpaCommentLikeRepository;
import core.application.movies.repositories.movie.CachedMovieRepository;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CommentLikeRepositoryTest {
    @Autowired
    private JpaCommentLikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CachedMovieRepository movieRepository;
    @Autowired
    private CommentRepository commentRepository;
    private UserEntity user;
    private CachedMovieEntity movie;
    private CommentEntity comment;

    @BeforeEach
    public void setUp() {
        user = UserEntity.builder()
                .userEmail("testEmail")
                .userPw("test")
                .role(UserRole.USER)
                .alias("nickname")
                .phoneNum("phone")
                .userName("test")
                .build();
        userRepository.saveNewUser(user);
        user = userRepository.findByUserEmail("testEmail").get();

        CachedMovieEntity movieEntity = new CachedMovieEntity(
                "test",
                "testTitle",
                "posterUrl",
                "액션",
                "2024-09-30",
                "줄거리",
                "122",
                "마동석, 김무열",
                "봉준호",
                1L, 1L, 10L, 10L
        );
        movieRepository.saveNewMovie(movieEntity);
        movie = movieRepository.findByMovieId(movieEntity.getMovieId()).orElseThrow(() -> new RuntimeException("저장안됨"));

        CommentEntity save = CommentEntity.builder()
                .content("내용")
                .like(1)
                .movie(movie)
                .dislike(1)
                .rating(10)
                .movie(movieEntity)
                .user(user)
                .build();
        comment = commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), save);
    }

    @Test
    @DisplayName("좋아요를 눌렀을 때 새로운 좋아요 데이터를 생성한다.")
    public void likeTest() {
        // GIVEN

        // WHEN
        CommentLike save = likeRepository.save(CommentLike.of(comment, user.getUserId()));

        // THEN
        CommentLike find = likeRepository.findById(save.getCommentLikeId()).orElseThrow();
        assertThat(find).isEqualTo(save);
    }

    @Test
    @DisplayName("좋아요를 삭제한다.")
    public void deleteTest() {
        // GIVEN
        CommentLike save = likeRepository.save(CommentLike.of(comment, user.getUserId()));

        // WHEN
        likeRepository.deleteByCommentAndUserId(comment, user.getUserId());

        // THEN
        assertThat(likeRepository.findById(save.getCommentLikeId())).isEmpty();
    }
}
