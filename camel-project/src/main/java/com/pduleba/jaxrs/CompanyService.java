package com.pduleba.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pduleba.config.ApplicationCtx;

@Path("/company")
@Service(ApplicationCtx.JAXRS_SERVICE_BEAN_ID)
public class CompanyService {

	private static final Logger LOG = Logger.getLogger(CompanyService.class);

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public DeveloperResponse save(DeveloperRequest developer) {
		LOG.info("Executing company resource logic");
		return new DeveloperResponse(200, "Successful resource result!");
	}
}
