package com.pduleba.camel.restful;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


/**
 * Main resource service provider for orders. Services are available through the
 * path /orderFulfillment.
 * 
 * @author Michael Hoffman, Pluralsight
 *
 */
@Path("/company")
public class CompanyResource {

   /**
    * Processing an order request. Simply writes out the order request to
    * System.err and then returns a fulfillment response object with the status
    * code 200 and status as success.
    * 
    * @param developer
    * @return
    */
   @POST
   @Path("/save")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   public DeveloperResponse save(DeveloperRequest developer) {
	   return new DeveloperResponse(200, "Success!");
   }
}
