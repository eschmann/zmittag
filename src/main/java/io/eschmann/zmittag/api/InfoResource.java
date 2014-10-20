package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Saying;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;

@Path("/infos")
@Produces(MediaType.APPLICATION_JSON)
public class InfoResource {
	private final AtomicLong counter;
	
	public InfoResource() {
		this.counter = new AtomicLong();
	}

	@GET
	@Timed
	@Path("/saying")
	public Saying sayHello(@QueryParam("name") Optional<String> name) {
		return new Saying(counter.incrementAndGet(), name.or("anonymous"));
	}
	
	@GET
	@Path("/test")
	public Response test() {
		final Saying saying = new Saying(counter.incrementAndGet(), "anonymous");
		return Response.ok().entity(saying).build();
	}
}
