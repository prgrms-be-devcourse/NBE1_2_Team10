package core.application.movies.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.application.movies.constant.Genre;
import core.application.movies.constant.MovieSearch;
import core.application.movies.exception.WrongAccessException;
import core.application.movies.models.dto.MainPageMoviesRespDTO;
import core.application.movies.models.dto.MovieDetailRespDTO;
import core.application.movies.models.dto.MovieSearchRespDTO;
import core.application.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;

	@GetMapping("/list")
	public MainPageMoviesRespDTO getMainPageMovies() {
		return movieService.getMainPageMovieInfo();
	}

	@GetMapping("/{movieId}")
	public MovieDetailRespDTO ViewMovieDetails(@PathVariable("movieId") String movieId) {
		return movieService.getMovieDetailInfo(movieId);
	}

	@GetMapping("/search")
	public List<MovieSearchRespDTO> search(@RequestParam(defaultValue = "") String query,
		@RequestParam(defaultValue = "latest") String sortType,
		@RequestParam(defaultValue = "0") Integer page) {
		// 정렬 조건이 없다면 제작년도 순으로 제공
		if (MovieSearch.isNotValid(sortType)) {
			return movieService.searchMovies(page, MovieSearch.LATEST, query);
		}
		return movieService.searchMovies(page, MovieSearch.valueOf(sortType), query);
	}

	@GetMapping("/genre/{genre}")
	public List<MovieSearchRespDTO> searchGenre(@PathVariable("genre") String genre,
		@RequestParam(defaultValue = "0") Integer page,
		@RequestParam(defaultValue = "latest") String sort) {
		// 잘못된 장르 검색 시, 아예 페이지 제공 X
		if (Genre.isNotValid(genre)) {
			throw new WrongAccessException("존재하지 않는 페이지입니다.");
		}
		// 유효하지 않은 정렬 조건이라면, 제작년도순으로 제공
		if (MovieSearch.isNotValid(sort)) {
			return movieService.getMoviesWithGenre(page, Genre.valueOf(genre.toUpperCase()), MovieSearch.LATEST);
		}
		return movieService.getMoviesWithGenre(page, Genre.valueOf(genre.toUpperCase()), MovieSearch.valueOf(sort));
	}
}
