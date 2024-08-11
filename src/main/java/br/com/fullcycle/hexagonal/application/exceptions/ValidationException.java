package br.com.fullcycle.hexagonal.application.exceptions;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1259444383926867146L;

	public ValidationException(final String message) {
		super(message, null, true, false);
	}

	public ValidationException(final String message, final Throwable cause) {
		super(message, cause, true, false);
	}

}
