package io.eschmann.zmittag.persistence;

import io.eschmann.zmittag.Constants;

import java.net.UnknownHostException;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class ConnectionManager {
	
	private MongoClient mongoDbClient;
	private Morphia morphia;
	private Datastore dataStore;
	
	public ConnectionManager() throws UnknownHostException {
		this.mongoDbClient = new MongoClient(Constants.DB_SERVER, Constants.DB_PORT);
		this.morphia = new Morphia();
		this.dataStore = morphia.createDatastore(mongoDbClient, Constants.DB_NAME);
	}

	public MongoClient getMongoDbClient() {
		return mongoDbClient;
	}

	public void setMongoDbClient(MongoClient mongoDbClient) {
		this.mongoDbClient = mongoDbClient;
	}

	public Morphia getMorphia() {
		return morphia;
	}

	public void setMorphia(Morphia morphia) {
		this.morphia = morphia;
	}

	public Datastore getDataStore() {
		return dataStore;
	}

	public void setDataStore(Datastore dataStore) {
		this.dataStore = dataStore;
	}
		
	
}
