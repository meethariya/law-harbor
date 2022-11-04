package com.virtusa.exception;

public class IncorrectDetailsException extends RuntimeException {

	private static final long serialVersionUID = -8523715851150247893L;

	public IncorrectDetailsException() {
		super();
	}

	public IncorrectDetailsException(String message) {
		super(message);
	}
	

}
