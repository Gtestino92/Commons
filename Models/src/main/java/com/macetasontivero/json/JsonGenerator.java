package com.macetasontivero.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonGenerator {

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String convertObjectToJSON(Object object) {
		StringBuilder jsonStr = new StringBuilder();
		try {
			jsonStr.append(objectMapper.writeValueAsString(object));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Ocurrió un error inesperado.", e);
		}
		return jsonStr.toString();
	}

	public static JSONObject convertStringToObject(String string) {
		try {
			return (JSONObject) new JSONParser().parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Error de parseo a Json.", e);
		}
	}

	public static JSONArray convertStringToJSONArray(String string) {
		try {
			return (JSONArray) new JSONParser().parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Error de parseo a Json.", e);
		}
	}
}
