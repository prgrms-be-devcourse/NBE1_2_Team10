package core.application.movies.exception;

public class WrongAccessException extends RuntimeException {
	public WrongAccessException() {
		super();
	}

	public WrongAccessException(String message) {
		super(message);
	}

	public WrongAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public WrongAccessException(Throwable cause) {
		super(cause);
	}
}
