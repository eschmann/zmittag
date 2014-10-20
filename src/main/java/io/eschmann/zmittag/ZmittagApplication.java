package io.eschmann.zmittag;

import java.net.UnknownHostException;
import java.util.List;

import com.sun.jersey.spi.container.ContainerResponseFilter;

import io.dropwizard.Application;
import io.dropwizard.Bundle;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.eschmann.zmittag.api.GroupResource;
import io.eschmann.zmittag.api.InfoResource;
import io.eschmann.zmittag.api.MemberResource;
import io.eschmann.zmittag.api.RestaurantResource;
import io.eschmann.zmittag.api.TagResource;
import io.eschmann.zmittag.filter.CrossDomainFilter;
import io.eschmann.zmittag.persistence.mongodb.ConnectionManager;
import io.eschmann.zmittag.persistence.mongodb.ManagedMongoDB;


public class ZmittagApplication extends Application<ZmittagConfiguration> {

	public static void main(String[] args) throws Exception {
		new ZmittagApplication().run(args);
	}

	@Override
	public String getName() {
		return "zmittag";
	}

	@Override
	public void initialize(Bootstrap<ZmittagConfiguration> bootstrap) {
		
		Bundle assetsBundle = new AssetsBundle("/assets", "/");
		bootstrap.addBundle(assetsBundle);
		
	}

	@Override
	public void run(ZmittagConfiguration configuration, Environment environment) throws UnknownHostException {
		
		final ConnectionManager manager = new ConnectionManager();
		manager.connect();

		final JerseyEnvironment jerseyEnvironment = environment.jersey();
		
		@SuppressWarnings("unchecked")
		final List<ContainerResponseFilter> containerResponseFilters = jerseyEnvironment.getResourceConfig().getContainerResponseFilters();
		final CrossDomainFilter crossDomainFilter = new CrossDomainFilter();
		containerResponseFilters.add(crossDomainFilter);
		
		final ManagedMongoDB managedMongoDB = new ManagedMongoDB(manager);
		environment.lifecycle().manage(managedMongoDB);
		
		jerseyEnvironment.setUrlPattern("/api/*");
		
		final InfoResource infoResource = new InfoResource();
		jerseyEnvironment.register(infoResource);
		
		final GroupResource groupResource = new GroupResource(manager);
		jerseyEnvironment.register(groupResource);
		
		final MemberResource memberResource = new MemberResource(manager);
		jerseyEnvironment.register(memberResource);
		
		final RestaurantResource restaurantResource = new RestaurantResource(manager);
		jerseyEnvironment.register(restaurantResource);
		
		final TagResource tagResource = new TagResource(manager);
		jerseyEnvironment.register(tagResource);
		
//		final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
//				configuration.getTemplate());
//		environment.healthChecks().register("template", healthCheck);
	}

}
