package com.virtusa.exception;

public class CaseRecordNotFoundException extends RuntimeException {
	// If case record is not found in database throw error
	
	private static final long serialVersionUID = 5599264398252247091L;

	public CaseRecordNotFoundException() {
		super();
	}

	public CaseRecordNotFoundException(String message) {
		super(message);
	}
	
	

}
