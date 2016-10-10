package com.pduleba.config;

import java.text.MessageFormat;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
public class ApplicationCtx {

	public static final String DATA_PROVIDER_JSON_BEAN_ID = "jsonProvider"; 

	public static final String JAXRS_SERVER_BEAN_ID = "jaxrsServer";
	public static final String JAXRS_SERVICE_BEAN_ID = "developerService";
	public static final String JAXRS_ROUTE_ID = MessageFormat.format("{0}Route", JAXRS_SERVICE_BEAN_ID); 
	public static final String JAXRS_ENDPOINT_ID = MessageFormat.format("cxfrs:bean:{0}?bindingStyle=SimpleConsumer", JAXRS_SERVER_BEAN_ID);
	public static final String JAXRS_ROUTE_BUILDER_ID = "jaxrsRouteBuilder"; 
	
	public static final String JACKSON_OBJECT_MAPPER = "jacksonObjectMapper"; 

	@Bean(name = JACKSON_OBJECT_MAPPER) 
	public ObjectMapper jacksonObjectMapper() {
		return new ObjectMapper();
	}
	
	@Bean(name = DATA_PROVIDER_JSON_BEAN_ID)
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
