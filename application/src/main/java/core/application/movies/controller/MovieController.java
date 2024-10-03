package core.application.movies.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import core.application.movies.constant.MovieSearch;
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
	public List<MovieSearchRespDTO> search(@RequestParam String query, @RequestParam String sortType,
		@RequestParam Integer page) {
		if (MovieSearch.isNotValid(sortType)) {
			return movieService.searchMovies(page, MovieSearch.RANK, query);
		}
		return movieService.searchMovies(page, MovieSearch.valueOf(sortType), query);
	}
}
