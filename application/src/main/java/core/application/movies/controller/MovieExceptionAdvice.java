package core.application.movies.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import core.application.movies.exception.ExceptionResult;
import core.application.movies.exception.InvalidReactionException;
import core.application.movies.exception.NotFoundCommentException;
import core.application.movies.exception.WrongWriteCommentException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MovieExceptionAdvice {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({NotFoundCommentException.class, InvalidReactionException.class,
		WrongWriteCommentException.class})
	public ExceptionResult handleNotFoundCommentException(NotFoundCommentException e) {
		log.error(e.getMessage());
		return new ExceptionResult(e.getMessage());
	}
}
