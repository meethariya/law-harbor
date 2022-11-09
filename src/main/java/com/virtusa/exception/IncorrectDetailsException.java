package com.virtusa.exception;

public class IncorrectDetailsException extends RuntimeException {
	// If any input from user is invalid throw error
	
	private static final long serialVersionUID = -8523715851150247893L;

	public IncorrectDetailsException() {
		super();
	}

	public IncorrectDetailsException(String message) {
		super(message);
	}
	

}
