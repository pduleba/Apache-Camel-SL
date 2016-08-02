package com.pduleba.loan.broker.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.spi.DataFormat;

import com.pduleba.loan.broker.xsd.CustInfo;

public class TutorialRouteBuilder extends RouteBuilder {

	private String from;

	public TutorialRouteBuilder(String from) {
		super();
		this.from = from;
	}

	@Override
	public void configure() throws Exception {
		DataFormat jaxbDataFormat = new JaxbDataFormat(CustInfo.class.getPackage().getName());
		
		getRouteWithHeader(jaxbDataFormat);
//		getRouteWithoutHeader(jaxbDataFormat);
	}

	private void getRouteWithHeader(DataFormat jaxbDataFormat) {
		from(from)
				.unmarshal(jaxbDataFormat)
				.log("This is body = ${body}")
				.setHeader("custNationalID", simple("${body.nationalID}"))
				.setHeader("firstName", simple("${body.firstName}"))
				.setHeader("lastName", simple("${body.lastName}"))
				.setHeader("age", simple("${body.age}"))
				.setHeader("occupation", simple("${body.occupation}"))
				.to("sql:delete from demo.CustInfo where nationalID = :#custNationalID; INSERT INTO demo.CustInfo (nationalID, firstName, lastName, age, occupation) values (:#custNationalID, :#firstName, :#lastName, :#age, :#occupation);");
	}

	@SuppressWarnings("unused")
	private void getRouteWithoutHeader(DataFormat jaxbDataFormat) {
		from(from)
				.unmarshal(jaxbDataFormat)
				.log("This is body = ${body}")
				.to("sql:delete from demo.CustInfo where nationalID = :#${body.nationalID}; INSERT INTO demo.CustInfo (nationalID, firstName, lastName, age, occupation) values (:#${body.nationalID}, :#${body.firstName}, :#${body.lastName}, :#${body.age}, :#${body.occupation});");
	}
	
}
