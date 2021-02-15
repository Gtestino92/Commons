package com.commonsmodels.exceptions;

public class InvalidRequestApiRestException extends RuntimeException {

	private static final long serialVersionUID = -4871059808792904433L;
	private final Integer code;

	public Integer getCode() {
		return this.code;
	}

	public InvalidRequestApiRestException(String message, Integer code) {
		super(message);
		this.code = code;
	}
}
