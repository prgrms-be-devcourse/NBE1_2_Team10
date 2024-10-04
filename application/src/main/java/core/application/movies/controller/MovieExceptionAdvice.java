package core.application.movies.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import core.application.movies.exception.ExceptionResult;
import core.application.movies.exception.NoMovieException;
import core.application.movies.exception.WrongAccessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MovieExceptionAdvice {

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(NoMovieException.class)
	public ExceptionResult handleNoMoviesException(NoMovieException e) {
		log.error("KMDB API를 이용한 검색 결과 아무것도 나오지 않음.");
		// 프론트에서 리턴 타입에 따라 처리가 가능할까?
		return new ExceptionResult(e.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(WrongAccessException.class)
	public ExceptionResult handleWrongAccessException(WrongAccessException e) {
		log.error("잘못된 페이지 접근");
		return new ExceptionResult(e.getMessage());
	}
}
