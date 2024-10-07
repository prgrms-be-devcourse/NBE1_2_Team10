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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/movies")
@Tag(name = "Movie", description = "영화 관련 API")
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;

	@Operation(summary = "메인 페이지의 영화 목록", description = "평점 높은 순, 찜 많은 순, 리뷰 많은 순으로 제공한다.")
	@ApiResponse(responseCode = "200",
		content=@Content(schema = @Schema(implementation = MainPageMoviesRespDTO.class))
	)
	@GetMapping("/list")
	public MainPageMoviesRespDTO getMainPageMovies() {
		return movieService.getMainPageMovieInfo();
	}

	@Operation(summary = "영화 상세 페이지", description = "영화의 상세 정보를 제공한다.")
	@ApiResponse(responseCode = "200",
		content=@Content(schema = @Schema(implementation = MovieDetailRespDTO.class))
	)
	@GetMapping("/{movieId}")
	public MovieDetailRespDTO ViewMovieDetails(@PathVariable("movieId") String movieId) {
		return movieService.getMovieDetailInfo(movieId);
	}

	@Operation(summary = "영화 통합 검색", description = "검색어를 사용해 영화를 검색한다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = MovieSearchRespDTO.class)))
	})
	@Parameters({
		@Parameter(name = "query", description = "검색어", example = "범죄도시"),
		@Parameter(name = "sortType", description = "정렬 타입", example = "latest"),
		@Parameter(name = "page", description = "page", example = "0")
	})
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

	@Operation(summary = "영화 장르 검색", description = "영화를 장르로 구분하여 볼 수 있다.")
	@ApiResponses({
		@ApiResponse(responseCode = "404", description = "존재하지 않는 장르 검색"),
		@ApiResponse(responseCode = "200",
			content = @Content(schema = @Schema(implementation = MovieSearchRespDTO.class)))
	})
	@Parameters({
		@Parameter(name = "genre", description = "장르", example = "action"),
		@Parameter(name = "page", description = "페이지", example = "0"),
		@Parameter(name = "sort", description = "정렬 타입", example = "latest")
	})
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
