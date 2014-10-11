package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Restaurant;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.RestaurantDao;

import java.net.UnknownHostException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("restaurants")
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
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		final List<Restaurant> restaurants = this.restaurantDao.find().asList();
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(restaurants)).build();
	}
}
