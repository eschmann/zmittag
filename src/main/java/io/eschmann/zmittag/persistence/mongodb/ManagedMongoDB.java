package io.eschmann.zmittag.persistence.mongodb;

import com.mongodb.MongoClient;
import io.dropwizard.lifecycle.Managed;

public class ManagedMongoDB implements Managed {

	private ConnectionManager connectionManager;

	public ManagedMongoDB(final ConnectionManager manager) {
		this.connectionManager = manager;
	}

	@Override
	public void start() throws Exception {
		//connection is already started
	}

	@Override
	public void stop() throws Exception {
		if (this.connectionManager == null) {
			return;
		}

		final MongoClient client = this.connectionManager.getMongoDbClient();
		if (client != null) {
			client.close();
		}

	}

}
