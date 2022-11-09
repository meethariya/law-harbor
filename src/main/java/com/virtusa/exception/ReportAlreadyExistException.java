package com.virtusa.exception;

public class ReportAlreadyExistException extends RuntimeException {
	// if report already exist for a case record throw error
	
	private static final long serialVersionUID = 2539190440090304883L;

	public ReportAlreadyExistException() {
		super();
	}

	public ReportAlreadyExistException(String message) {
		super(message);
	}

}
