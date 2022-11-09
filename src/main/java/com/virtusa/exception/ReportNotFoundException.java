package com.virtusa.exception;

public class ReportNotFoundException extends RuntimeException {
	// if report not found in database throw error
	
	private static final long serialVersionUID = 6702324190009790866L;

	public ReportNotFoundException() {
		super();
	}

	public ReportNotFoundException(String message) {
		super(message);
	}

}
