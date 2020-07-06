package com.mappers;

import java.sql.Connection;
import java.util.List;

import com.commonsmodels.models.FormatoMaceta;
import com.commonsmodels.models.Maceta;
import com.commonsmodels.models.Pedido;

public interface IMacetasMapper {

	public List<Maceta> getListado(Connection conn);

	public List<Maceta> getListadoPrecios(Connection conn);

	public List<FormatoMaceta> getFormatos(Connection conn);

	public List<Maceta> getListaByFormato(Connection conn, FormatoMaceta formato);

	public Maceta getMacetaByCodigoNew(Connection conn, String codigoNew);

	public List<Maceta> getListadoRecomRandomOnlyCodNew(Connection conn, Pedido pedido);

	public void agregarModelo(Connection conn, Maceta maceta);

	public void modificarModelo(Connection conn, Maceta maceta);

	public void eliminarModelo(Connection conn, String codigoNew);

	public boolean modeloExiste(Connection conn, String codigoNew);
}
