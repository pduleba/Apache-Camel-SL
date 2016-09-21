package com.pduleba.config;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.pduleba.jaxrs.DeveloperRequest;
import com.pduleba.jaxrs.DeveloperResponse;

@Configuration
@PropertySource("classpath:application.properties")
public class CamelConfig extends CamelConfiguration {
	
	private static final Logger LOG = Logger.getLogger(CamelConfig.class);
	
	// Camel Default Bean Formatters IDS hidden AFAIK 
	// org.apache.camel.model.dataformat.JsonDataFormat.createDataFormat(RouteContext)
	public static final String DATA_FORMAT_JSON_BEAN_ID = "json-jackson"; // TRICK : Apache Camel Bean Id
	public static final String DATA_PROVIDER_JSON_BEAN_ID = "jsonProvider"; 

	public static final String JAXRS_BEAN_ID = "rsServer";
	public static final String CXFRS_ENDPOINT_ID = MessageFormat.format("cxfrs:bean:{0}", JAXRS_BEAN_ID);

	public static final String ROUTE_JAXRS_ID = "jaxrs-route"; 
	public static final String ROUTE_JAXRS = "jaxrsRoute"; 
	public static final String ROUTE_JSON = "jsonRoute"; 
	
	public static final String JACKSON_OBJECT_MAPPER = "jacksonObjectMapper"; 
	
	@Autowired
	@Qualifier(ROUTE_JAXRS)
	private RouteBuilder jaxrsRoute;
	
	@Autowired
	@Qualifier(ROUTE_JSON)
	private RouteBuilder jsonRoute;

	@Bean(name = JACKSON_OBJECT_MAPPER) 
	public ObjectMapper jacksonObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		
		return objectMapper;
	}
	
	@Bean(name = DATA_FORMAT_JSON_BEAN_ID) 
	public JacksonDataFormat jsonDataFormat(@Qualifier(JACKSON_OBJECT_MAPPER) ObjectMapper objectMapper) {
	    return new JacksonDataFormat(objectMapper, DeveloperRequest.class);
	}
	
	@Bean(name = DATA_PROVIDER_JSON_BEAN_ID)
	public JacksonJsonProvider jsonProvider(@Qualifier(JACKSON_OBJECT_MAPPER) ObjectMapper objectMapper) {
		return new JacksonJsonProvider(objectMapper);
	}

	@Bean(name = ROUTE_JAXRS)
	public RouteBuilder jaxrsRoute() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from(CXFRS_ENDPOINT_ID).routeId(ROUTE_JAXRS_ID)
						.log("BEFORE :: ${body}")
						.process(new Processor() {

							@Override
							public void process(Exchange exchange)
									throws Exception {
								LOG.info("Executing route processor logic");
								exchange.getOut()
										.setBody(
												new DeveloperResponse(200,
														"Successful processor result!"));
							}
						})
				.log("AFTER :: ${body}");
			}
		};
	}
	
	@Bean(name = ROUTE_JSON)
	public RouteBuilder jsonRoute() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				// should use bean by id CamelConfig.JACKSON_OBJECT_MAPPER
				from("direct:toJson_ByDefaultJackson").marshal().json(JsonLibrary.Jackson);
				from("direct:toPojo_ByDefaultJackson").unmarshal().json(JsonLibrary.Jackson);
			}
		};
	}

	@Override
	public List<RouteBuilder> routes() {
		return Arrays.asList(jaxrsRoute, jsonRoute);
	}
	
}
