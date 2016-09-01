package com.pduleba.main;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;

import com.pduleba.config.ApplicationConfig;

public class Server {

	public static final Logger LOG = Logger.getLogger(Server.class);

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
		LOG.info("Saver ready...");
		Thread.sleep(600*1000);
		LOG.info("Saver closing...");
		System.exit(0);
	}
}
