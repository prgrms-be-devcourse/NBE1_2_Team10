package core.application.movies.service;

import java.util.List;

import core.application.movies.models.dto.MainPageMovieResDTO;
import core.application.movies.models.dto.MainPageMoviesResDTO;
import core.application.movies.models.dto.MovieDetailResDTO;
import core.application.movies.models.dto.MovieSimpleResDTO;

public interface MovieServiceInterface {

	// 추후 기능 분리 시 사용될 예정
	public List<MainPageMovieResDTO> getMoviesOrderByDib();

	public List<MainPageMovieResDTO> getMoviesOrderByReview();

	/**
	 * KMDB를 통해 최신순 영화를 조회한 후, 찜 개수 확인을 위해 영화별로 캐시 영화 테이블에 조회 필요
	 */
	public List<MainPageMovieResDTO> getMoviesOrderByDate();

	/**
	 * 찜 많은 순 / 리뷰 많은 순 / 최신순
	 *
	 * 찜 많은 순
	 * 10개
	 *
	 * 리뷰 많은 순
	 * 10개
	 *
	 * 최신순
	 * 10개
	 */

	public MainPageMoviesResDTO getMainPageMovieInfo();

	public List<MovieSimpleResDTO> searchMovies(Integer page, String sort, String query, String category);

	/**
	 * 영화 상세정보의 찜 개수, 평점을 가져오기 위해서는 캐시 영화 테이블에 접근 필요
	 * @param movieId
	 * @param movieSeq
	 * @return
	 */
	public MovieDetailResDTO getMovieDetailInfo(String movieId, String movieSeq);
}
