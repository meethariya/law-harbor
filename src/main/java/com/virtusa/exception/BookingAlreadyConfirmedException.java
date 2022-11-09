package com.virtusa.exception;

public class BookingAlreadyConfirmedException extends RuntimeException {
	// if booking is already confirmed by lawyer and user tries to cancel it
	// throw error
	
	private static final long serialVersionUID = -1265132990976887770L;

	public BookingAlreadyConfirmedException() {
		super();
	}

	public BookingAlreadyConfirmedException(String message) {
		super(message);
	}
	
}
