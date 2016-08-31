package com.pduleba.main;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import com.pduleba.config.ApplicationConfig;

public class MainRunner {

	private static JAXRSServerFactoryBean serviceServerFactory;

	public static void main(String[] args) throws InterruptedException {
		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
				ApplicationConfig.class)) {
			initialize(ctx);
			run();
		}
	}

	private static void initialize(ConfigurableApplicationContext ctx) {
		serviceServerFactory = ctx.getBean(ApplicationConfig.REST_BEAN_ID, JAXRSServerFactoryBean.class);
		Assert.notNull(serviceServerFactory, "ServiceFactory must not be null");
	}

	private static void run() throws InterruptedException {
		Server server = serviceServerFactory.create();
		server.start();
		Thread.sleep(60*1000);
		server.stop();
	}
}
