package core.application.movies.service;

import java.util.List;

import core.application.movies.constant.Genre;
import core.application.movies.constant.MovieSearch;
import core.application.movies.models.dto.MainPageMoviesRespDTO;
import core.application.movies.models.dto.MovieDetailRespDTO;
import core.application.movies.models.dto.MovieSearchRespDTO;

public interface MovieService {

	/**
	 * 메인 페이지에서 찜 많은 순 영화 10개, 평점 높은 순 영화 10개, 최신순 영화 10개의 정보를 보여준다.
	 * @return 찜 많은 순, 평점 높은 순, 최신순으로 정렬된 영화 정보
	 */
	public MainPageMoviesRespDTO getMainPageMovieInfo();

	/**
	 * 통합 검색은 검색 후 정확도순, 최신순을 제공한다.
	 *
	 * @param page 검색 페이지 목록
	 * @param sort 정렬 조건
	 * @param query 검색어
	 * @return 검색한 영화 정보
	 */
	public List<MovieSearchRespDTO> searchMovies(Integer page, MovieSearch sort, String query);

	/**
	 * 카테고리에 해당하는 영화를 제공한다.
	 *
	 * @param page 페이지
	 * @param genre 원하는 장르
	 * @param sort 정렬 조건
	 * @return 해당 카테고리의 영화
	 */
	public List<MovieSearchRespDTO> getMoviesWithGenre(Integer page, Genre genre, MovieSearch sort);

	/**
	 * 사용자가 영화 상세 페이지에서 필요로 하는 정보를 보내준다.
	 * @param movieId KMDB API의 DOCID
	 * @return 영화 상세 페이지에서 필요로하는 영화 정보
	 */
	public MovieDetailRespDTO getMovieDetailInfo(String movieId);
}
