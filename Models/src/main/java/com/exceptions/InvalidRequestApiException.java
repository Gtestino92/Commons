package com.exceptions;

public class InvalidRequestApiException extends RuntimeException {

	private static final long serialVersionUID = 117174560264616509L;

	private final Integer code;

	public Integer getCode() {
		return this.code;
	}

	public InvalidRequestApiException(String message, Integer code) {
		super(message);
		this.code = code;
	}
}
