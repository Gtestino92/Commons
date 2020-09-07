package com.commonsmodels.models;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ColorGraph {

	MARRON("OVA", 102, 51, 0), ROJO("REC", 255, 36, 0), BLANCO("CAS", 243, 243, 243), MARRON_CL("BSQ", 153, 76, 0),
	VERDE_CL("RED", 102, 255, 102), AZUL("OCT", 51, 51, 255), AMARILLO("SQR", 255, 255, 51),
	VERDE_OSC("HEX", 0, 102, 0), NEGRO("OTR", 10, 10, 10), CELESTE("LAG", 0, 245, 245), GRIS("GRS", 96, 96, 96);

	private String codMaceta;
	private Integer r;
	private Integer g;
	private Integer b;

	public String getCodMaceta() {
		return codMaceta;
	}

	public Integer getR() {
		return r;
	}

	public Integer getG() {
		return g;
	}

	public Integer getB() {
		return b;
	}

	ColorGraph(String codMaceta, Integer r, Integer g, Integer b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.codMaceta = codMaceta;
	}

	private static final Map<String, ColorGraph> lookup = new HashMap<String, ColorGraph>();

	static {
		for (ColorGraph s : EnumSet.allOf(ColorGraph.class))
			lookup.put(s.getCodMaceta(), s);
	}

	public static ColorGraph getColorByCodMaceta(String codMaceta) {
		return lookup.get(codMaceta);
	}
}
