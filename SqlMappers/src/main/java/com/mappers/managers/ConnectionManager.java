package com.mappers.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.commonsmodels.exceptions.SQLConnectionErrorException;
import com.mysql.jdbc.Driver;

@PropertySource("classpath:db.properties")
@Component
public class ConnectionManager {

	@Value("${db.url}")
	private String url;

	@Value("${db.user}")
	private String user;

	@Value("${db.password}")
	private String password;

	private static Connection conn;

	public Connection getConnection() {
		try {
			DriverManager.registerDriver(new Driver());
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println(e);
			throw new SQLConnectionErrorException("Error en la conexión con la B/D");
		}
		return conn;
	}

	public void closeConnection() {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			throw new SQLConnectionErrorException("Error en la conexión con la B/D");
		}
	}

}
