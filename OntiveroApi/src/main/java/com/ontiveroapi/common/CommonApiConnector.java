package com.ontiveroapi.common;

import java.io.IOException;

import com.commonsmodels.exceptions.ApiConnectionException;
import com.commonsmodels.exceptions.ApiConnectionRestException;
import com.commonsmodels.exceptions.InvalidRequestApiException;
import com.commonsmodels.exceptions.InvalidRequestApiRestException;

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
			System.out.println("Se realiza llamado http a API");
			System.out.println(request);
			response = httpClient.newCall(request).execute();
			if (!response.isSuccessful()) {
				if ("S".equals(request.header("isRest")))
					throw new InvalidRequestApiRestException("Error en la consulta a la API", response.code());
				else
					throw new InvalidRequestApiException("Error en la consulta a la API", response.code(),
							"apiConnectError");
			}

			ResponseBody body = response.body();
			strResponse = body.string();
			MediaType contentType = body.contentType();
			return response.newBuilder().body(ResponseBody.create(contentType, strResponse)).build().body().string();
		} catch (IOException e) {
			if ("S".equals(request.header("isRest")))
				throw new ApiConnectionRestException("Error de conexión con la API", e);
			else
				throw new ApiConnectionException("apiConnectError", "Error de conexión con la API");
		} finally {
			if (response != null)
				response.close();
		}
	}
}
