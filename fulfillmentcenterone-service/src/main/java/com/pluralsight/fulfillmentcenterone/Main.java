package com.pluralsight.fulfillmentcenterone;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Main runner class for the restful web service. This can be easily run from a
 * command line after it has been packaged. Its recommend that if you have
 * Maven, run the command mvn exec:java to run the service.
 * 
 * NOTE: If you run into an issue with port conflicts, simply change the port
 * found in the static member BASE_URI on this class.
 * 
 * @author Michael Hoffman, Pluralsight
 *
 */
public class Main {
   // Base URI the Grizzly HTTP server will listen on
   public static final String BASE_URI = "http://localhost:8090/services/";

   /**
    * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
    * application.
    * 
    * @return Grizzly HTTP server.
    */
   public static HttpServer startServer() {
      // create a resource config that scans for JAX-RS resources and providers
      // in com.pluralsight.fulfillmentcenterone package
      final ResourceConfig rc =
            new ResourceConfig()
                  .packages("com.pluralsight.fulfillmentcenterone");

      // create and start a new instance of grizzly http server
      // exposing the Jersey application at BASE_URI
      return GrizzlyHttpServerFactory
            .createHttpServer(URI.create(BASE_URI), rc);
   }

   /**
    * Main execution method. This will start the Grizzly HTTP server and then
    * wait until an input is made to the command window before it shuts down.
    * 
    * @param args
    * @throws IOException
    */
   public static void main(String[] args) throws IOException {
      final HttpServer server = startServer();
      System.out.println(String.format(
            "Jersey app started with WADL available at "
                  + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
      System.in.read();
      server.stop();
   }
}
