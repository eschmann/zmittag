package io.eschmann.zmittag.service;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;

public class ServiceHelper {
	
	private static Gson GSON = new Gson();

	public static ResponseBuilder createFailedResponseBuilder() {
		final ResponseBuilder builder = Response.status(Response.Status.BAD_REQUEST);
		return builder;
	}

	public static ResponseBuilder createOkResponseBuilder() {
		final ResponseBuilder builder = Response.status(Response.Status.OK);
		return builder;
	}

	public static String convertToJson(final Object entity) {
		return GSON.toJson(entity);
	}

}
