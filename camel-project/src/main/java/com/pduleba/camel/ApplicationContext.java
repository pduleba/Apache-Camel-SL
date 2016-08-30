package com.pduleba.camel;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.pduleba.camel.restful.CompanyResource;
import com.pduleba.camel.restful.DeveloperResponse;

@Configuration
@Import(JSonContext.class)
public class ApplicationContext extends CamelConfiguration {

	@Bean
	public JaxRsApiApplication jaxRsApiApplication() {
		return new JaxRsApiApplication();
	}

	@Bean(destroyMethod = "shutdown")
	public SpringBus cxf() {
		return new SpringBus();
	}

	@Bean
	public CompanyResource companyResource() {
		return new CompanyResource();
	}
	
	@Bean
	public JAXRSServerFactoryBean rsServer(CompanyResource companyResource) {
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance()
				.createEndpoint(jaxRsApiApplication(),
						JAXRSServerFactoryBean.class);
		factory.setServiceBean(companyResource);
		factory.setAddress('/' + factory.getAddress());
//		factory.setProviders(Arrays.<Object> asList(jsonProvider()));

		return factory;
	}

	@Bean
	public RouteBuilder route() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				final String fromURI = "cxfrs:bean:rsServer";

				from(fromURI).log("Body = ${body}").process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						exchange.getOut().setBody(
								new DeveloperResponse(200, "Success!"));
					}
				});
			}
		};
	}

}
