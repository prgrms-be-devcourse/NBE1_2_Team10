//package core.application.movies.Controller;
//
//import core.application.movies.models.dto.MovieDetailRespDTO;
//import core.application.movies.service.MovieService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//
//
///**
// * KMDB API 조회 및 조회 후 데이터 가공에 대한 로직은 MovieService에서 구현한다.
// * 추 후 KMDB API 관련 Service 분리가 필요하면, 분리한다.
// */
//
//@Slf4j
//@RestController
//@RequestMapping("/movies")
//@RequiredArgsConstructor
//public class MovieController {
//
//	private final MovieService movieService;
//
//	/**
//	 * 영화 상세 조회
//	 * /movies/{movieId}
//	 */
//	@GetMapping("/{movieId}")
//	public MovieDetailRespDTO ViewMovieDetails(@PathVariable("movieId") String movieId) {
//		return movieService.getMovieDetailInfo(movieId);
//	}
//
//}
