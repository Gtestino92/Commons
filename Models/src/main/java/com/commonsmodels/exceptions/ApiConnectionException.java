package com.commonsmodels.exceptions;

public class ApiConnectionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6333415927398790139L;
	private final String typeError;

	public ApiConnectionException(String typeError, String message) {
		super(message);
		this.typeError = typeError;
	}

	public String getTypeError() {
		return typeError;
	}
}
