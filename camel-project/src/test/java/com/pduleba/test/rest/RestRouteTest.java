package com.pduleba.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pduleba.camel.ApplicationContext;
import com.pduleba.camel.restful.DeveloperRequest;
import com.pduleba.camel.restful.DeveloperResponse;
import com.pduleba.camel.restful.JsonService;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = {ApplicationContext.class},
        loader = CamelSpringDelegatingTestContextLoader.class)
//@MockEndpoints
public class RestRouteTest {

	@EndpointInject(uri = "mock:direct:end")
	private MockEndpoint resultEndpoint;

	@Produce(uri = "http4://localhost:9000/api/company/save")
	private ProducerTemplate testProducer;
	
	@Autowired
	private JsonService jsonService;
	
	private DeveloperRequest request;

	
	@Before
	public void before() throws JsonProcessingException {
		this.request = DeveloperRequest.getRequest();
	}
	
	@Test
	public void test_success() throws Exception {
		
		// 1 - Mock endpoint expects one message
		resultEndpoint.expectedMessageCount(1);

		// 2 - Send the XML as the body of the message through the route
		testProducer.sendBodyAndHeader(request, Exchange.HTTP_METHOD, "POST");
		// 3 - Waits ten seconds to see if the expected number of messages have
		// been received
		resultEndpoint.assertIsSatisfied();

		// 4 - Get the exchanges. Our exchange should be the first.
		Exchange exchange = resultEndpoint.getExchanges().get(0);
		assertNotNull(exchange);
		Message inMessage = exchange.getIn();
		DeveloperResponse response = jsonService.deserializeByCustomJackson(
				inMessage.getBody(String.class), DeveloperResponse.class);
		assertEquals(200, response.getResponseCode());
	}
}
