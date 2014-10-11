package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.PostedTag;
import io.eschmann.zmittag.entities.Tag;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.TagDao;
import io.eschmann.zmittag.service.ServiceHelper;

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

@Path("tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class TagService {

	private TagDao tagDao;

	public TagService() {
		try {
			this.tagDao = new TagDao(new ConnectionManager());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("list")
	public Response list() {
		final List<Tag> tags = this.tagDao.find().asList();
		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(tags)).build();
	}

	@POST
	@Path("add")
	public Response add(PostedTag newTag) {
		
		final Tag tag = new Tag(newTag);
		this.tagDao.save(tag);
		
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(tag)).build();
	}

}
