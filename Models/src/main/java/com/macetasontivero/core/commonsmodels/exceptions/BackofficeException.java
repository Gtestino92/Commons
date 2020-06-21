package com.macetasontivero.core.commonsmodels.exceptions;

public class BackofficeException extends RuntimeException {
	private static final long serialVersionUID = 4436033181430110194L;
	private final String typeError;

	public BackofficeException(String typeError, String message) {
		super(message);
		this.typeError = typeError;
	}

	public String getTypeError() {
		return typeError;
	}
}
