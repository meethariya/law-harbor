package com.virtusa.exception;

public class NoBookingFoundException extends RuntimeException {

	
	private static final long serialVersionUID = -3149629713355065193L;

	public NoBookingFoundException() {
		super();
	}

	public NoBookingFoundException(String message) {
		super(message);
	}
	

}
