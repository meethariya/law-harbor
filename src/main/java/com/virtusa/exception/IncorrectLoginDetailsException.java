package com.virtusa.exception;

public class IncorrectLoginDetailsException extends RuntimeException {
	// If login details mismatch or incorrect throw error
	
	private static final long serialVersionUID = 6262368035113914765L;

	public IncorrectLoginDetailsException() {
		super();
	}

	public IncorrectLoginDetailsException(String message) {
		super(message);
	}
}
