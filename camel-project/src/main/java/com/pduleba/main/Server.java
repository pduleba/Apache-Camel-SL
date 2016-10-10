package com.pduleba.main;

import org.apache.camel.component.cxf.spring.SpringJAXRSServerFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import com.pduleba.config.ApplicationCtx;
import com.pduleba.jaxrs.CompanyService;
import com.pduleba.route.JaxrsRouteBuilder;

public class Server {

	public static final Logger LOG = Logger.getLogger(Server.class);

	public static void main(String[] args) throws InterruptedException {
		try (ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:META-INF/spring/camel-context.xml")) {
			initialize(ctx);
			run();
		}
	}

	private static void initialize(ConfigurableApplicationContext ctx) {
		Assert.notNull(ctx.getBean(ApplicationCtx.JAXRS_SERVER_BEAN_ID, SpringJAXRSServerFactoryBean.class), "Server must not be null");
		Assert.notNull(ctx.getBean(ApplicationCtx.JAXRS_SERVICE_BEAN_ID, CompanyService.class), "Service must not be null");
		Assert.notNull(ctx.getBean(ApplicationCtx.JAXRS_ROUTE_BUILDER_ID, JaxrsRouteBuilder.class), "Route builder must not be null");
	}

	private static void run() throws InterruptedException {
		LOG.info("Saver ready...");
		Thread.sleep(600*1000);
		LOG.info("Saver closing...");
		System.exit(0);
	}
}
