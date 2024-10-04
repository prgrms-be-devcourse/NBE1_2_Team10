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

    @ExceptionHandler(NoReviewFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleNoReviewFoundException(NoReviewFoundException e) {
        String stackTrace = logHandledException("handleNoReviewFoundException", e);
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST, stackTrace);
    }

    @ExceptionHandler(NoReviewCommentFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleNoReviewCommentFoundException(NoReviewCommentFoundException e) {
        String stackTrace = logHandledException("handleNoReviewCommentFoundException", e);
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST, stackTrace);
    }

    @ExceptionHandler(NotCommentOwnerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleNotCommentOwnerException(NotCommentOwnerException e) {
        String stackTrace = logHandledException("handleNotCommentOwnerException", e);
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST, stackTrace);
    }

    @ExceptionHandler(InvalidCommentContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleInvalidCommentContentException(InvalidCommentContentException e) {
        String stackTrace = logHandledException("handleInvalidCommentContentException", e);
        return new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST, stackTrace);
    }

    private String logHandledException(String methodName, Throwable e) {
        String stackTrace = getStackTraceToString(e);
        log.warn("[ReviewExceptionHandler - {}] handled exception : {}", methodName,
                e.getMessage());
        log.debug(e.getMessage(), e);
        return stackTrace;
    }

    private String getStackTraceToString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    @Data
    @AllArgsConstructor
    public static class ResponseError {

        private String message;
        private HttpStatus status;
        private String stackTrace;
    }
}
