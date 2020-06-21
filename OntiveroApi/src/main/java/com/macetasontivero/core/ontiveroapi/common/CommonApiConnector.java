package com.macetasontivero.ontiveroapi.common;

import java.io.IOException;

import com.macetasontivero.exceptions.ApiConnectionException;
import com.macetasontivero.exceptions.InvalidRequestApiException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class CommonApiConnector {

	private final OkHttpClient httpClient = new OkHttpClient();

	protected String makeCallStrResponse(Request request) {
		String strResponse = "";
		Response response = null;
		try {
			response = httpClient.newCall(request).execute();
			if (!response.isSuccessful())
				throw new InvalidRequestApiException("Error en la consulta a la API", response.code());
			ResponseBody body = response.body();
			strResponse = body.string();
			MediaType contentType = body.contentType();
			return response.newBuilder().body(ResponseBody.create(contentType, strResponse)).build().body().string();
		} catch (IOException e) {
			throw new ApiConnectionException("Error de conexión con la API");
		}
	}
}
