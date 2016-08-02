package com.pduleba.loan.broker.route;

import org.apache.camel.builder.RouteBuilder;

public class ReadFormFileRouteBuilder extends RouteBuilder {

	private String from;
	private String to;

	public ReadFormFileRouteBuilder(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	@Override
	public void configure() throws Exception {
		from(from).log("Processing file ${file:name}").to(to);
	}

}
