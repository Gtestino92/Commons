package com.mappers.impl;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.commonsmodels.models.Notificacion;
import com.commonsmodels.models.Pedido;
import com.mappers.INotificacionesMapper;
import com.mappers.common.CommonMapper;

@Component
public class NotificacionesMapper extends CommonMapper implements INotificacionesMapper {

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static final String ID_PEDIDO = "ID_PEDIDO";
	private static final String FECHA_GEN = "FECHA_GEN";
	private static final String FECHA_CHECK = "FECHA_CHECK";
	private static final String CANT_NOTIF = "CANT_NOTIF";

	@Value("${db.notif.dias}")
	private Integer maxDiasNotif;

	@Override
	public Long getCantNotificaciones(Connection conn) {
		String[] outKeys = new String[] { CANT_NOTIF };
		String query = "SELECT COUNT(*) AS CANT_NOTIF FROM PEDIDOS_NOTIFICACIONES WHERE FECHA_GEN > NOW() - interval "
				+ maxDiasNotif.toString() + " day ORDER BY FECHA_GEN DESC";
		HashMap<String, String> listNotifMap = simpleSelect(conn, query, new String[] {}, outKeys);
		return Long.parseLong(listNotifMap.get(CANT_NOTIF).toString());
	}

	@Override
	public List<Notificacion> getNotificacionesNuevas(Connection conn) {
		String[] outKeys = new String[] { ID_PEDIDO, FECHA_GEN, FECHA_CHECK};
		String query = "SELECT * FROM PEDIDOS_NOTIFICACIONES WHERE FECHA_GEN > NOW() - interval "
				+ maxDiasNotif.toString() + " day ORDER BY FECHA_GEN DESC";
		List<HashMap<String, String>> listNotifMap = executeQuery(conn, query, new String[] {}, outKeys);
		return getListNotificationsByListMap(listNotifMap);
	}

	@Override
	public void generarNotificacion(Connection conn, Long idPedido, Date fechaGen) {
		String[] paramsIn = new String[] { idPedido.toString(), formatter.format(fechaGen) };
		String query = "INSERT INTO PEDIDOS_NOTIFICACIONES (ID_PEDIDO, FECHA_GEN) VALUES (?, ?)";
		executeUpdate(conn, query, paramsIn);
	}

	@Override
	public void marcarComoLeida(Connection conn, Long idPedido, Date fechaCheck) {
		String[] paramsIn = new String[] { formatter.format(fechaCheck), idPedido.toString() };
		String query = "UPDATE PEDIDOS_NOTIFICACIONES SET FECHA_CHECK = ? WHERE ID_PEDIDO = ?";
		executeUpdate(conn, query, paramsIn);
	}

	private List<Notificacion> getListNotificationsByListMap(List<HashMap<String, String>> listNotificationsHash) {
		List<Notificacion> listNotifications = new ArrayList<>();
		for (HashMap<String, String> notifHash : listNotificationsHash) {
			Long idPedido = Long.parseLong(notifHash.get(ID_PEDIDO));
			Date fechaGen;
			Date fechaCheck;
			try {
				fechaGen = formatter.parse(notifHash.get(FECHA_GEN));
				fechaCheck = notifHash.get(FECHA_CHECK) != null ? formatter.parse(notifHash.get(FECHA_CHECK)) : null;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			listNotifications.add(Notificacion.builder().pedido(Pedido.builder().idPedido(idPedido).build())
					.fechaGen(fechaGen).fechaCheck(fechaCheck).build());
		}
		return listNotifications;
	}

}
