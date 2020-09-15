package com.mongo.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongo.managers.MongoConnectionManager;
import com.mongodb.client.MongoDatabase;

public abstract class MongoReqHandler<T, Y> {

	@Autowired
	private MongoConnectionManager mongoConnMgr;

	protected abstract Y execute(MongoDatabase mongoDb, T request);

	public Y runService(T body) {
		try {
			Y result = execute(mongoConnMgr.getDatabase(), body);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			mongoConnMgr.closeConnection();
		}
	}
}
