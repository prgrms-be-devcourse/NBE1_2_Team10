package core.application.movies.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import core.application.movies.constant.Genre;
import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.models.entities.CommentEntity;
import core.application.movies.repositories.CachedMovieRepository;
import core.application.movies.repositories.CommentRepository;
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
	private CommentEntity comment;

	@BeforeEach
	public void setUp() {
		UserEntity user = UserEntity.builder()
			.userEmail("testEmail")
			.userPw("test")
			.role(UserRole.USER)
			.alias("nickname")
			.phoneNum("phone")
			.userName("test")
			.build();
		userRepository.saveNewUser(user);

		CachedMovieEntity movieEntity = new CachedMovieEntity(
			"test",
			"testTitle",
			"posterUrl",
			Genre.ACTION,
			"2024-09-30",
			"줄거리",
			"122",
			"마동석, 김무열",
			"봉준호",
			1L, 1L, 10L, 10L
		);
		movieRepository.saveNewMovie(movieEntity);

		comment = CommentEntity.builder()
			.content("내용")
			.like(1)
			.dislike(1)
			.rating(10)
			.movieId("test")
			.userId(userRepository.findByUserEmail("testEmail").get().getUserId())
			.build();
	}

	@Test
	@DisplayName("한줄평 리뷰를 저장한다.")
	public void save() {
		// GIVEN

		// WHEN
		CommentEntity save = commentRepository.saveNewComment("test", comment.getUserId(), comment);

		// THEN
		assertThat(save).isNotNull();
		assertThat(save.getContent()).isEqualTo(comment.getContent());
		assertThat(save.getLike()).isEqualTo(comment.getLike());
		assertThat(save.getDislike()).isEqualTo(comment.getDislike());
		assertThat(save.getRating()).isEqualTo(comment.getRating());
		assertThat(save.getMovieId()).isEqualTo(comment.getMovieId());
		assertThat(save.getUserId()).isEqualTo(comment.getUserId());
	}

	@Test
	@DisplayName("영화 ID로 조회한다.")
	public void findByMovieId() {
		// GIVEN
		CommentEntity save1 = commentRepository.saveNewComment(comment.getMovieId(), comment.getUserId(),
			comment);
		CommentEntity comment2 = CommentEntity.builder()
			.content("내용2")
			.like(1)
			.dislike(1)
			.rating(10)
			.movieId("test")
			.userId(userRepository.findByUserEmail("testEmail").get().getUserId())
			.build();
		CommentEntity save2 = commentRepository.saveNewComment(comment2.getMovieId(), comment2.getUserId(),
			comment2);

		// WHEN
		List<CommentEntity> find = commentRepository.findByMovieId(comment.getMovieId());

		// THEN
		assertThat(find.size()).isEqualTo(2);
		for (CommentEntity commentEntity : find) {
			assertThat(commentEntity.getMovieId()).isEqualTo("test");
		}
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
			.movieId("test")
			.userId(userRepository.findByUserEmail("testEmail").get().getUserId())
			.build();

		// WHEN
		List<CommentEntity> finds = commentRepository.findByMovieIdOnDateDescend("test");

		// THEN
		Instant later = finds.get(0).getCreatedAt();
		for (CommentEntity find : finds) {
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
			.movieId("test")
			.userId(userRepository.findByUserEmail("testEmail").get().getUserId())
			.build();
		commentRepository.saveNewComment(comment.getMovieId(), comment.getUserId(), comment);
		commentRepository.saveNewComment(comment2.getMovieId(), comment2.getUserId(), comment2);

		// WHEN
		List<CommentEntity> finds = commentRepository.findByMovieIdOnLikeDescend("test");

		// THEN
		int more = finds.get(0).getLike();
		for (CommentEntity find : finds) {
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
			.movieId("test")
			.userId(userRepository.findByUserEmail("testEmail").get().getUserId())
			.build();
		commentRepository.saveNewComment(comment.getMovieId(), comment.getUserId(), comment);
		commentRepository.saveNewComment(comment2.getMovieId(), comment2.getUserId(), comment2);

		// WHEN
		List<CommentEntity> finds = commentRepository.findByMovieIdOnDislikeDescend("test");

		// THEN
		int more = finds.get(0).getDislike();
		for (CommentEntity find : finds) {
			assertThat(more).isGreaterThanOrEqualTo(find.getDislike());
			more = find.getDislike();
		}
	}

	@Test
	@DisplayName("한줄평을 삭제한다.")
	public void delete() {
		// GIVEN
		commentRepository.saveNewComment(comment.getMovieId(), comment.getUserId(), comment);

		// WHEN
		commentRepository.deleteComment(comment.getCommentId());

		// THEN
		Optional<CommentEntity> find = commentRepository.findByCommentId(comment.getCommentId());
		assertThat(find).isEmpty();
	}
}
