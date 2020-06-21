package com.macetasontivero.ontiveroapi.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.macetasontivero.json.JsonGenerator;
import com.macetasontivero.models.Maceta;
import com.macetasontivero.models.Pedido;
import com.macetasontivero.ontiveroapi.IOntiveroPythonApi;
import com.macetasontivero.ontiveroapi.common.CommonApiConnector;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

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

	@Override
	public String getPedidosEntregadosML(MultipartFile filePedidosML) {
		String fileName = filePedidosML.getName();
		File file = getFileFromMultipart(filePedidosML, fileName);
		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("pedidosEntregados", fileName,
						RequestBody.create(MediaType.parse("application/octet-stream"), file))
				.build();
		Request request = new Request.Builder().url(baseUrl + "/pedidosEntregadosML").post(requestBody).build();
		return makeCallStrResponse(request);
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

	private File getFileFromMultipart(MultipartFile filePedidosML, String fileName) {
		File file = new File("pedidosFile");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			fos.write(filePedidosML.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
}
