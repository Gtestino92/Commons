package com.commonsmodels.exceptions;

public class InvalidRequestApiException extends RuntimeException {

	private static final long serialVersionUID = 117174560264616509L;

	private final Integer code;
	private final String typeError;

	public Integer getCode() {
		return this.code;
	}

	public String getTypeError() {
		return this.typeError;
	}

	public InvalidRequestApiException(String message, Integer code, String typeError) {
		super(message);
		this.code = code;
		this.typeError = typeError;
	}
}
