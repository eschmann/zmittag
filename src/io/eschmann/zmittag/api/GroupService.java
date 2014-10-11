package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Group;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.GroupDao;

import java.net.UnknownHostException;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;


@Path("groups")
@Stateless
public class GroupService {
	
	private GroupDao groupDao;
	private Gson gson;
	
	public GroupService() {
		try {
			this.groupDao = new GroupDao(new ConnectionManager());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.gson = new Gson();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String index() {
		return "{\"type\": \"Group\"}";
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public String list() {
		final List<Group> groups = this.groupDao.find().asList();
		return this.gson.toJson(groups);
	}

}
