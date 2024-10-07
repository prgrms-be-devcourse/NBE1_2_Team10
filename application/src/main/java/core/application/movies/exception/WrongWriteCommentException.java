package core.application.movies.exception;

public class WrongWriteCommentException extends RuntimeException {
	public WrongWriteCommentException(String message) {
		super(message);
	}

	public WrongWriteCommentException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongWriteCommentException(Throwable cause) {
		super(cause);
	}

	public WrongWriteCommentException() {
		super();
	}
}
