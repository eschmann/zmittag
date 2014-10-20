package io.eschmann.zmittag.persistence.mongodb;

import io.eschmann.zmittag.entities.Restaurant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

public class RestaurantDao extends BasicDAO<Restaurant, String> {

	private ConnectionManager connectionManager;

	public RestaurantDao(final ConnectionManager connectionManager) {
		super(Restaurant.class, connectionManager.getDataStore());
		this.connectionManager = connectionManager;

		getCollection().createIndex(new BasicDBObject("location", "2d"));

		final BasicDBObject textSearchIndex = new BasicDBObject("name", "text");
		textSearchIndex.put("description", "text");
		textSearchIndex.put("category", "text");
		getCollection().createIndex(textSearchIndex);
	}

	public Restaurant findOneRestaurant(final String id) {
		return findOne("_id", this.connectionManager.getObjectId(id));
	}

	public Restaurant findByName(final String name) {
		return findOne("name", name);
	}

	public List<Restaurant> searchByName(final String name) {
		final DBObject textSearchCommand = new BasicDBObject();
		textSearchCommand.put("text", "restaurants");
		textSearchCommand.put("search", name);
		final CommandResult commandResult = getDatastore().getDB().command(
				textSearchCommand);

		final List<Restaurant> list = new ArrayList<Restaurant>();

		final Object results = commandResult.get("results");
		if (results != null) {
			BasicDBList rows = (BasicDBList) results;
			for (Object row : rows) {
				final BasicDBObject result = (BasicDBObject) row;
				final BasicDBObject obj = (BasicDBObject) result.get("obj");
				if (obj != null) {
					final Restaurant restaurant = new Restaurant(obj);
					list.add(restaurant);
				}
			}
		}

		return list;
	}

	public List<Restaurant> findBest3LeftByRating(
			final List<String> alreadyChoosen) {
		final Query<Restaurant> query = createQuery();
		if(!alreadyChoosen.isEmpty()) {
			query.field("name").notIn(alreadyChoosen);
		}
		query.order("-averageRating").limit(3);
		final List<Restaurant> foundRestaurants = find(query).asList();
		return foundRestaurants;
	}

	public List<Restaurant> findByTag(final String searchTag) {
		final Query<Restaurant> query = createQuery().field("tags").contains(
				searchTag);
		final List<Restaurant> foundRestaurants = find(query).asList();
		return foundRestaurants;
	}

	public UpdateResults addTagToRestaurant(final String restaurantId,
			final String newTag) {
		final ObjectId oid = this.connectionManager.getObjectId(restaurantId);
		final Query<Restaurant> query = createQuery().field("_id").equal(oid);
		final UpdateOperations<Restaurant> update = createUpdateOperations()
				.add("tags", newTag);
		final UpdateResults result = update(query, update);
		return result;
	}

	public UpdateResults addRatingToRestaurant(final String restaurantId,
			final double newRating) {
		final Restaurant restaurant = findOneRestaurant(restaurantId);
		if (restaurant == null) {
			return null;
		}

		final BigDecimal ratingCount = new BigDecimal(
				restaurant.getRatingCount());
		final BigDecimal ratingPoints = ratingCount.multiply(new BigDecimal(
				restaurant.getAverageRating()));
		final BigDecimal increasedRating = ratingPoints.add(new BigDecimal(
				newRating));
		final BigDecimal newRatingCount = new BigDecimal(
				ratingCount.intValue() + 1);
		final BigDecimal newAverage = increasedRating.divide(newRatingCount);

		final ObjectId oid = this.connectionManager.getObjectId(restaurantId);
		final Query<Restaurant> query = createQuery().field("_id").equal(oid);
		final UpdateOperations<Restaurant> update = createUpdateOperations()
				.set("ratingCount", newRatingCount.longValue()).set(
						"averageRating", newAverage.doubleValue());

		final UpdateResults result = update(query, update);
		return result;
	}

	public List<Restaurant> findNearestRestaurants(final double latitude,
			final double longitude) {
		final Query<Restaurant> query = createQuery().field("location").near(
				latitude, longitude);
		final List<Restaurant> foundRestaurants = find(query).asList();

		return foundRestaurants;
	}

	public List<Restaurant> findNearestRestaurantsWithTag(final String tag,
			final double latitude, final double longitude) {

		final Query<Restaurant> query = createQuery().field("tags")
				.contains(tag).field("location").near(latitude, longitude);
		final List<Restaurant> foundRestaurants = find(query).asList();

		return foundRestaurants;
	}
}
