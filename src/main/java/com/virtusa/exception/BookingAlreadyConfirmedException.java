package com.virtusa.exception;

public class BookingAlreadyConfirmedException extends RuntimeException {

	private static final long serialVersionUID = -1265132990976887770L;

	public BookingAlreadyConfirmedException() {
		super();
	}

	public BookingAlreadyConfirmedException(String message) {
		super(message);
	}
	
}
