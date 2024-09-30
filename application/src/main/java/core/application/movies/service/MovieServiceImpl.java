package core.application.movies.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import core.application.movies.constant.Genre;
import core.application.movies.constant.MovieSearch;
import core.application.movies.models.dto.MainPageMovieRespDTO;
import core.application.movies.models.dto.MainPageMoviesRespDTO;
import core.application.movies.models.dto.MovieDetailRespDTO;
import core.application.movies.models.dto.MovieSimpleRespDTO;
import core.application.movies.repositories.CachedMovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

	@Value("${kmdb.api.key}")
	private String apiKey;

	@Value("${kmdb.api.default.image}")
	private String defaultImgUrl;

	private final WebClient webClient;
	private final CachedMovieRepository movieRepository;

	@Override
	public MainPageMoviesRespDTO getMainPageMovieInfo() {
		List<MainPageMovieRespDTO> ratingOrder = movieRepository.selectOnAVGRatingDescend(10).stream()
			.map(MainPageMovieRespDTO::from)
			.toList();

		List<MainPageMovieRespDTO> dibOrder = movieRepository.selectOnDibOrderDescend(10).stream()
			.map(MainPageMovieRespDTO::from)
			.toList();

		List<MainPageMovieRespDTO> reviewOrder = movieRepository.selectOnReviewCountDescend(10).stream()
			.map(MainPageMovieRespDTO::from)
			.toList();

		return MainPageMoviesRespDTO.of(dibOrder, ratingOrder, reviewOrder);
	}

	@Override
	public List<MovieSimpleRespDTO> searchMovies(Integer page, MovieSearch sort, String query, Genre genre) {
		return List.of();
	}

	public MovieDetailRespDTO getMovieDetailInfo(String movieId) {

		String MovieId = movieId.substring(0, 1);
		String MovieSeq = movieId.substring(1);

		String response = webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/search_json2.jsp")
				.queryParam("ServiceKey", apiKey)
				.queryParam("detail", "Y")
				.queryParam("collection", "kmdb_new2")
				.queryParam("ratedYn", "Y")
				.queryParam("movieId", MovieId)
				.queryParam("movieSeq", MovieSeq)
				.queryParam("listCount", 10)
				.build())
			.retrieve()
			.bodyToMono(String.class)
			.block();

		return parseMovieDetail(new JSONObject(response));
	}

	private MovieDetailRespDTO parseMovieDetail(JSONObject jsonResponse) {

		MovieDetailRespDTO movieDetailRespDTO = new MovieDetailRespDTO();

		JSONObject resultObject = jsonResponse.getJSONArray("Data")
			.getJSONObject(0)
			.getJSONArray("Result")
			.getJSONObject(0);

		// 제목에서 !HS, !HE 제거 후 제목 저장
		String title = resultObject.optString("title", "").replaceAll("!HS", "").replaceAll("!HE", "").trim();
		movieDetailRespDTO.setTitle(title);

		// 포스트 링크 저장
		String imgUrl = resultObject.optString("posters", defaultImgUrl);
		if (!imgUrl.equals(defaultImgUrl)) {
			movieDetailRespDTO.setPosterUrl(imgUrl.split("\\|")[0]);
		} else {
			movieDetailRespDTO.setPosterUrl(imgUrl);
		}

		// 장르 저장
		movieDetailRespDTO.setGenre(resultObject.optString("genre", ""));

		// 대표 개봉일
		movieDetailRespDTO.setReleaseDate(resultObject.optString("repRlsDate", ""));

		// 줄거리 설정
		JSONArray plotArray = resultObject.optJSONObject("plots").optJSONArray("plot");
		for (int j = 0; j < plotArray.length(); j++) {
			JSONObject plot = plotArray.getJSONObject(j);
			if (plot.optString("plotLang").equals("한국어")) {
				movieDetailRespDTO.setOverview(plot.optString("plotText"));
			}
		}

		movieDetailRespDTO.setRunningTime(resultObject.optString("runtime", "상영시간"));

		// 배우 목록 설정 (최대 3명)
		JSONArray actorsArray = resultObject.optJSONObject("actors").optJSONArray("actor");
		List<String> actorsList = new ArrayList<>();
		if (actorsArray != null) {
			for (int k = 0; k < Math.min(actorsArray.length(), 3); k++) {
				String actorName = actorsArray.getJSONObject(k).optString("actorNm", "배우명");
				actorsList.add(actorName);
			}
		}
		movieDetailRespDTO.setActors(actorsList);

		JSONArray directorsArray = resultObject.optJSONObject("directors").optJSONArray("director");
		movieDetailRespDTO.setDirector(directorsArray.getJSONObject(0).optString("directorNm", "감독명"));

		return movieDetailRespDTO;
	}
}
