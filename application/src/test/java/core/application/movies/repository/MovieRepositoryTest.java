package core.application.movies.repository;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.movie.CachedMovieRepository;

@SpringBootTest
@Transactional
public class MovieRepositoryTest {

	@Autowired
	CachedMovieRepository repository;
	@Autowired
	EntityManager em;

	@Test
	@DisplayName("영화를 데이터베이스에 저장한다.")
	public void save() {
		// GIVEN
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
			1L, 2L, 3L, 4L
		);

		// WHEN
		CachedMovieEntity movie = repository.saveNewMovie(movieEntity);

		// THEN
		CachedMovieEntity find = repository.findByMovieId(movie.getMovieId()).orElse(null);
		assertThat(find).isEqualTo(movie);
	}

	@Test
	@DisplayName("영화 정보를 수정한다.")
	@Transactional
	public void update() {
		// GIVEN
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
			1L, 2L, 3L, 4L
		);
		em.persist(movieEntity);

		// WHEN
		movieEntity.isCommentedWithRating(10);
		movieEntity.incrementReviewCount();
		em.flush();

		// THEN
		CachedMovieEntity find = repository.findByMovieId(movieEntity.getMovieId()).orElse(null);
		assertThat(movieEntity).isEqualTo(find);
	}

	@Test
	@DisplayName("찜 많은 순, 평점 높은 순, 리뷰 많은 순으로 영화를 제공한다.")
	public void order() {
		// GIVEN
		for (int i = 0; i < 10; i++) {
			CachedMovieEntity movieEntity = new CachedMovieEntity(
				"test" + i,
				"testTitle",
				"posterUrl",
				"액션",
				"2024-09-30",
				"줄거리",
				"122",
				"마동석, 김무열",
				"봉준호",
				(long)i, (long)(i), 10L, (long)(100 - 10 * i)
			);
			repository.saveNewMovie(movieEntity);
		}

		// WHEN
		List<CachedMovieEntity> rating = repository.selectOnAVGRatingDescend(5);
		List<CachedMovieEntity> dib = repository.selectOnDibOrderDescend(5);
		List<CachedMovieEntity> review = repository.selectOnDibOrderDescend(5);

		// THEN
		assertThat(rating).hasSize(5);
		assertThat(rating.get(0).getSumOfRating() / rating.get(0).getCommentCount()).isEqualTo(10);
		assertThat(rating.get(1).getSumOfRating() / rating.get(1).getCommentCount()).isEqualTo(9);
		assertThat(rating.get(2).getSumOfRating() / rating.get(2).getCommentCount()).isEqualTo(8);
		assertThat(rating.get(3).getSumOfRating() / rating.get(3).getCommentCount()).isEqualTo(7);
		assertThat(rating.get(4).getSumOfRating() / rating.get(4).getCommentCount()).isEqualTo(6);

		assertThat(dib).hasSize(5);
		assertThat(dib.get(0).getDibCount()).isEqualTo(9);
		assertThat(dib.get(1).getDibCount()).isEqualTo(8);
		assertThat(dib.get(2).getDibCount()).isEqualTo(7);
		assertThat(dib.get(3).getDibCount()).isEqualTo(6);
		assertThat(dib.get(4).getDibCount()).isEqualTo(5);

		assertThat(review).hasSize(5);
		assertThat(review.get(0).getReviewCount()).isEqualTo(9);
		assertThat(review.get(1).getReviewCount()).isEqualTo(8);
		assertThat(review.get(2).getReviewCount()).isEqualTo(7);
		assertThat(review.get(3).getReviewCount()).isEqualTo(6);
		assertThat(review.get(4).getReviewCount()).isEqualTo(5);
	}
}
