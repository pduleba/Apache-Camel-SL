package com.pduleba.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath( JaxRsApiApplication.APPLICATION_PATH )
public class JaxRsApiApplication extends Application {
	public static final String APPLICATION_PATH = "api";
	
}
