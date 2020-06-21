package com.macetasontivero.core.mongo.services;

import java.text.ParseException;
import java.util.List;

import com.macetasontivero.core.commonsmodels.models.EstadoPedido;
import com.macetasontivero.core.commonsmodels.models.Maceta;
import com.macetasontivero.core.commonsmodels.models.Pedido;
import com.mongodb.client.MongoDatabase;

public interface IPedidosService {

	public List<Pedido> getPedidosByEstado(MongoDatabase mongoDb, EstadoPedido estado) throws ParseException;

	public void altaPedido(MongoDatabase mongoDb, Pedido pedido);

	public List<Maceta> getListadoPedidoById(MongoDatabase mongoDb, Long idPedido);

	public void modificarPedido(MongoDatabase mongoDb, Pedido pedido);

	public void setPedidoAsEntregado(MongoDatabase mongoDb, Long idPedido);

	public void cancelarPedido(MongoDatabase mongoDb, Long idPedido);

	public boolean pedidoExiste(MongoDatabase mongoDb, Long idPedido, EstadoPedido estado);

	public void insertPedido(MongoDatabase mongoDb, Pedido pedido);
}
