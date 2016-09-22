package com.pduleba.config;

import static com.pduleba.config.CamelConfig.DATA_PROVIDER_JSON_BEAN_ID;

import java.util.Arrays;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.pduleba.jaxrs.CompanyResource;

@Configuration
@Import(CamelConfig.class)
@ImportResource("classpath:META-INF/cxf/cxf.xml") // TRICK : use CXF Bus bean definition
@PropertySource("classpath:application.properties")
public class ApplicationConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = CamelConfig.JAXRS_BEAN_ID)
	@DependsOn("cxf")
	public JAXRSServerFactoryBean rsServer(
			@Qualifier(DATA_PROVIDER_JSON_BEAN_ID) JacksonJsonProvider jsonProvider,
			SpringBus cxf) {
		JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();

		factory.setBus(cxf);
		factory.setServiceBeans(Arrays.<Object> asList(new CompanyResource()));
		factory.setAddress("http://localhost:9000/api");
		factory.setProviders(Arrays.<Object> asList(jsonProvider));

		return factory;
	}
}
