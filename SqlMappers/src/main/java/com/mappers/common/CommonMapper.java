package com.mappers.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.commonsmodels.exceptions.SQLConnectionErrorException;

public abstract class CommonMapper {

	protected HashMap<String, String> simpleSelect(Connection conn, String query, String[] paramsIn, String[] outKeys) {
		List<HashMap<String, String>> result = executeQuery(conn, query, paramsIn, outKeys);
		if (result.size() > 1)
			throw new RuntimeException("Se esperaba un resultado, hubo " + result.size());
		else if (result.size() == 0 || result == null)
			return null;
		return result.get(0);
	}

	protected List<HashMap<String, String>> executeQuery(Connection conn, String query, String[] paramsIn,
			String[] outKeys) {
		List<HashMap<String, String>> result = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			loadParameters(ps, query, paramsIn);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result.add(getMapFromResultSet(rs, outKeys));
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			throw new SQLConnectionErrorException("Error al conectarse con la B/D");
		}
		return result;
	}

	protected void executeUpdate(Connection conn, String query, Object[] paramsIn) {
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			loadParameters(ps, query, paramsIn);
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			throw new SQLConnectionErrorException("Error al ejecutar update");
		}

	}

	private static HashMap<String, String> getMapFromResultSet(ResultSet rs, String[] outKeys) throws SQLException {
		HashMap<String, String> map = new HashMap<>();
		for (int i = 0; i < outKeys.length; i++) {
			String key = outKeys[i];
			try {
				map.put(key, rs.getString(key));
			} catch (Exception e) {
				System.out.println("Error al obtener parametro " + key);
				throw e;
			}
		}
		return map;
	}

	private static void loadParameters(PreparedStatement ps, String query, Object[] params) {
		if (params.length == 0)
			return;
		else {
			for (int i = 0; i < params.length; i++) {
				try {
					if (params[i].getClass().equals(String.class)) {
						ps.setString(i + 1, (String) params[i]);
					} else if (params[i].getClass().equals(FileInputStream.class)) {
						ps.setBlob(i + 1, (InputStream) params[i]);
					}
				} catch (SQLException e) {
					System.out.println("Error al insertar parametro " + params[i] + "al query.");
					e.printStackTrace();
				}
			}
		}
	}
}
