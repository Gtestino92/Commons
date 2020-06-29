package com.commonsmodels.models;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class Pedido {

	private Long idPedido;
	@Setter
	private List<Maceta> listadoMacetas;
	private EstadoPedido estadoPedido;
	private String celular;
	private String mail;
	private String nombre;
	private String msjMail;
	private Long total;
	@Setter
	private Date fechaSolicitud;
	@Setter
	private Date fechaEntrega;

}
