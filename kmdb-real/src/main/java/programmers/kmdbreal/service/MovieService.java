package programmers.kmdbreal.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import programmers.kmdbreal.dto.MovieDetailDTO;
import programmers.kmdbreal.dto.MovieSimpleDTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class MovieService {

	@Value("${kmdb.api.key}")
	private String apiKey;

	@Value("${kmdb.api.default.image}")
	private String defaultImgUrl;

	private final WebClient webClient;

	/**
	 * 10개 조회 시, KMDB API 응답이 너무 길기 때문에 오류 발생
	 * => 5개씩 잘라서 2번 조회
	 * 비동기식 처리가 나은 방향인지 확인
	 * @param page 현재 페이지
	 * @param sort 정렬 조건
	 * @param query 검색 내용
	 * @return 영화 제목, 영화 포스터 URL, 개봉일을 담은 영화 정보들을 반환한다.
	 */
	public List<MovieSimpleDTO> searchMovies(Integer page, String sort, String query) {
		// 앞의 5개의 결과 조회
		String first = webClient.get()
			.uri(uriBuilder -> {
				UriBuilder builder = uriBuilder
					.path("/search_json2.jsp")
					.queryParam("ServiceKey", apiKey)
					.queryParam("detail", "Y")
					.queryParam("query", query)
					.queryParam("collection", "kmdb_new2")
					.queryParam("startCount", 10 * page)
					.queryParam("ratedYn", "Y")
					.queryParam("listCount", 5);

				return builder.build();
			})
			.retrieve()
			.bodyToMono(String.class)
			.block();

		List<MovieSimpleDTO> movieSimpleDTOS = parseSimpleMovie(new JSONObject(first));

		// 뒤의 5개의 결과 조회
		String second = webClient.get()
			.uri(uriBuilder -> {
				UriBuilder builder = uriBuilder
					.path("/search_json2.jsp")
					.queryParam("ServiceKey", apiKey)
					.queryParam("detail", "Y")
					.queryParam("query", query)
					.queryParam("collection", "kmdb_new2")
					.queryParam("startCount", 10 * page + 5)
					.queryParam("ratedYn", "Y")
					.queryParam("listCount", 5);

				return builder.build();
			})
			.retrieve()
			.bodyToMono(String.class)
			.block();

		movieSimpleDTOS.addAll(parseSimpleMovie(new JSONObject(second)));
		return movieSimpleDTOS;
	}

	private List<MovieSimpleDTO> parseSimpleMovie(JSONObject jsonResponse) {
		List<MovieSimpleDTO> movieDTOList = new ArrayList<>();

		JSONArray resultArray = jsonResponse.getJSONArray("Data")
			.getJSONObject(0)
			.getJSONArray("Result");


		// 각 영화별 탐색 후 파싱
		for (int j = 0; j < resultArray.length(); j++) {
			JSONObject movieObject = resultArray.getJSONObject(j);

			// MovieDTO 객체 생성 및 값 설정
			MovieSimpleDTO movieSimpleDTO = new MovieSimpleDTO();

			// 제목에서 !HS, !HE 제거
			String title = movieObject.optString("title", "").replaceAll("!HS", "").replaceAll("!HE", "").trim();
			movieSimpleDTO.setTitle(title);

			// 포스터 url 설정
			String imgUrl = movieObject.optString("posters", defaultImgUrl);
			if (!imgUrl.equals(defaultImgUrl)) {
				movieSimpleDTO.setPosterUrl(imgUrl.split("\\|")[0]);
			} else {
				movieSimpleDTO.setPosterUrl(imgUrl);
			}

			// 대표 개봉일
			movieSimpleDTO.setReleaseDate(movieObject.optString("repRlsDate", ""));

			// DTO 리스트에 추가
			movieDTOList.add(movieSimpleDTO);
		}

		return movieDTOList;
	}

	private MovieDetailDTO parseMovieDetail(JSONObject jsonResponse) {

		MovieDetailDTO movieDetailDTO = new MovieDetailDTO();

		JSONObject resultObject = jsonResponse.getJSONArray("Data")
			.getJSONObject(0)
			.getJSONArray("Result")
			.getJSONObject(0);

		// 제목에서 !HS, !HE 제거 후 제목 저장
		String title = resultObject.optString("title", "").replaceAll("!HS", "").replaceAll("!HE", "").trim();
		movieDetailDTO.setTitle(title);

		// 포스트 링크 저장
		String imgUrl = resultObject.optString("posters", defaultImgUrl);
		if (!imgUrl.equals(defaultImgUrl)) {
			movieDetailDTO.setPosterUrl(imgUrl.split("\\|")[0]);
		} else {
			movieDetailDTO.setPosterUrl(imgUrl);
		}

		// 장르 저장
		movieDetailDTO.setGenre(resultObject.optString("genre", ""));

		// 대표 개봉일
		movieDetailDTO.setReleaseDate(resultObject.optString("repRlsDate", ""));

		// 줄거리 설정
		JSONArray plotArray = resultObject.optJSONObject("plots").optJSONArray("plot");
		for (int j = 0; j < plotArray.length(); j++) {
			JSONObject plot = plotArray.getJSONObject(j);
			if (plot.optString("plotLang").equals("한국어")) {
				movieDetailDTO.setOverview(plot.optString("plotText"));
			}
		}

		movieDetailDTO.setRunningTime(resultObject.optString("runtime", "상영시간"));

		// 배우 목록 설정 (최대 3명)
		JSONArray actorsArray = resultObject.optJSONObject("actors").optJSONArray("actor");
		List<String> actorsList = new ArrayList<>();
		if (actorsArray != null) {
			for (int k = 0; k < Math.min(actorsArray.length(), 3); k++) {
				String actorName = actorsArray.getJSONObject(k).optString("actorNm", "배우명");
				actorsList.add(actorName);
			}
		}
		movieDetailDTO.setActors(actorsList);

		JSONArray directorsArray = resultObject.optJSONObject("directors").optJSONArray("director");
		movieDetailDTO.setDirector(directorsArray.getJSONObject(0).optString("directorNm", "감독명"));

		return movieDetailDTO;
	}

public MovieDetailDTO getMovieDetailInfo(String movieId, String movieSeq) {
	String response = webClient.get()
		.uri(uriBuilder -> uriBuilder
			.path("/search_json2.jsp")
			.queryParam("ServiceKey", apiKey)
			.queryParam("detail", "Y")
			.queryParam("collection", "kmdb_new2")
			.queryParam("ratedYn", "Y")
			.queryParam("movieId", movieId)
			.queryParam("movieSeq", movieSeq)
			.queryParam("listCount", 5)
			.build())
		.retrieve()
		.bodyToMono(String.class)
		.block();

	return parseMovieDetail(new JSONObject(response));
}
}
