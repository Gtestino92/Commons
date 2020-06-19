package com.macetasontivero.models;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class Maceta {

	private String codigo;
	private String codigoNew;
	private BigDecimal precio;
	private BigDecimal alto;
	private BigDecimal ancho;
	private Integer capacidad;
	private BigDecimal largo;
	private Integer stock;	
	private List<String> fotosStatic;
	private List<String> fotosLink;
	private FormatoMaceta formato;
	@Setter
	private Integer cantSolicitada;
	private Integer cantImgStatic;
	
}
