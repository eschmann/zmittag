package io.eschmann.zmittag.api;

import io.eschmann.zmittag.entities.Version;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;


@Path("info")
public class Info {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String info() {
		final Version version = new Version();
		version.setVersion("0.0.1");
		return new Gson().toJson(version);
	}
}
