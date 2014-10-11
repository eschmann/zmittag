package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Version;
import io.eschmann.zmittag.service.ServiceHelper;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("info")
@Produces(MediaType.APPLICATION_JSON)
public class InfoService {
	
	@GET
	public Response info() {
		final Version version = new Version();
		version.setVersion("0.0.1");
		
		return ServiceHelper.createOkResponseBuilder().entity(ServiceHelper.convertToJson(version)).build();
	}

}
