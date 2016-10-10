package com.pduleba.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.pduleba.config.ApplicationCtx;
import com.pduleba.jaxrs.DeveloperResponse;

@Component(ApplicationCtx.JAXRS_ROUTE_BUILDER_ID)
public class JaxrsRouteBuilder extends RouteBuilder {

	private static final Logger LOG = Logger.getLogger(JaxrsRouteBuilder.class);

	@Override
	public void configure() throws Exception {
		from(ApplicationCtx.JAXRS_ENDPOINT_ID)
				.routeId(ApplicationCtx.JAXRS_ROUTE_ID)
				.log("-----------------").log("BEFORE :: ${body}")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						LOG.info("logic");
						exchange.getOut().setBody(
								new DeveloperResponse(200,
										"Successful processor result!"));
					}
				}).log("AFTER :: ${body}");
	}

}
