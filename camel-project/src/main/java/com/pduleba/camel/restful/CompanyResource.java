package com.pduleba.camel.restful;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



@Path("/company")
public class CompanyResource {

   
   @POST
   @Path("/save")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public DeveloperResponse save(DeveloperRequest developer) {
	   return new DeveloperResponse(200, "Success!");
   }
}
