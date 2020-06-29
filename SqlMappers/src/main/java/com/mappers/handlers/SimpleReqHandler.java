package com.macetasontivero.core.mappers.handlers;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;

import com.macetasontivero.core.commonsmodels.exceptions.SQLConnectionErrorException;
import com.macetasontivero.core.mappers.managers.ConnectionManager;

public abstract class SimpleReqHandler<T, Y> {

	@Autowired
	private ConnectionManager connMgr;

	protected abstract Y execute(Connection conn, T request);

	public Y runService(T body) {
		try {
			Y result = execute(connMgr.getConnection(), body);
			return result;
		} catch (SQLConnectionErrorException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			connMgr.closeConnection();
		}
	}

}
