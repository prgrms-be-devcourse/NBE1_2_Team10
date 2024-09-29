package core.application.movies.service;

import java.util.List;

import core.application.movies.constant.Genre;
import core.application.movies.constant.MovieSearch;
import core.application.movies.models.dto.MainPageMoviesRespDTO;
import core.application.movies.models.dto.MovieDetailRespDTO;
import core.application.movies.models.dto.MovieSimpleRespDTO;

public interface MovieService {

	/**
	 * 사용자가 영화 상세 페이지에서 필요로 하는 정보를 보내준다.
	 * @param movieId KMDB API의 DOCID
	 * @return 영화 상세 페이지에서 필요로하는 영화 정보
	 */
	public MovieDetailRespDTO getMovieDetailInfo(String movieId);
}
