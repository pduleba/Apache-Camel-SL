package com.pduleba.test.rest;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CamelSpringDelegatingTestContextLoaderTest.TestConfig.class}, 
loader = CamelSpringDelegatingTestContextLoader.class)
@MockEndpoints
public class CamelSpringDelegatingTestContextLoaderTest {
	
	@Produce(uri = "direct:test")
	protected ProducerTemplate testProducer;

	@Configuration
	public static class TestConfig extends SingleRouteCamelConfiguration {
		@Bean
		@Override
		public RouteBuilder route() {
			return new RouteBuilder() {
				@Override
				public void configure() throws Exception {
					from("direct:test").errorHandler(
							deadLetterChannel("direct:error")).to("direct:end");

					from("direct:error").log(
							"Received message on direct:error endpoint.");

					from("direct:end").log(
							"Received message on direct:end endpoint.");
				}
			};
		}
	}

	@Test
	public void testRoute() throws InterruptedException {
		testProducer.sendBody("<name>test</name>");
	}
}
// end::example[]