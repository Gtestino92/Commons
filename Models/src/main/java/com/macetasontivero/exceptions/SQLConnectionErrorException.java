package com.macetasontivero.exceptions;

public class SQLConnectionErrorException extends RuntimeException {

	private static final long serialVersionUID = 5858843290317864248L;

	public SQLConnectionErrorException(String message) {
		super(message);
	}
}
