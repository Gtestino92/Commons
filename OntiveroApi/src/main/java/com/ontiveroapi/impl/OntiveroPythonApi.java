package com.ontiveroapi.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.json.JsonGenerator;
import com.models.Maceta;
import com.models.Pedido;
import com.ontiveroapi.IOntiveroPythonApi;
import com.ontiveroapi.common.CommonApiConnector;

import okhttp3.FormBody;
import okhttp3.Request;

@PropertySource("classpath:python-api.properties")
@Component
public class OntiveroPythonApi extends CommonApiConnector implements IOntiveroPythonApi {

	@Value("${api.url}")
	private String baseUrl;

	@Override
	public List<Maceta> getRecomendaciones(Pedido pedidoSolicitado) {
		FormBody.Builder formBody = new FormBody.Builder();
		List<Maceta> listado = pedidoSolicitado.getListadoMacetas();
		for (int i = 0; i < listado.size(); i++) {
			formBody.add("codigoNew" + i, listado.get(i).getCodigoNew());
			formBody.add("cantSolicitada" + i, listado.get(i).getCantSolicitada().toString());
		}
		formBody.add("cantModelos", Integer.toString(listado.size()));
		Request request = new Request.Builder().url(baseUrl + "/getRecomendaciones").post(formBody.build()).build();
		return getListadoRecomendaciones(makeCallStrResponse(request));
	}

	@SuppressWarnings("unchecked")
	private List<Maceta> getListadoRecomendaciones(String makeCallStrResponse) {
		JSONArray listCodigos = JsonGenerator.convertStringToJSONArray(makeCallStrResponse);
		List<Maceta> macetas = new ArrayList<>();
		Iterator<String> iterator = listCodigos.iterator();
		while (iterator.hasNext()) {
			String codigoNew = iterator.next().toString();
			macetas.add(Maceta.builder().codigoNew(codigoNew).build());
		}
		return macetas;
	}

}
