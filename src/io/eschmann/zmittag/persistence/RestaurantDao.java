package io.eschmann.zmittag.persistence;

import io.eschmann.zmittag.entities.Restaurant;

import org.mongodb.morphia.dao.BasicDAO;

public class RestaurantDao extends BasicDAO<Restaurant, String> {

	private ConnectionManager connectionManager;

	public RestaurantDao(final ConnectionManager connectionManager) {
		super(Restaurant.class, connectionManager.getDataStore());
		this.connectionManager = connectionManager;
	}
	
	public Restaurant findOneRestaurant(final String id) {
		return findOne("_id", this.connectionManager.getObjectId(id));
	}
}
