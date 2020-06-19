package com.macetasontivero.mappers.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.macetasontivero.mappers.IMacetasMapper;
import com.macetasontivero.mappers.common.CommonMapper;
import com.macetasontivero.models.FormatoMaceta;
import com.macetasontivero.models.Maceta;
import com.macetasontivero.models.Pedido;

@Component
public class MacetasMapper extends CommonMapper implements IMacetasMapper {

	private static final String CAPACIDAD = "CAPACIDAD";
	private static final String CODIGO = "CODIGO";
	private static final String PRECIO = "PRECIO";
	private static final String ANCHO = "ANCHO";
	private static final String ALTO = "ALTO";
	private static final String LARGO = "LARGO";
	private static final String STOCK = "STOCK";
	private static final String CODIGO_NUEVO = "CODIGO_NUEVO";
	private static final String LINKS_FOTOS = "LINKS_FOTOS";
	private static final String FORMATO = "FORMATO";
	private static final String CANT_IMG_STATIC = "CANT_IMG_STATIC";
	private static final String ESTADO = "ESTADO";

	@Override
	public List<Maceta> getListado(Connection conn) {
		String query = "SELECT * FROM lista_macetas WHERE " + ESTADO + " = 'A'";
		String[] paramsIn = new String[] {};
		String[] outKeys = new String[] { CODIGO, PRECIO, LARGO, ANCHO, ALTO, CAPACIDAD, STOCK, CODIGO_NUEVO,
				LINKS_FOTOS, FORMATO, CANT_IMG_STATIC };
		List<HashMap<String, String>> listMacetasMap = executeQuery(conn, query, paramsIn, outKeys);
		return getListMacetasByListMap(listMacetasMap);
	}

	@Override
	public List<FormatoMaceta> getFormatos(Connection conn) {
		List<FormatoMaceta> formatos = new ArrayList<>();
		String query = "SELECT DISTINCT FORMATO FROM lista_macetas";
		String[] paramsIn = new String[] {};
		String[] outKeys = new String[] { FORMATO };
		List<HashMap<String, String>> listFormatosMap = executeQuery(conn, query, paramsIn, outKeys);
		for (HashMap<String, String> formatoHash : listFormatosMap)
			formatos.add(FormatoMaceta.getFormatoByCode(formatoHash.get(FORMATO)));
		return formatos;
	}

	@Override
	public List<Maceta> getListaByFormato(Connection conn, FormatoMaceta formatoMaceta) {
		String query = "SELECT * FROM lista_macetas WHERE FORMATO = ? AND ESTADO = 'A'";
		String[] paramsIn = new String[] { formatoMaceta.getCode() };
		String[] outKeys = new String[] { CODIGO, PRECIO, LARGO, ANCHO, ALTO, CAPACIDAD, STOCK, CODIGO_NUEVO,
				LINKS_FOTOS, FORMATO, CANT_IMG_STATIC };
		List<HashMap<String, String>> listMacetasMap = executeQuery(conn, query, paramsIn, outKeys);
		return getListMacetasByListMap(listMacetasMap);
	}

	@Override
	public Maceta getMacetaByCodigoNew(Connection conn, String codigoNew) {
		String query = "SELECT * FROM lista_macetas WHERE CODIGO_NUEVO = ? AND ESTADO = 'A'";
		String[] paramsIn = new String[] { codigoNew };
		String[] outKeys = new String[] { CODIGO, PRECIO, LARGO, ANCHO, ALTO, CAPACIDAD, STOCK, CODIGO_NUEVO,
				LINKS_FOTOS, FORMATO, CANT_IMG_STATIC };
		HashMap<String, String> macetaMap = simpleSelect(conn, query, paramsIn, outKeys);
		List<HashMap<String, String>> listMacetasMap = new ArrayList<>();
		listMacetasMap.add(macetaMap);
		return getListMacetasByListMap(listMacetasMap).get(0);
	}

	@Override
	public List<Maceta> getListadoRecomRandomOnlyCodNew(Connection conn, Pedido pedido) {
		String query = "SELECT codigo_nuevo FROM lista_macetas WHERE " + ESTADO + " = 'A'";
		String[] paramsIn = new String[] {};
		String[] outKeys = new String[] { CODIGO_NUEVO };
		List<HashMap<String, String>> listMacetasMap = executeQuery(conn, query, paramsIn, outKeys);
		for (int i = 0; i < pedido.getListadoMacetas().size(); i++) {
			String codNewPedido = pedido.getListadoMacetas().get(i).getCodigoNew();
			listMacetasMap.removeIf(macetaMap -> macetaMap.get(CODIGO_NUEVO).equals(codNewPedido));
		}
		Collections.shuffle(listMacetasMap);
		return getListMacetasCodNewByListMap(listMacetasMap);
	}

	@Override
	public void agregarModelo(Connection conn, Maceta maceta) {
		String query = "INSERT INTO lista_macetas (" + CODIGO + ", " + CODIGO_NUEVO + ", " + PRECIO + ", " + LARGO
				+ ", " + ANCHO + ", " + ALTO + ", " + CAPACIDAD + ", " + STOCK + ", " + LINKS_FOTOS + ", " + FORMATO
				+ ", " + CANT_IMG_STATIC + ", " + ESTADO + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		String[] paramsIn = new String[] { maceta.getCodigo(), maceta.getCodigoNew(), maceta.getPrecio().toString(),
				maceta.getLargo().toString(), maceta.getAncho().toString(), maceta.getAlto().toString(),
				maceta.getCapacidad().toString(), "0", getImagesFromList(maceta.getFotosLink()),
				maceta.getFormato().getCode(), "0", "A" };
		executeUpdate(conn, query, paramsIn);

	}

	@Override
	public void modificarModelo(Connection conn, Maceta maceta) {
		String query = "UPDATE lista_macetas SET " + PRECIO + " = ? , " + LARGO + " = ? , " + ANCHO + " = ? , " + ALTO
				+ " = ? , " + CAPACIDAD + " = ? , " + LINKS_FOTOS + " = ? , " + CANT_IMG_STATIC + " = ? " + "WHERE "
				+ CODIGO_NUEVO + " = ?";

		String[] paramsIn = new String[] { maceta.getPrecio().toString(), maceta.getLargo().toString(),
				maceta.getAncho().toString(), maceta.getAlto().toString(), maceta.getCapacidad().toString(),
				getImagesFromList(maceta.getFotosLink()), maceta.getCantImgStatic().toString(), maceta.getCodigoNew() };
		executeUpdate(conn, query, paramsIn);

	}

	@Override
	public void eliminarModelo(Connection conn, String codigoNew) {
		String query = "UPDATE lista_macetas SET " + ESTADO + " = 'B' WHERE " + CODIGO_NUEVO + "= ?";
		String[] paramsIn = new String[] { codigoNew };
		executeUpdate(conn, query, paramsIn);

	}

	@Override
	public boolean modeloExiste(Connection conn, String codigoNew) {
		String query = "SELECT (CODIGO) FROM lista_macetas WHERE " + CODIGO_NUEVO + " = ?";
		String[] paramsIn = new String[] { codigoNew };
		String[] outKeys = new String[] { CODIGO };
		HashMap<String, String> result = simpleSelect(conn, query, paramsIn, outKeys);
		return result != null;
	}

	private static List<Maceta> getListMacetasByListMap(List<HashMap<String, String>> listMacetasMap) {
		List<Maceta> listaMacetas = new ArrayList<>();
		for (HashMap<String, String> macetaHash : listMacetasMap) {

			String codigo = macetaHash.get(CODIGO);
			BigDecimal precio = new BigDecimal(macetaHash.get(PRECIO));
			BigDecimal largo = new BigDecimal(macetaHash.get(LARGO));
			BigDecimal ancho = new BigDecimal(macetaHash.get(ANCHO));
			BigDecimal alto = new BigDecimal(macetaHash.get(ALTO));
			Integer capacidad = Integer.parseInt(macetaHash.get(CAPACIDAD));
			Integer stock = Integer.parseInt(macetaHash.get(STOCK));
			String codigoNew = macetaHash.get(CODIGO_NUEVO);
			Integer cantImgStatic = Integer.parseInt(macetaHash.get(CANT_IMG_STATIC));
			List<String> fotosLink = ((macetaHash.get(LINKS_FOTOS) != null)
					&& (!"".equals(macetaHash.get(LINKS_FOTOS).toString())))
							? Arrays.asList(macetaHash.get(LINKS_FOTOS).split("\\|"))
							: null;
			FormatoMaceta formato = FormatoMaceta.getFormatoByCode(macetaHash.get(FORMATO));
			listaMacetas.add(Maceta.builder().codigo(codigo).cantImgStatic(cantImgStatic).codigoNew(codigoNew)
					.precio(precio).capacidad(capacidad).largo(largo).alto(alto).ancho(ancho)
					.fotosStatic(makeListFotosStatic(codigoNew, cantImgStatic)).fotosLink(fotosLink).formato(formato)
					.stock(stock).build());
		}
		return listaMacetas;
	}

	private String getImagesFromList(List<String> fotos) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < fotos.size(); i++) {
			result.append(fotos.get(i) + "|");
		}
		return result.toString();
	}

	private static List<Maceta> getListMacetasCodNewByListMap(List<HashMap<String, String>> listMacetasMap) {
		List<Maceta> listaMacetas = new ArrayList<>();
		for (HashMap<String, String> macetaHash : listMacetasMap) {
			String codigoNew = macetaHash.get(CODIGO_NUEVO);
			listaMacetas.add(Maceta.builder().codigoNew(codigoNew).build());
		}
		return listaMacetas;

	}

	private static List<String> makeListFotosStatic(String codigoNew, Integer cantImgStatic) {
		List<String> fotosStatic = new ArrayList<>();
		for (int i = 0; i < cantImgStatic; i++) {
			if (i == 0) {
				fotosStatic.add(codigoNew + ".jpg");
			} else {
				fotosStatic.add(codigoNew + " (" + (i + 1) + ").jpg");
			}
		}
		return fotosStatic;
	}

}
