package com.pluralsight.processor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Main application configuration for the order fulfillment processor.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@Configuration
@ComponentScan(basePackageClasses = ApplicationConfig.class)
public class ApplicationConfig {

	@Configuration
	@Profile("standard")
	@PropertySource("classpath:order-fulfillment.properties")
	static class StandardProfile {

	}

	@Configuration
	@Profile("test")
	@PropertySource("classpath:order-fulfillment-test.properties")
	static class TestProfile {

	}

}
