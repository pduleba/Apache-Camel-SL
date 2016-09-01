package com.pduleba.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

@Path("/company")
public class CompanyResource {

	private static final Logger LOG = Logger.getLogger(CompanyResource.class);

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DeveloperResponse save(DeveloperRequest developer) {
		LOG.info("Executing company resource logic");
		return new DeveloperResponse(200, "Successful resource result!");
	}
}
