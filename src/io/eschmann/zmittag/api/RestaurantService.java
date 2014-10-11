package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.PostedRating;
import io.eschmann.zmittag.entities.PostedRestaurant;
import io.eschmann.zmittag.entities.PostedTag;
import io.eschmann.zmittag.entities.Restaurant;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.RestaurantDao;
import io.eschmann.zmittag.service.ServiceHelper;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.query.UpdateResults;

@Path("restaurants")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class RestaurantService {

	private RestaurantDao restaurantDao;

	public RestaurantService() {
		try {
			this.restaurantDao = new RestaurantDao(new ConnectionManager());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("list")
	public Response list() {
		final List<Restaurant> restaurants = this.restaurantDao.find().asList();
		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(restaurants)).build();
	}

	@POST
	@Path("add")
	public Response add(PostedRestaurant newRestaurant) {

		Restaurant restaurant = new Restaurant(newRestaurant);

		this.restaurantDao.save(restaurant);

		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(restaurant)).build();
	}

	@POST
	@Path("locationSearch")
	public Response locationSearch(PostedRestaurant postedRestaurant) {

		final String searchTag = postedRestaurant.getSearchTag();

		List<Restaurant> foundRestaurants = null;

		if (StringUtils.isBlank(searchTag)) {
			foundRestaurants = this.restaurantDao.findNearestRestaurants(
					postedRestaurant.getLatitude(),
					postedRestaurant.getLongitude());
		} else {
			foundRestaurants = this.restaurantDao
					.findNearestRestaurantsWithTag(searchTag,
							postedRestaurant.getLatitude(),
							postedRestaurant.getLongitude());
		}

		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(foundRestaurants)).build();
	}

	@GET
	@Path("{id}/tags")
	public Response join(@PathParam("id") String restaurantId) {
		final Restaurant foundRestaurant = this.restaurantDao
				.findOneRestaurant(restaurantId);
		final Set<String> tags = foundRestaurant == null ? new HashSet<String>()
				: foundRestaurant.getTags();
		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(tags)).build();
	}

	@POST
	@Path("{id}/tags/add")
	public Response join(@PathParam("id") String restaurantId, PostedTag newTag) {
		final UpdateResults result = this.restaurantDao.addTagToRestaurant(
				restaurantId, newTag.getName());

		return ServiceHelper.createOkResponseBuilder().build();
	}

	@POST
	@Path("{id}/rating/add")
	public Response join(@PathParam("id") String restaurantId,
			PostedRating newRating) {
		final UpdateResults result = this.restaurantDao.addRatingToRestaurant(
				restaurantId, newRating.getRating());

		return ServiceHelper.createOkResponseBuilder().build();
	}

}
