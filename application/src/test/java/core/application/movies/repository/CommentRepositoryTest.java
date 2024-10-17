package core.application.movies.repository;

import static org.assertj.core.api.Assertions.*;

import core.application.movies.models.entities.CommentLike;
import core.application.movies.repositories.comment.JpaCommentLikeRepository;
import core.application.movies.repositories.movie.CachedMovieRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import core.application.movies.models.dto.response.CommentRespDTO;
import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.models.entities.CommentEntity;
import core.application.movies.repositories.comment.CommentRepository;
import core.application.users.models.entities.UserEntity;
import core.application.users.models.entities.UserRole;
import core.application.users.repositories.UserRepository;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {

	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CachedMovieRepository movieRepository;
	@Autowired
	private EntityManager em;
	private CommentEntity comment;
	private UserEntity user;
	private CachedMovieEntity movie;
	@Autowired
	private JpaCommentLikeRepository commentLikeRepository;

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

		comment = CommentEntity.builder()
			.content("내용")
			.like(1)
			.movie(movie)
			.dislike(1)
			.rating(10)
			.movie(movieEntity)
			.user(user)
			.build();
	}

	@Test
	@DisplayName("한줄평 리뷰를 저장한다.")
	public void save() {
		// GIVEN

		// WHEN
		CommentEntity save = commentRepository.saveNewComment("test", user.getUserId(), comment);

		// THEN
		assertThat(save).isEqualTo(commentRepository.findByCommentId(save.getCommentId()).orElse(null));
	}

	@Test
	@DisplayName("로그인하지 않은 사용자가 영화 ID로 조회한다.")
	public void findByMovieId() {
		// GIVEN
		CommentEntity save1 = commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(),
			comment);
		CommentEntity comment2 = CommentEntity.builder()
			.content("내용2")
			.like(1)
			.dislike(1)
			.rating(10)
			.movie(movie)
			.user(user)
			.build();
		CommentEntity save2 = commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(),
			comment2);

		// WHEN
		Page<CommentRespDTO> finds = commentRepository.findByMovieId(movie.getMovieId(), null, PageRequest.of(1, 10));

		// THEN
//		assertThat(finds.getSize()).isEqualTo(2);

		for (CommentRespDTO find : finds) {
			assertThat(find.getMovieId()).isEqualTo("test");
		}
	}

	@Test
	@DisplayName("로그인한 사용자가 한줄평 조회 시, 좋아요한 한줄평을 구분한다.")
	public void findByMovieIdWithUser() {
		// GIVEN
		CommentEntity save1 = commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(),
			comment);
		commentLikeRepository.save(CommentLike.of(comment, user));

		// WHEN
		CommentEntity comment = commentRepository.findByCommentId(save1.getCommentId())
				.orElseThrow(() -> new RuntimeException("ERROR"));
		System.out.println(comment.getMovie().getMovieId());
		Page<CommentRespDTO> finds = commentRepository.findByMovieId(movie.getMovieId(), user.getUserId(), PageRequest.of(0, 10));
		System.out.println("movieId : " + movie.getMovieId());

		// THEN
		assertThat(finds.getContent().get(0).getIsLiked()).isTrue();
	}

	@Test
	@DisplayName("특정 영화의 한줄평을 시간순으로 조회한다.")
	public void findByMovieIdOnDateDescend() {
		// GIVEN
		CommentEntity comment2 = CommentEntity.builder()
			.content("내용2")
			.like(1)
			.dislike(1)
			.rating(10)
			.movie(movie)
			.user(user)
			.build();
		commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), comment);
		commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), comment2);

		// WHEN
		Page<CommentRespDTO> finds = commentRepository.findByMovieIdOnDateDescend("test", user.getUserId(), PageRequest.of(0, 10));

		// THEN
		Instant later = finds.getContent().get(0).getCreatedAt();
		for (CommentRespDTO find : finds) {
			System.out.println("later : " + later + " find : " + find.getCreatedAt());
			assertThat(later).isBeforeOrEqualTo(find.getCreatedAt());
			later = find.getCreatedAt();
		}
	}

	@Test
	@DisplayName("특정 영화 한줄평을 좋아요 순으로 검색한다.")
	public void testLikeOrder() {
		// GIVEN
		CommentEntity comment2 = CommentEntity.builder()
			.content("내용2")
			.like(100)
			.dislike(1)
			.rating(10)
			.movie(movie)
			.user(user)
			.build();
		commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), comment);
		commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), comment2);

		// WHEN
		Page<CommentRespDTO> finds = commentRepository.findByMovieIdOnLikeDescend("test", user.getUserId(), PageRequest.of(0, 10));

		// THEN
		int more = finds.getContent().get(0).getLike();
		for (CommentRespDTO find : finds) {
			assertThat(more).isGreaterThanOrEqualTo(find.getLike());
			more = find.getLike();
		}
	}

	@Test
	@DisplayName("특정 영화 한줄평을 싫어요 순으로 검색한다.")
	public void testDislikeOrder() {
		// GIVEN
		CommentEntity comment2 = CommentEntity.builder()
			.content("내용2")
			.like(1)
			.dislike(100)
			.rating(10)
			.movie(movie)
			.user(user)
			.build();
		commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), comment);
		commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), comment2);

		// WHEN
		Page<CommentRespDTO> finds = commentRepository.findByMovieIdOnDislikeDescend("test", user.getUserId(), PageRequest.of(0, 10));

		// THEN
		int more = finds.getContent().get(0).getDislike();
		for (CommentRespDTO find : finds) {
			assertThat(more).isGreaterThanOrEqualTo(find.getDislike());
			more = find.getDislike();
		}
	}

	@Test
	@DisplayName("한줄평을 삭제한다.")
	public void delete() {
		// GIVEN
		commentRepository.saveNewComment(movie.getMovieId(), user.getUserId(), comment);

		// WHEN
		commentRepository.deleteComment(comment.getCommentId());

		// THEN
		Optional<CommentEntity> find = commentRepository.findByCommentId(comment.getCommentId());
		assertThat(find).isEmpty();
	}
}
