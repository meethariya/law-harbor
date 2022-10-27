package com.virtusa.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7611704751443809879L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
		super(message);
	}
}
