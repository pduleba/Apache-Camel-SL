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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.pduleba.config.ApplicationConfig;
import com.pduleba.config.CamelConfig;
import com.pduleba.jaxrs.DeveloperRequest;
import com.pduleba.jaxrs.DeveloperResponse;
import com.pduleba.service.JsonService;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {ApplicationConfig.class},
        loader = CamelSpringDelegatingTestContextLoader.class)
public class RestRouteTest {

	private MockEndpoint result;

	@Produce(uri = "http4://localhost:9000/api/company/save")
	private ProducerTemplate browser;
	
	@Autowired
	private JsonService jsonService;
	@Autowired
	private ModelCamelContext context;
	
	private DeveloperRequest request;
	private Object requestJson;
	
	@Before
	public void before() throws Exception {
		this.request = DeveloperRequest.getRequest();
		this.requestJson = jsonService.serializeByJacksonProvider(request, DeveloperRequest.class);
		
		final String MOCK_ENDPOINT_ID = "mock:result";
		context.getRouteDefinition(CamelConfig.ROUTE_JAXRS_ID).adviceWith(
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
