package core.application.movies.service;

import core.application.movies.constant.Genre;
import core.application.movies.constant.MovieSearch;
import core.application.movies.models.dto.MainPageMoviesRespDTO;
import core.application.movies.models.dto.MovieDetailRespDTO;
import core.application.movies.models.dto.MovieSimpleRespDTO;
import core.application.movies.models.entities.CachedMovieEntity;
import core.application.movies.repositories.CachedMovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

	@Value("${kmdb.api.key}")
	private String apiKey;

	private final WebClient webClient;

	private final CachedMovieRepository movieRepository;

	public MainPageMoviesRespDTO getMainPageMovieInfo(){
		return null;
	}

	public List<MovieSimpleRespDTO> searchMovies(Integer page, MovieSearch sort, String query, Genre genre){
		return null;
	}

	/*
	 * 구현 순서
	 * 1. repositories로 DB에 접근하여 DB에서 데이터 가져오기
	 * 2. 데이터 있을 경우 return으로 (Entity -> DTO 변환)데이터 반환
	 * 3. 비어 있을 경우 API에 접근한 뒤 Entity에 저장 후 DB에 저장
	 * 4. Entity 값을 DTO로 변환 후 return
	 */
	public MovieDetailRespDTO getMovieDetailInfo(String movieId) {

		AtomicBoolean check = new AtomicBoolean(false);

		MovieDetailRespDTO movieDetailRespDTO = new MovieDetailRespDTO();

		// 1. repositories로 DB에 접근하여 DB에서 데이터 가져오기
		Optional<CachedMovieEntity> cachedMovieEntity = movieRepository.findByMovieId(movieId);

		// 2. 데이터 있을 경우 return으로 데이터 반환
		cachedMovieEntity.ifPresent(movieEntity -> {
			movieDetailRespDTO.toDTO(movieEntity);
			check.set(true);
		});

		// 데이터가 있을 경우에 return으로 함수 종료
		if(check.get()){
			return movieDetailRespDTO;
		}


		// 3. 비어 있을 경우 API에 접근한 뒤 Entity에 저장 후 DB에 저장
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
						.queryParam("listCount", 5)
						.build())
				.retrieve()
				.bodyToMono(String.class)
				.block();

		return parseMovieDetail(new JSONObject(response));
	}

	private MovieDetailRespDTO parseMovieDetail(JSONObject jsonResponse) {

		JSONObject resultObject = jsonResponse.getJSONArray("Data")
				.getJSONObject(0)
				.getJSONArray("Result")
				.getJSONObject(0);

		//MovieId 저장
		String MovieID = exception(resultObject.optString("DOCID"));

		// 제목에서 !HS, !HE 제거 후 제목 저장
		String title = exception(resultObject.optString("title").replaceAll("!HS", "").replaceAll("!HE", "").trim());

		// 포스터 url 저장
		String imgUrl = handleString(resultObject.optString("posters"));

		String resultimgUrl = imgUrl.split("\\|")[0];

		// 장르 저장
		String genre = exception(resultObject.optString("genre"));

		// 대표 개봉일
		String ReleaseDate = handleString(resultObject.optString("repRlsDate", ""));

		// 줄거리 설정
		JSONArray plotArray = resultObject.optJSONObject("plots").optJSONArray("plot");
		String resultPlot = "";
		for (int j = 0; j < plotArray.length(); j++) {
			JSONObject plot = plotArray.getJSONObject(j);
			if (plot.optString("plotLang").equals("한국어")) {
				resultPlot = handleString(plot.optString("plotText"));
			}else{
				resultPlot = "알 수 없음"; // 한국어 줄거리 외 알 수 없음 처리
			}
		}

		// 상영시간
		String runtime = handleString(resultObject.optString("runtime"));

		// 배우 목록 설정 (최대 5명)
		JSONArray actorsArray = resultObject.optJSONObject("actors").optJSONArray("actor");
		String actorName = "";
		for(int k=0; k<Math.min(actorsArray.length(), 5); k++){
			actorName = actorName + ", " + handleString(actorsArray.getJSONObject(k).optString("actorNm"));
			if(actorName.equals(", 알 수 없음")) break;
		}
		actorName = actorName.substring(2);

		JSONArray directorsArray = resultObject.optJSONObject("directors").optJSONArray("director");
		String resultDirector = handleString(directorsArray.getJSONObject(0).optString("directorNm"));

		CachedMovieEntity cachedMovieEntity = new CachedMovieEntity(MovieID, title, resultimgUrl, genre, ReleaseDate, resultPlot, runtime, actorName, resultDirector, 0L, 0L, 0L, 0L);

		// 3. 비어 있을 경우 API에 접근한 뒤 Entity에 저장 후 DB에 저장
		movieRepository.saveNewMovie(cachedMovieEntity);

		// 4. Entity 값을 DTO로 변환 후 클라이언트에게 return
		MovieDetailRespDTO movieDetailRespDTO = new MovieDetailRespDTO();

		return movieDetailRespDTO.toDTO(cachedMovieEntity);
	}

	private String exception(String str) {
		return Optional.ofNullable(str)
				.filter(val -> !val.isEmpty())  // 빈 문자열이 아닐 때만 처리
				.orElseThrow(() -> new IllegalArgumentException("잘못된 자료에 대한 요청입니다."));
	}

	public String handleString(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "알 수 없음";
		}
		return input;
	}
}
