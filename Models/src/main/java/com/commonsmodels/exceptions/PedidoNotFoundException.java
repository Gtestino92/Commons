package com.commonsmodels.exceptions;

public class PedidoNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1439585182672715597L;

	public PedidoNotFoundException(String message) {
		super(message);
	}
}
