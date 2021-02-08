package com.mappers;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

import com.commonsmodels.models.PedidoNotificacion;

public interface INotificacionesMapper {

	public List<PedidoNotificacion> getNotificaciones(Connection conn);

	public List<PedidoNotificacion> getNotificacionesNuevas(Connection conn);

	public void generarNotificacion(Connection conn, Long idPedido, Date fechaGen);

	public void marcarComoLeida(Connection conn, Long idPedido, Date fechaCheck);
}
