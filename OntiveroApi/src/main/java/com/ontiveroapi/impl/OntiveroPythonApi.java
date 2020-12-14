package com.ontiveroapi.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.commonsmodels.json.JsonGenerator;
import com.commonsmodels.models.ColorGraph;
import com.commonsmodels.models.FormatoGraph;
import com.commonsmodels.models.FormatoMaceta;
import com.commonsmodels.models.FormatoPredictGraph;
import com.commonsmodels.models.Maceta;
import com.commonsmodels.models.Pedido;
import com.commonsmodels.models.PedidosEntregadosGraph;
import com.ontiveroapi.IOntiveroPythonApi;
import com.ontiveroapi.common.CommonApiConnector;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@PropertySource("classpath:python-api.properties")
@Component
public class OntiveroPythonApi extends CommonApiConnector implements IOntiveroPythonApi {

	private static final String TOKEN = "token";

	@Value("${api.url}")
	private String baseUrl;

	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	public List<Maceta> getRecomendaciones(Pedido pedidoSolicitado) {
		FormBody.Builder formBody = new FormBody.Builder();
		List<Maceta> listado = pedidoSolicitado.getListadoMacetas();
		for (int i = 0; i < listado.size(); i++) {
			formBody.add("codigoNew" + i, listado.get(i).getCodigoNew());
			formBody.add("cantSolicitada" + i, listado.get(i).getCantSolicitada().toString());
		}
		formBody.add("cantModelos", Integer.toString(listado.size()));
		Request request = new Request.Builder().url(baseUrl + "/getRecomendaciones").post(formBody.build())
				.header("isRest", "S").build();
		return makeListadoRecomendaciones(makeCallStrResponse(request));
	}

	@Override
	public String getPedidosEntregadosML(MultipartFile filePedidosML) {
		String fileName = filePedidosML.getName();
		File file = getFileFromMultipart(filePedidosML, fileName);
		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("pedidosEntregados", fileName,
						RequestBody.create(MediaType.parse("application/octet-stream"), file))
				.build();
		Request request = new Request.Builder().url(baseUrl + "/pedidosEntregadosML").post(requestBody)
				.header("isRest", "S").build();
		return makeCallStrResponse(request);
	}

	@Override
	public List<FormatoPredictGraph> getPrediccionesByFormato(List<FormatoGraph> pedidos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FormatoPredictGraph> getPrediccionesByFormatoMock(List<FormatoGraph> pedidos) {
		List<FormatoPredictGraph> dataByFormato = new ArrayList<>();
		Integer val = 1;
		for (FormatoMaceta formato : FormatoMaceta.values()) {
			dataByFormato.add(FormatoPredictGraph.builder().value(new BigDecimal(val++)).stdDev(new BigDecimal(10))
					.formato(formato).colorGraph(ColorGraph.getColorByCodMaceta(formato.getCode())).build());
		}
		return dataByFormato;
	}

	@Override
	public PedidosEntregadosGraph getPedidosEntregadosDB(String token) {
		Request request = new Request.Builder().url(baseUrl + "/getPedidosEntregadosDb").addHeader("isRest", "N")
				.addHeader("access-token", token).build();
		String response = makeCallStrResponse(request);
		return makePedidosDataGraph(response);
	}

	@Override
	public String getToken(String user) {
		Request request = new Request.Builder().url(baseUrl + "/getToken?id=" + user).build();
		String response = makeCallStrResponse(request);
		return getTokenFromJson(response);
	}

	private String getTokenFromJson(String response) {
		JSONObject dataResp = JsonGenerator.convertStringToObject(response);
		return dataResp.get(TOKEN).toString();
	}

	@SuppressWarnings("unchecked")
	private PedidosEntregadosGraph makePedidosDataGraph(String response) {
		JSONObject dataResp = JsonGenerator.convertStringToObject(response);
		List<FormatoGraph> formatosGraph = new ArrayList<>();
		for (int i = 0; i < FormatoMaceta.values().length; i++) {
			FormatoMaceta formato = FormatoMaceta.values()[i];
			List<Integer> values = new ArrayList<>();
			if (dataResp.containsKey(formato.getCode())) {
				Iterator<Integer> iterator = JsonGenerator
						.convertStringToJSONArray(dataResp.get(formato.getCode()).toString()).iterator();
				while (iterator.hasNext()) {
					Object cant = iterator.next();
					values.add(Integer.parseInt(cant.toString()));
				}
				formatosGraph.add(FormatoGraph.builder().formato(formato)
						.colorGraph(ColorGraph.getColorByCodMaceta(formato.getCode())).values(values).build());
			}
		}

		Iterator<String> iteratorFechas = JsonGenerator.convertStringToJSONArray(dataResp.get("fechas").toString())
				.iterator();
		List<Date> fechas = new ArrayList<>();
		while (iteratorFechas.hasNext()) {
			try {
				fechas.add(formatter.parse(iteratorFechas.next().toString()));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		return PedidosEntregadosGraph.builder().dataByFormato(formatosGraph).fechas(fechas).build();
	}

	@SuppressWarnings("unchecked")
	private List<Maceta> makeListadoRecomendaciones(String makeCallStrResponse) {
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
