package core.application.reviews;

import core.application.reviews.exceptions.InvalidCommentContentException;
import core.application.reviews.exceptions.NoReviewCommentFoundException;
import core.application.reviews.exceptions.NoReviewFoundException;
import core.application.reviews.exceptions.NotCommentOwnerException;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "core.application.reviews")
public class ReviewExceptionHandler {

    @ExceptionHandler({
            NoReviewFoundException.class, NoReviewCommentFoundException.class,
            NotCommentOwnerException.class,
            InvalidCommentContentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleException(RuntimeException e) {
        logHandledException(e.getClass().getName(), e);
        return new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void logHandledException(String methodName, Throwable e) {
        log.warn("[ReviewExceptionHandler - {}] handled exception : {}", methodName,
                e.getMessage());
        log.debug(e.getMessage(), e);
    }

    @Data
    @AllArgsConstructor
    public static class ResponseError {

        private String message;
        private HttpStatus status;
    }
}
