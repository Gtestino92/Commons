package com.commonsmodels.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EstadoPedido {

	PENDIENTE("PEN", "Pendiente"), ENTREGADO("ENT", "Entregado"), CANCELADO("CAN", "Cancelado");

	private String code;
	private String value;

	private static final Map<String, EstadoPedido> lookup = new HashMap<>();

	static {
		for (EstadoPedido s : EnumSet.allOf(EstadoPedido.class))
			lookup.put(s.getCode(), s);
	}

	EstadoPedido(String code, String value) {
		this.value = value;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static EstadoPedido getEstadoByCode(String code) {
		return lookup.get(code);
	}
}
