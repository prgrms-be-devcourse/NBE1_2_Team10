package core.application.api.response.code.status;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
	// 공통 관련 에러
	NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404"),

	// 한줄평 관련 에러
	INVALID_REACTION(HttpStatus.BAD_REQUEST, "COMMENT4000"),
	INVALID_WRITE_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT4001"),
	NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "COMMENT4002"),
	NOT_COMMENT_WRITER(HttpStatus.BAD_REQUEST, "COMMENT4003"),
	// 영화 관련 에러
	NO_SEARCH_RESULT(HttpStatus.BAD_REQUEST, "MOVIE4000"),
	NO_MOVIE(HttpStatus.BAD_REQUEST, "MOVIE4001");

	private final HttpStatus httpStatus;
	private final String code;
}
