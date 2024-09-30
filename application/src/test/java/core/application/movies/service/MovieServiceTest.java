package core.application.movies.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import core.application.movies.constant.Genre;
import core.application.movies.models.dto.MainPageMovieRespDTO;
import core.application.movies.models.dto.MainPageMoviesRespDTO;
import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.CachedMovieRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@Slf4j
public class MovieServiceTest {
	@Autowired
	private MovieService movieService;
	@Autowired
	private CachedMovieRepository repository;

	@BeforeEach
	public void initTestData() {
		for (int i = 0; i < 10; i++) {
			CachedMovieEntity movieEntity = new CachedMovieEntity(
				String.valueOf(i),
				String.valueOf(i),
				"posterUrl",
				Genre.ACTION,
				"2024-09-30",
				"줄거리",
				"122",
				"마동석, 김무열",
				"봉준호",
				(long)i, (long)(i), 10L, (long)(100 - 10 * i)
			);
			repository.saveNewMovie(movieEntity);
		}
	}

	@Test
	@DisplayName("메인 페이지의 영화 목록을 보여준다.")
	public void getMainMovies() {
		// GIVEN
		MainPageMoviesRespDTO mainMovies = movieService.getMainPageMovieInfo();

		// WHEN
		List<MainPageMovieRespDTO> dib = mainMovies.getDibMovieList();
		List<MainPageMovieRespDTO> rating = mainMovies.getRatingMovieList();
		List<MainPageMovieRespDTO> review = mainMovies.getReviewMovieList();

		// THEN
		for (int i = 0; i < 10; i++) {
			MainPageMovieRespDTO movie = dib.get(i);
			assertThat(movie.getTitle()).isEqualTo(String.valueOf(9 - i));
		}
		for (int i = 0; i < 10; i++) {
			MainPageMovieRespDTO movie = rating.get(i);
			assertThat(movie.getMovieId()).isEqualTo(String.valueOf(i));
		}

		for (int i = 0; i < 10; i++) {
			MainPageMovieRespDTO movie = review.get(i);
			assertThat(movie.getMovieId()).isEqualTo(String.valueOf(9 - i));
		}
	}
}
