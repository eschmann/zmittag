package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Group;
import io.eschmann.zmittag.entities.GroupAdd;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.GroupDao;

import java.net.UnknownHostException;
import java.util.List;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.query.UpdateResults;


@Path("groups")
@Stateless
public class GroupService {
	
	private GroupDao groupDao;
	
	public GroupService() {
		try {
			this.groupDao = new GroupDao(new ConnectionManager());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String index() {
		return "{\"type\": \"Group\"}";
	}
	
	@GET
	@Path("list")
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		final List<Group> groups = this.groupDao.find().asList();
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(groups)).build();
	}
	
	@POST
	@Path("join")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response join(GroupAdd groupAdd) {
		if(groupAdd == null || StringUtils.isBlank(groupAdd.getGroup()) || StringUtils.isBlank(groupAdd.getMember())) {
			return ServiceHelper.createFailedResponseBuilder().build();
		}
			
		final UpdateResults result = this.groupDao.addMemberToGroup(groupAdd.getGroup(), groupAdd.getMember());
		
		return ServiceHelper.createOkResponseBuilder().build();
	}

}
