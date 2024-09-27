package core.application.movies.service;

import java.util.List;

import core.application.movies.constant.Genre;
import core.application.movies.constant.MovieSearch;
import core.application.movies.models.dto.MainPageMoviesRespDTO;
import core.application.movies.models.dto.MovieDetailRespDTO;
import core.application.movies.models.dto.MovieSimpleRespDTO;

public interface MovieService {

	/**
	 * 메인 페이지에서 찜 많은 순 영화 10개, 평점 높은 순 영화 10개, 최신순 영화 10개의 정보를 보여준다.
	 * @return 찜 많은 순, 평점 높은 순, 최신순으로 정렬된 영화 정보
	 */
	public MainPageMoviesRespDTO getMainPageMovieInfo();

	/**
	 * 사용자는 장르, 검색어, 정렬 조건을 이용할 수 있다. <br>
	 * 정렬 조건과 검색어는 필수이지만, 장르는 선택적으로 사용한다.
	 *
	 * @param page 검색 페이지 목록
	 * @param sort 정렬 조건
	 * @param query 검색어
	 * @param genre 영화 장르
	 * @return 검색한 영화 정보
	 */
	public List<MovieSimpleRespDTO> searchMovies(Integer page, MovieSearch sort, String query, Genre genre);

	/**
	 * 사용자가 영화 상세 페이지에서 필요로 하는 정보를 보내준다.
	 * @param movieId KMDB API의 DOCID
	 * @return 영화 상세 페이지에서 필요로하는 영화 정보
	 */
	public MovieDetailRespDTO getMovieDetailInfo(String movieId);
}
