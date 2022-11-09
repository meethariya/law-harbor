package com.virtusa.exception;

public class SlotAlreadyReservedException extends RuntimeException {
	// if same slot is reserved by another user, throw error
	
	private static final long serialVersionUID = 8980515439841332296L;

	public SlotAlreadyReservedException() {
		super();
	}

	public SlotAlreadyReservedException(String message) {
		super(message);
	}

}
