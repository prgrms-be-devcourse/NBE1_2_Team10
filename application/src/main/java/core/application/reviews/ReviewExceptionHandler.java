package core.application.reviews;

import core.application.reviews.exceptions.NoReviewCommentFoundException;
import core.application.reviews.exceptions.NoReviewFoundException;
import core.application.reviews.exceptions.NotCommentOwnerException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "core.application.reviews")
public class ReviewExceptionHandler {

    @ExceptionHandler(NoReviewFoundException.class)
    public ResponseError handleNoReviewFoundException(NoReviewFoundException e) {
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST, e.getStackTrace());
    }

    @ExceptionHandler(NoReviewCommentFoundException.class)
    public ResponseError handleNoReviewCommentFoundException(NoReviewCommentFoundException e) {
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST, e.getStackTrace());
    }

    @ExceptionHandler(NotCommentOwnerException.class)
    public ResponseError handleNotCommentOwnerException(NotCommentOwnerException e) {
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST, e.getStackTrace());
    }

    @AllArgsConstructor
    public static class ResponseError {

        private String message;
        private HttpStatus status;
        private StackTraceElement[] stackTrace;
    }
}
