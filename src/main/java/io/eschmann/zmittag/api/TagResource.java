package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.PostedTag;
import io.eschmann.zmittag.entities.Tag;
import io.eschmann.zmittag.persistence.mongodb.ConnectionManager;
import io.eschmann.zmittag.persistence.mongodb.TagDao;
import io.eschmann.zmittag.service.ServiceHelper;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

@Path("tags")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagResource {

	private TagDao tagDao;

	public TagResource(final ConnectionManager connectionManager) {
		this.tagDao = new TagDao(connectionManager);
	}

	@GET
	@Path("list")
	public Response list() {
		final List<Tag> tags = this.tagDao.orderedList();
		return ServiceHelper.createOkResponseBuilder()
				.entity(ServiceHelper.convertToJson(tags)).build();
	}

	@POST
	@Path("add")
	public Response add(PostedTag newTag) {
		
		final String tagName = newTag.getName();
		
		if(StringUtils.isBlank(tagName)) {
			return ServiceHelper.createOkResponseBuilder().build();
		}
		
		final Tag tag = this.tagDao.addTagIfNotExist(tagName);
		
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(tag)).build();
	}

}
