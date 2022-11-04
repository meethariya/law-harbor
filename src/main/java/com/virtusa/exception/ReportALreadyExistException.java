package com.virtusa.exception;

public class ReportALreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 2539190440090304883L;

	public ReportALreadyExistException() {
		super();
	}

	public ReportALreadyExistException(String message) {
		super(message);
	}

}
