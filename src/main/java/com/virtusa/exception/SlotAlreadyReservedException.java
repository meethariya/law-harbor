package com.virtusa.exception;

public class SlotAlreadyReservedException extends RuntimeException {

	private static final long serialVersionUID = 8980515439841332296L;

	public SlotAlreadyReservedException() {
		super();
	}

	public SlotAlreadyReservedException(String message) {
		super(message);
	}

}
