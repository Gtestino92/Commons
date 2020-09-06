package com.commonsmodels.models;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PedidosEntregadosGraph {
	private List<Date> fechas;
	private List<FormatoGraph> dataByFormato;
}
