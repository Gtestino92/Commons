package com.commonsmodels.models;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BodyPedidosFront {
	private List<Pedido> pedidos;
	private Boolean noMorePedidosLeft;
}
