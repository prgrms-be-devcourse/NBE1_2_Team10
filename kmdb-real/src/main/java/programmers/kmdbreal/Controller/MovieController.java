package programmers.kmdbreal.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.kmdbreal.dto.MovieDetailDTO;
import programmers.kmdbreal.dto.MovieSimpleDTO;
import programmers.kmdbreal.service.MovieService;


/**
 * KMDB API 조회 및 조회 후 데이터 가공에 대한 로직은 MovieService에서 구현한다.
 * 추 후 KMDB API 관련 Service 분리가 필요하면, 분리한다.
 */

@Slf4j
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

	private final MovieService movieService;

	/**
	 * 영화 목록 조회
	 * /movies/list
	 * CacheMovie 서비스 필요
	 * CacheMovieService를 통해 영화 데이터를 불러와야 한다.
	 * 찜 많은 순, 리뷰 많은 순은
	//  */
	// @GetMapping("/list")
	// public List<> searchMovieList() {
	// }

	/**
	 * 영화 검색
	 * /movies/search/term={}&sort={}
	 */
	@GetMapping("/search")
	public List<MovieSimpleDTO> searchMovie(@RequestParam("term")String term, @RequestParam("sort") String sort, @RequestParam("page") Integer page) {
		return movieService.searchMovies(page, sort, term);
	}

	/**
	 * 영화 상세 조회
	 * /movies/{movieId}
	 */
	@GetMapping("/{movieId}/{movieSeq}")
	public MovieDetailDTO ViewMovieDetails(@PathVariable("movieId") String movieId, @PathVariable("movieSeq") String movieSeq) {
		return movieService.getMovieDetailInfo(movieId, movieSeq);
	}

}
