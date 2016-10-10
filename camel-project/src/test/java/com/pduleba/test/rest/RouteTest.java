package com.pduleba.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.Assert;

import com.pduleba.config.ApplicationCtx;
import com.pduleba.jaxrs.DeveloperRequest;
import com.pduleba.jaxrs.DeveloperResponse;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(
		value = "classpath:META-INF/spring/camel-context.xml",
        loader = CamelSpringDelegatingTestContextLoader.class)
public class RouteTest {

	public static final Logger LOG = Logger.getLogger(RouteTest.class);

	private MockEndpoint result;

	@Produce(uri = "http4://localhost:9000/rest/api/company/save")
	private ProducerTemplate browser;
	
	@Autowired
	private ModelCamelContext context;
	@Autowired @Qualifier(ApplicationCtx.JACKSON_OBJECT_MAPPER) 
	private ObjectMapper objectMapper;
	
	private DeveloperRequest request;
	private Object requestJson;
	
	@Before
	public void before() throws Exception {
		this.request = DeveloperRequest.getRequest();
		this.requestJson = serialize(request);
		
		Assert.notNull(context);
		
		final String MOCK_ENDPOINT_ID = "mock:result";
		context.getRouteDefinition(ApplicationCtx.JAXRS_ROUTE_ID).adviceWith(
				context, new AdviceWithRouteBuilder() {
					@Override
					public void configure() throws Exception {
						// add mock to the end of the route
						weaveAddLast().to(MOCK_ENDPOINT_ID);
					}
				});
        result = context.getEndpoint(MOCK_ENDPOINT_ID, MockEndpoint.class);
	}

	@Test
	public void testPostRequest() throws Exception {
		
		// 1 - Mock endpoint expects one message
		result.expectedMessageCount(1);
		
		// 2 - Send the XML as the body of the message through the route
		browser.sendBodyAndHeaders(requestJson , getHeaders());

		// 3 - Waits ten seconds to see if the expected number of messages have been received
		result.assertIsSatisfied();

		// 4 - extract response from exchange.
		DeveloperResponse response = extractResponse(result.getExchanges().get(0));
		assertEquals(200, response.getResponseCode());
	}

	public <T> String serialize(T in) {
		try {
			return objectMapper.writeValueAsString(in);
		} catch (IOException e) {
			LOG.error("Unable to serialize", e);
			return null;
		}
	}
	
	private Map<String, Object> getHeaders() {
		Map<String, Object> headers = new HashMap<>();
		headers.put(Exchange.HTTP_METHOD, "POST");
		headers.put(Exchange.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		return headers;
	}

	private DeveloperResponse extractResponse(Exchange exchange) throws IOException {
		assertNotNull(exchange);
		Message inMessage = exchange.getIn();
		return inMessage.getBody(DeveloperResponse.class);
	}
}
