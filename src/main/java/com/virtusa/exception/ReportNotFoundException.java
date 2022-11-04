package com.virtusa.exception;

public class ReportNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6702324190009790866L;

	public ReportNotFoundException() {
		super();
	}

	public ReportNotFoundException(String message) {
		super(message);
	}

}
