package com.commonsmodels.exceptions;

public class ApiConnectionException extends RuntimeException {

	private static final long serialVersionUID = 4339671021065850838L;
	private Exception exceptionOrig;

	public ApiConnectionException(String message) {
		super(message);
	}

	public ApiConnectionException(String message, Exception e) {
		super(message);
		this.exceptionOrig = e;
	}

	public Exception getExceptionOrig() {
		return this.exceptionOrig;
	}
}
