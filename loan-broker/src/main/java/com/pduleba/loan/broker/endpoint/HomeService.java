package com.pduleba.loan.broker.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/homeService")
public interface HomeService {

	@GET
	@Path("/details/{nationalID}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSummary(@PathParam("nationalID") String nationalID);
	
}
