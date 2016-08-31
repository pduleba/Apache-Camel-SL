package com.pduleba.context;

import static com.pduleba.context.CamelConfig.DATA_FORMAT_CAMEL_GSON_BEAN_ID;
import static com.pduleba.context.CamelConfig.DATA_FORMAT_CAMEL_JACKSON_BEAN_ID;
import static com.pduleba.context.CamelConfig.DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID;

import java.util.Arrays;

import org.apache.camel.CamelContext;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.pduleba.camel.restful.CompanyResource;
import com.pduleba.camel.restful.JsonService;
import com.pduleba.web.JaxRsApiApplication;

@Configuration
@PropertySource("classpath:application.properties")
@Import(CamelConfig.class)
public class ApplicationConfig extends CamelConfiguration {

	public final static String REST_BEAN_ID = "rsServer";

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
	public JacksonJsonProvider jsonProvider() {
		return new JacksonJsonProvider();
	}

	@Bean(name = REST_BEAN_ID)
	@DependsOn("cxf")
	public JAXRSServerFactoryBean rsServer(CompanyResource companyResource,
			JacksonJsonProvider jsonProvider,
			JaxRsApiApplication jaxRsApiApplication, SpringBus cxf) {
		JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
		
		factory.setBus(cxf);
		factory.setServiceBeans(Arrays.<Object>asList(companyResource));
		factory.setAddress("http://localhost:9000/");
		factory.setProviders(Arrays.<Object>asList(jsonProvider));
		
		return factory;
	}


	@Bean
	public JsonService jsonService(
			@Qualifier(DATA_FORMAT_CAMEL_GSON_BEAN_ID) GsonDataFormat gson,
			@Qualifier(DATA_FORMAT_CAMEL_JACKSON_BEAN_ID) JacksonDataFormat jackson,
			@Qualifier(DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID) JacksonDataFormat custom,
			CamelContext camelContext) {
		return new JsonService(gson, jackson, custom, camelContext);
	}
}
