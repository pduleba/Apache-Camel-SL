package com.pduleba.context;

import static com.pduleba.context.ApplicationConfig.REST_BEAN_ID;

import java.text.MessageFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pduleba.camel.restful.DeveloperRequest;
import com.pduleba.camel.restful.DeveloperResponse;

@Configuration
@PropertySource("classpath:application.properties")
public class CamelConfig extends CamelConfiguration {

	// Camel Default Bean Formatters IDS hidden AFAIK 
	// org.apache.camel.model.dataformat.JsonDataFormat.createDataFormat(RouteContext)
	public static final String DATA_FORMAT_CAMEL_GSON_BEAN_ID = "json-gson"; // TRICK : Apache Camel Bean Id
	public static final String DATA_FORMAT_CAMEL_JACKSON_BEAN_ID = "json-jackson"; // TRICK : Apache Camel Bean Id
	public static final String DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID = "custom-jackson"; 

	public static final String CXFRS_ENDPOINT_ID = MessageFormat.format("cxfrs:bean:{0}", REST_BEAN_ID);
	public static final String MOCK_ENDPOINT_ID = "mock:result";

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
	public RouteBuilder rsRoute() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				
				from(CXFRS_ENDPOINT_ID).process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getOut().setBody(
								new DeveloperResponse(200, "Success on processor!"));
					}
				})
				.to(MOCK_ENDPOINT_ID);
			}
		};
	}
	
	@Bean
	public RouteBuilder jsonRoute() {
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
