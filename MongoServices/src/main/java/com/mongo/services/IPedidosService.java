package com.mongo.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.commonsmodels.models.BodyPedidosFront;
import com.commonsmodels.models.EstadoPedido;
import com.commonsmodels.models.Maceta;
import com.commonsmodels.models.Pedido;
import com.mongodb.client.MongoDatabase;

public interface IPedidosService {

	public BodyPedidosFront getPedidosByEstado(MongoDatabase mongoDb, EstadoPedido estado, Date fechaSolicitudDesde,
			Date fechaSolicitudHasta, Date fechaEntregaDesde, Date fechaEntregaHasta, Integer countFrom,
			Boolean isNotFilt) throws ParseException;

	public void altaPedido(MongoDatabase mongoDb, Pedido pedido);

	public List<Maceta> getListadoPedidoById(MongoDatabase mongoDb, Long idPedido);

	public void modificarPedido(MongoDatabase mongoDb, Pedido pedido);

	public void setPedidoAsEntregado(MongoDatabase mongoDb, Long idPedido);

	public void cancelarPedido(MongoDatabase mongoDb, Long idPedido);

	public boolean pedidoExiste(MongoDatabase mongoDb, Long idPedido, EstadoPedido estado);

	public Long insertPedido(MongoDatabase mongoDb, Pedido pedido);
}
