package com.commonsmodels.models;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PedidoNotificacion {
	private Pedido pedido;
	private Date fechaGen;
	private Date fechaCheck;
}
