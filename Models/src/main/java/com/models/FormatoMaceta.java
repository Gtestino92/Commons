package com.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum FormatoMaceta {

	OVAL("OVA", "Ovalada"), RECTANGULAR("REC", "Rectangular"), CASCADA("CAS", "Cascada"), BOSQUE("BSQ", "Bosque"),
	REDONDA("RED", "Redonda"), OCTOGONAL("OCT", "Octogonal"), CUADRADA("SQR", "Cuadrada"),
	HEXAGONAL("HEX", "Hexagonal"), OTRAS("OTR", "Otras"), LAGOS("LAG", "Lagos"), GRES("GRS", "Gres");

	private String code;
	private String value;

	private static final Map<String, FormatoMaceta> lookup = new HashMap<String, FormatoMaceta>();

	static {
		for (FormatoMaceta s : EnumSet.allOf(FormatoMaceta.class))
			lookup.put(s.getCode(), s);
	}

	FormatoMaceta(String code, String value) {
		this.value = value;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static FormatoMaceta getFormatoByCode(String code) {
		return lookup.get(code);
	}

}
