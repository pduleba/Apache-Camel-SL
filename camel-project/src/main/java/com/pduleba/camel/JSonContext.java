package com.pduleba.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pduleba.camel.restful.DeveloperRequest;

@Configuration
@ComponentScan(basePackages = "com.pduleba")
public class JSonContext extends CamelConfiguration {

	public static final String DATA_FORMAT_CAMEL_GSON_BEAN_ID = "json-gson"; // TRICK : Apache Camel Bean Id
	public static final String DATA_FORMAT_CAMEL_JACKSON_BEAN_ID = "json-jackson"; // TRICK : Apache Camel Bean Id
	public static final String DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID = "custom-jackson"; 
	
	@Bean(name = DATA_FORMAT_CAMEL_GSON_BEAN_ID) 
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public GsonDataFormat gson() {
		Gson gson = new GsonBuilder()
		
		.setPrettyPrinting()
		.serializeNulls().create();
		
		return new GsonDataFormat(gson, DeveloperRequest.class);
	}
	
	@Bean(name = DATA_FORMAT_CAMEL_JACKSON_BEAN_ID) 
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public JacksonDataFormat jackson() {
		ObjectMapper objectMapper = new ObjectMapper();
		
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		
	    return new JacksonDataFormat(objectMapper, DeveloperRequest.class);
	}
	
	@Bean(name = DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID) 
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public JacksonDataFormat custom() {
	    return new JacksonDataFormat(new ObjectMapper(), DeveloperRequest.class);
	}
	
	@Bean
	public RouteBuilder route() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				// overrriden camel gson
				from("direct:toJson_ByDefaultGson").marshal().json(JsonLibrary.Gson);
				from("direct:toPojo_ByDefaultGson").unmarshal().json(JsonLibrary.Gson);
				
				// overrriden camel jackson
				from("direct:toJson_ByDefaultJackson").marshal().json(JsonLibrary.Jackson);
				from("direct:toPojo_ByDefaultJackson").unmarshal().json(JsonLibrary.Jackson);
				
				// custom jackson
				from("direct:toJson_ByCustomJackson").marshal(DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID);
				from("direct:toPojo_ByCustomJackson").unmarshal(DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID);
			}
		};
	}
}
