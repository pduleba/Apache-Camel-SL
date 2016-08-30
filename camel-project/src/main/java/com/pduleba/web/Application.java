package com.pduleba.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.pduleba.camel.ApplicationContext;

/**
 * Main application configuration for the order fulfillment processor.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@Configuration
@ComponentScan(basePackageClasses = ApplicationContext.class)
@PropertySource("classpath:application.properties")
public class Application {

}
