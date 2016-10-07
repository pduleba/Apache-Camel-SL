package com.pduleba.main;

import static java.text.MessageFormat.format;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import com.pduleba.config.ApplicationConfig;
import com.pduleba.config.CamelConfig;

public class Main {

	public static final Logger LOG = Logger.getLogger(Main.class);

	private static JAXRSServerFactoryBean serviceServerFactory;

	public static void main(String[] args) throws InterruptedException {
		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(
				ApplicationConfig.class)) {
			initialize(ctx);
			run();
		} finally {
			System.exit(0);
		}
	}

	private static void initialize(ConfigurableApplicationContext ctx) {
		try {
			LOG.info("Initializing...");
			serviceServerFactory = ctx.getBean(CamelConfig.JAXRS_BEAN_ID, JAXRSServerFactoryBean.class);
			Assert.notNull(serviceServerFactory, "ServiceFactory must not be null");
			
	        Server server = new Server(8080);
	           
	        // Register and map the dispatcher servlet
	        final ServletHolder servletHolder = new ServletHolder( new CXFServlet() );
	        final ServletContextHandler context = new ServletContextHandler();  
	        context.setContextPath( "/" );
	        context.addServlet( servletHolder, "/*" ); 
	        context.addEventListener( new ContextLoaderListener() );
	        context.setInitParameter( "contextClass", AnnotationConfigWebApplicationContext.class.getName() );
	        context.setInitParameter( "contextConfigLocation", ApplicationConfig.class.getName() );
	        server.setHandler( context );
	        server.start();
	        server.join();
			LOG.info("Initializing...Complete");
		} catch (Exception e) {
			LOG.info(format("Initializing...Error :: {0}", e.getMessage()));
			throw new RuntimeException(e);
		}
	}

	private static void run() throws InterruptedException {
		LOG.info("Running...");
		Thread.sleep(600 * 1000);
		LOG.info("Running...Complete");
	}
}
