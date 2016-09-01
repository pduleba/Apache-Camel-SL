package com.pduleba.config;

import static com.pduleba.config.ApplicationConfig.REST_BEAN_ID;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pduleba.jaxrs.DeveloperRequest;
import com.pduleba.jaxrs.DeveloperResponse;

@Configuration
@PropertySource("classpath:application.properties")
public class CamelConfig extends CamelConfiguration {
	
	private static final Logger LOG = Logger.getLogger(CamelConfig.class);
	
	// Camel Default Bean Formatters IDS hidden AFAIK 
	// org.apache.camel.model.dataformat.JsonDataFormat.createDataFormat(RouteContext)
	public static final String DATA_FORMAT_CAMEL_GSON_BEAN_ID = "json-gson"; // TRICK : Apache Camel Bean Id
	public static final String DATA_FORMAT_CAMEL_JACKSON_BEAN_ID = "json-jackson"; // TRICK : Apache Camel Bean Id
	public static final String DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID = "custom-jackson"; 
	
	public static final String CXFRS_ROUTE_ID = "cxfrs-route"; 
	
	public static final String DATA_FORMAT_DEFAULT_CXF_PROVIDER_BEAN_ID = "default-cxf-provider"; 
	public static final String DATA_FORMAT_JACKSON_CXF_PROVIDER_BEAN_ID = "jackson-cxf-provider"; 

	public static final String CXFRS_ENDPOINT_ID = MessageFormat.format("cxfrs:bean:{0}", REST_BEAN_ID);

	@Bean(name = DATA_FORMAT_CAMEL_GSON_BEAN_ID) 
	public GsonDataFormat gson() {
		Gson gson = new GsonBuilder()
		
		.setPrettyPrinting()
		.serializeNulls().create();
		
		return new GsonDataFormat(gson, DeveloperRequest.class) {
			@Override
			public void marshal(Exchange exchange, Object graph,
					OutputStream stream) throws Exception {
				LOG.info(">>>> Custom marshal data format logic called");
				super.marshal(exchange, graph, stream);
			}
			
			@Override
			public Object unmarshal(Exchange exchange, InputStream stream)
					throws Exception {
				LOG.info(">>>> Custom unmarshal data format logic called");
				return super.unmarshal(exchange, stream);
			}
		};
	}
	
	@Bean(name = DATA_FORMAT_CAMEL_JACKSON_BEAN_ID) 
	public JacksonDataFormat jackson() {
		ObjectMapper objectMapper = new ObjectMapper();
		
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		
	    return new JacksonDataFormat(objectMapper, DeveloperRequest.class);
	}
	
	@Bean(name = DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID) 
	public JacksonDataFormat customJackson() {
	    return new JacksonDataFormat(new ObjectMapper(), DeveloperRequest.class);
	}
	
	// ### Providers ###
	@Bean(name = DATA_FORMAT_DEFAULT_CXF_PROVIDER_BEAN_ID) 
	public JSONProvider<Object> cxfDefault() {
		return new JSONProvider<>();
	}
	
	@Bean(name = DATA_FORMAT_JACKSON_CXF_PROVIDER_BEAN_ID)
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}

	@Bean
	public RouteBuilder rsRoute(@Value("${camel.performInvocation}") boolean performInvocation) {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				
				String fromUri = getRouteUri(performInvocation);
				from(fromUri).routeId(CXFRS_ROUTE_ID)
						.log("BODY BEFORE PROCESSING = ${body}")
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
				.log("BODY AFTER PROCESSING = ${body}");
			}

			private String getRouteUri(boolean performInvocation) {
				return MessageFormat.format(
						"{0}?performInvocation={1}", CXFRS_ENDPOINT_ID, performInvocation);
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
