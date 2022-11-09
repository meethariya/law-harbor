package com.virtusa.exception;

public class UserAlreadyExistException extends RuntimeException {
	// if user already exist with same email or phone number, throw error
	
	private static final long serialVersionUID = 1418424021952546894L;

	public UserAlreadyExistException() {
		super();
	}

	public UserAlreadyExistException(String message) {
		super(message);
	}

}
