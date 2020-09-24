package com.commonsmodels.exceptions;

public class ApiConnectionRestException extends RuntimeException {

	private static final long serialVersionUID = 4339671021065850838L;
	private Exception exceptionOrig;

	public ApiConnectionRestException(String message) {
		super(message);
	}

	public ApiConnectionRestException(String message, Exception e) {
		super(message);
		this.exceptionOrig = e;
	}

	public Exception getExceptionOrig() {
		return this.exceptionOrig;
	}
}
