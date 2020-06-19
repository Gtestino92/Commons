package com.macetasontivero.mongo.managers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

@PropertySource("classpath:mongo.db.properties")
@Component
public class MongoConnectionManager {

	@Value("${mongo.db.uri}")
	private String uri;

	@Value("${mongo.db.name}")
	private String dbName;

	private MongoClient mongoClient;

	public MongoDatabase getDatabase() {
		MongoClientURI clientURI = new MongoClientURI(uri);
		this.mongoClient = new MongoClient(clientURI);
		return mongoClient.getDatabase(dbName);
	}

	public void closeConnection() {
		this.mongoClient.close();
	}

}
