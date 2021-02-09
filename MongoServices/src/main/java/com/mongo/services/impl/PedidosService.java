package com.mongo.services.impl;

import static com.mongo.utils.DateUtils.getDateCorrectGMT;
import static com.mongodb.client.model.Updates.set;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import com.commonsmodels.models.BodyPedidosFront;
import com.commonsmodels.models.EstadoPedido;
import com.commonsmodels.models.Maceta;
import com.commonsmodels.models.Pedido;
import com.mongo.services.IPedidosService;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@Component
public class PedidosService implements IPedidosService {

	private static final String MONGODB_PEDIDOS = "pedidos";
	private static final String MONGODB_PEDIDOS_INFO = "pedidos_info";
	private static final String CELULAR = "celular";
	private static final String NOMBRE = "nombre";
	private static final String MAIL = "mail";
	private static final String TOTAL = "total";
	private static final String ESTADO_PEDIDO = "estado_pedido";
	private static final String ID_PEDIDO = "id_pedido";
	private static final String ID_DEFAULT_MONGO = "_id";
	private static final String FECHA_SOLICITUD = "fecha_solicitud";
	private static final String FECHA_ENTREGA = "fecha_entrega";
	private static final Integer CANT_PEDIDOS = 5;

	@Override
	public BodyPedidosFront getPedidosByEstado(MongoDatabase mongoDb, EstadoPedido estado, Date fechaSolicitudDesde,
			Date fechaSolicitudHasta, Date fechaEntregaDesde, Date fechaEntregaHasta, Integer countFrom,
			Boolean isNotFilt) throws ParseException {
		Boolean noMorePedidosLeft = Boolean.FALSE;
		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		MongoCollection<Document> pedidosCollection = mongoDb.getCollection(MONGODB_PEDIDOS);
		List<Pedido> pedidos = new ArrayList<>();
		Bson filter;
		if (EstadoPedido.ENTREGADO.equals(estado))
			filter = Filters.and(Filters.eq(ESTADO_PEDIDO, estado.getCode()),
					Filters.gte(FECHA_SOLICITUD, fechaSolicitudDesde),
					Filters.lte(FECHA_SOLICITUD, fechaSolicitudHasta), Filters.gte(FECHA_ENTREGA, fechaEntregaDesde),
					Filters.lte(FECHA_ENTREGA, fechaEntregaHasta));
		else
			filter = Filters.and(Filters.eq(ESTADO_PEDIDO, estado.getCode()),
					Filters.gte(FECHA_SOLICITUD, fechaSolicitudDesde),
					Filters.lte(FECHA_SOLICITUD, fechaSolicitudHasta));

		MongoCursor<Document> cursorInfo = pedidosInfoCollection.find(filter)
				.sort(new BasicDBObject(FECHA_SOLICITUD, -1)).iterator();
		int i = 0;
		if (isNotFilt) {
			for (i = 0; i < countFrom; i++)
				cursorInfo.next();
		}
		while ((cursorInfo.hasNext() && (isNotFilt && i < countFrom + CANT_PEDIDOS))
				|| cursorInfo.hasNext() && (!isNotFilt)) {
			Document infoDoc = cursorInfo.next();
			Pedido pedido = getInfoPedidoFromDoc(infoDoc, estado);
			Document pedidoListaDoc = pedidosCollection.find(Filters.eq(ID_PEDIDO, pedido.getIdPedido().toString()))
					.first();
			pedido.setListadoMacetas(getListadoFromPedidoDoc(pedidoListaDoc));
			pedidos.add(pedido);
			i++;
		}

		if (!isNotFilt || (i == countFrom) || (i <= countFrom + CANT_PEDIDOS && !cursorInfo.hasNext()))
			noMorePedidosLeft = Boolean.TRUE;
		return BodyPedidosFront.builder().pedidos(pedidos).noMorePedidosLeft(noMorePedidosLeft).build();
	}

	private Pedido getInfoPedidoFromDoc(Document infoDoc, EstadoPedido estado) {
		String celular = infoDoc.getString(CELULAR);
		String mail = infoDoc.getString(MAIL);
		String nombre = infoDoc.getString(NOMBRE);
		Long total = Long.parseLong(infoDoc.getString(TOTAL));
		Long idPedido = Long.parseLong(infoDoc.getString(ID_PEDIDO));
		Pedido pedido = Pedido.builder().nombre(nombre).celular(celular).mail(mail).total(total).idPedido(idPedido)
				.estadoPedido(estado).build();
		pedido.setFechaSolicitud((Date) infoDoc.get(FECHA_SOLICITUD));
		if (EstadoPedido.ENTREGADO.equals(estado))
			pedido.setFechaEntrega((Date) infoDoc.get(FECHA_ENTREGA));
		return pedido;
	}

	@Override
	public void altaPedido(MongoDatabase mongoDb, Pedido pedido) {
		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		MongoCollection<Document> pedidosCollection = mongoDb.getCollection(MONGODB_PEDIDOS);

		Document pedidoInfoDocument = new Document();
		Long idPedido = getNextIdPedido(pedidosCollection);
		pedidoInfoDocument.append(ID_PEDIDO, idPedido.toString());
		if (!"".equals(pedido.getCelular()) && pedido.getCelular() != null)
			pedidoInfoDocument.append(CELULAR, pedido.getCelular());
		pedidoInfoDocument.append(MAIL, pedido.getMail());
		pedidoInfoDocument.append(NOMBRE, pedido.getNombre());
		pedidoInfoDocument.append(ESTADO_PEDIDO, pedido.getEstadoPedido().getCode());
		pedidoInfoDocument.append(TOTAL, pedido.getTotal().toString());
		pedidoInfoDocument.append(FECHA_SOLICITUD, pedido.getFechaSolicitud());
		if (pedido.getEstadoPedido().equals(EstadoPedido.ENTREGADO))
			pedidoInfoDocument.append(FECHA_ENTREGA, pedido.getFechaEntrega());

		pedidosInfoCollection.insertOne(pedidoInfoDocument);

		Document pedidoDocument = new Document();
		pedidoDocument.append(ID_PEDIDO, idPedido.toString());
		for (int i = 0; i < pedido.getListadoMacetas().size(); i++) {
			Maceta modeloPedido = pedido.getListadoMacetas().get(i);
			pedidoDocument.append(modeloPedido.getCodigoNew(), modeloPedido.getCantSolicitada());
		}
		pedidosCollection.insertOne(pedidoDocument);

	}

	@Override
	public List<Maceta> getListadoPedidoById(MongoDatabase mongoDb, Long idPedido) {
		MongoCollection<Document> pedidosCollection = mongoDb.getCollection(MONGODB_PEDIDOS);
		Document pedidoListaDoc = pedidosCollection.find(Filters.eq(ID_PEDIDO, idPedido.toString())).first();
		return getListadoFromPedidoDoc(pedidoListaDoc);
	}

	@Override
	public void setPedidoAsEntregado(MongoDatabase mongoDb, Long idPedido) {
		Date fechaHoy = getDateCorrectGMT();
		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		Bson filter = Filters.eq(ID_PEDIDO, idPedido.toString());
		Bson updateOperationFecha = set(FECHA_ENTREGA, fechaHoy);
		Bson updateOperationEstado = set(ESTADO_PEDIDO, EstadoPedido.ENTREGADO.getCode());
		pedidosInfoCollection.updateOne(filter, updateOperationFecha);
		pedidosInfoCollection.updateOne(filter, updateOperationEstado);
	}

	@Override
	public void modificarPedido(MongoDatabase mongoDb, Pedido pedido) {

		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		Bson filter = Filters.eq(ID_PEDIDO, pedido.getIdPedido().toString());
		Bson updateOperationTotal = set(TOTAL, pedido.getTotal().toString());
		pedidosInfoCollection.updateOne(filter, updateOperationTotal);

		MongoCollection<Document> pedidosCollection = mongoDb.getCollection(MONGODB_PEDIDOS);
		pedidosCollection.deleteOne(filter);
		Document pedidoModif = new Document();
		pedidoModif.append(ID_PEDIDO, pedido.getIdPedido().toString());
		for (int i = 0; i < pedido.getListadoMacetas().size(); i++) {
			Maceta modeloPedido = pedido.getListadoMacetas().get(i);
			pedidoModif.append(modeloPedido.getCodigoNew(), modeloPedido.getCantSolicitada());
		}
		pedidosCollection.insertOne(pedidoModif);
	}

	@Override
	public void cancelarPedido(MongoDatabase mongoDb, Long idPedido) {
		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		Bson filter = Filters.eq(ID_PEDIDO, idPedido.toString());
		Bson updateOperationEstado = set(ESTADO_PEDIDO, EstadoPedido.CANCELADO.getCode());
		pedidosInfoCollection.updateOne(filter, updateOperationEstado);
	}

	@Override
	public boolean pedidoExiste(MongoDatabase mongoDb, Long idPedido, EstadoPedido estado) {
		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		MongoCollection<Document> pedidosCollection = mongoDb.getCollection(MONGODB_PEDIDOS);
		Bson filterInfo = Filters.and(Filters.eq(ID_PEDIDO, idPedido.toString()),
				Filters.eq(ESTADO_PEDIDO, estado.getCode()));
		Bson filterLista = Filters.eq(ID_PEDIDO, idPedido.toString());
		MongoCursor<Document> cursor = pedidosCollection.find(filterLista).iterator();
		MongoCursor<Document> cursorInfo = pedidosInfoCollection.find(filterInfo).iterator();
		return (cursor.hasNext() && cursorInfo.hasNext());
	}

	@Override
	public Long insertPedido(MongoDatabase mongoDb, Pedido pedido) {
		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		MongoCollection<Document> pedidosCollection = mongoDb.getCollection(MONGODB_PEDIDOS);

		Document pedidoInfoDocument = new Document();
		Long idPedido = getNextIdPedido(pedidosCollection);
		pedidoInfoDocument.append(ID_PEDIDO, idPedido.toString());
		if (!"".equals(pedido.getCelular()) && pedido.getCelular() != null)
			pedidoInfoDocument.append(CELULAR, pedido.getCelular());
		pedidoInfoDocument.append(MAIL, pedido.getMail());
		pedidoInfoDocument.append(NOMBRE, pedido.getNombre());
		pedidoInfoDocument.append(ESTADO_PEDIDO, pedido.getEstadoPedido().getCode());
		pedidoInfoDocument.append(TOTAL, pedido.getTotal().toString());
		pedidoInfoDocument.append(FECHA_SOLICITUD, pedido.getFechaSolicitud());
		pedidosInfoCollection.insertOne(pedidoInfoDocument);

		Document pedidoDocument = new Document();
		pedidoDocument.append(ID_PEDIDO, idPedido.toString());
		for (int i = 0; i < pedido.getListadoMacetas().size(); i++) {
			Maceta modeloPedido = pedido.getListadoMacetas().get(i);
			pedidoDocument.append(modeloPedido.getCodigoNew(), modeloPedido.getCantSolicitada());
		}
		pedidosCollection.insertOne(pedidoDocument);
		return idPedido;
	}

	@Override
	public Pedido getPedidoById(MongoDatabase mongoDb, Long idPedido) {
		MongoCollection<Document> pedidosInfoCollection = mongoDb.getCollection(MONGODB_PEDIDOS_INFO);
		MongoCollection<Document> pedidosCollection = mongoDb.getCollection(MONGODB_PEDIDOS);
		Bson filter = Filters.eq(ID_PEDIDO, idPedido.toString());
		Document infoDoc = pedidosInfoCollection.find(filter).first();
		Document pedidoListaDoc = pedidosCollection.find(filter).first();
		Pedido pedido = getInfoPedidoFromDoc(infoDoc, EstadoPedido.PENDIENTE);
		pedido.setListadoMacetas(getListadoFromPedidoDoc(pedidoListaDoc));
		return pedido;
	}

	private List<Maceta> getListadoFromPedidoDoc(Document pedidoListaDoc) {
		List<Maceta> listado = new ArrayList<>();
		Set<String> keys = pedidoListaDoc.keySet();
		keys.remove(ID_DEFAULT_MONGO);
		keys.remove(ID_PEDIDO);
		Iterator<String> iterator = keys.iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			Integer value = (Integer) pedidoListaDoc.get(key);
			listado.add(Maceta.builder().codigoNew((String) key).cantSolicitada(value).build());
		}
		return listado;
	}

	private Long getNextIdPedido(MongoCollection<Document> pedidosCollection) {
		if (pedidosCollection == null || pedidosCollection.countDocuments() == 0)
			return Long.parseLong("0");
		else {
			List<String> listIdPedidos = new ArrayList<>();
			MongoCursor<Document> cursor = pedidosCollection.find().iterator();
			while (cursor.hasNext()) {
				Document info = cursor.next();
				listIdPedidos.add(info.getString(ID_PEDIDO));
			}
			return getMaxIdFromList(listIdPedidos) + 1;
		}
	}

	private Long getMaxIdFromList(List<String> listIdPedidos) {
		List<Long> idPedidosLong = new ArrayList<>();
		for (String id : listIdPedidos) {
			idPedidosLong.add(Long.parseLong(id));
		}
		return Collections.max(idPedidosLong);
	}

}
