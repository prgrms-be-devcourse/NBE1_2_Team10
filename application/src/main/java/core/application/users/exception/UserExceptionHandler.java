package core.application.users.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import core.application.api.response.ApiResponse;
import core.application.api.response.code.status.ErrorStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackages = "core.application.users")
public class UserExceptionHandler {

	@ExceptionHandler(DuplicateEmailException.class)
	public ApiResponse<?> handleDuplicateEmailException(DuplicateEmailException e) {
		log.error("중복된 EMAIL 회원가입 시도");
		return ApiResponse.onFailure(ErrorStatus.DUPLICATE_EMAIL.getCode(), e.getMessage(), null);
	}
}
