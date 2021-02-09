package com.commonsmodels.models;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class Notificacion {
	@Setter
	private Pedido pedido;
	private Date fechaGen;
	private Date fechaCheck;
}
