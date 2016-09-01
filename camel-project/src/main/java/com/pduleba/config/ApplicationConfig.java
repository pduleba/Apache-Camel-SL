package com.pduleba.config;

import static com.pduleba.config.CamelConfig.DATA_FORMAT_CAMEL_GSON_BEAN_ID;
import static com.pduleba.config.CamelConfig.DATA_FORMAT_CAMEL_JACKSON_BEAN_ID;
import static com.pduleba.config.CamelConfig.DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID;
import static com.pduleba.config.CamelConfig.DATA_FORMAT_DEFAULT_CXF_PROVIDER_BEAN_ID;
import static com.pduleba.config.CamelConfig.DATA_FORMAT_JACKSON_CXF_PROVIDER_BEAN_ID;

import java.util.Arrays;

import org.apache.camel.CamelContext;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.pduleba.jaxrs.CompanyResource;
import com.pduleba.jaxrs.JaxRsApiApplication;
import com.pduleba.service.JsonService;

@Configuration
@PropertySource("classpath:application.properties")
@Import(CamelConfig.class)
public class ApplicationConfig extends CamelConfiguration {

	public final static String REST_BEAN_ID = "rsServer";
	
	@Bean 
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
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
	public Swagger2Feature swagger2feature() {
		Swagger2Feature swagger2Feature = new Swagger2Feature();
		
		return swagger2Feature;
	}
	
	@Bean(name = REST_BEAN_ID)
	@DependsOn("cxf")
	public JAXRSServerFactoryBean rsServer(CompanyResource companyResource,
			@Qualifier(DATA_FORMAT_DEFAULT_CXF_PROVIDER_BEAN_ID) JSONProvider<Object> defaultProvider,
			@Qualifier(DATA_FORMAT_JACKSON_CXF_PROVIDER_BEAN_ID) JacksonJsonProvider jacksonProvider,
			Swagger2Feature swagger2feature,
			JaxRsApiApplication jaxRsApiApplication, SpringBus cxf,
			@Value("${use.jackson.provider}") boolean useJacksonProvider) {
		JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
		
		factory.setBus(cxf);
		factory.setServiceBeans(Arrays.<Object>asList(companyResource));
		factory.setFeatures(Arrays.<Feature>asList(swagger2feature));
		factory.setAddress("http://localhost:9000/api");
		if (useJacksonProvider) {
			factory.setProviders(Arrays.<Object>asList(jacksonProvider));
		} else {
			factory.setProviders(Arrays.<Object>asList(defaultProvider));
		}
		
		return factory;
	}


	@Bean
	public JsonService jsonService(
			@Qualifier(DATA_FORMAT_CAMEL_GSON_BEAN_ID) GsonDataFormat gson,
			@Qualifier(DATA_FORMAT_CAMEL_JACKSON_BEAN_ID) JacksonDataFormat jackson,
			@Qualifier(DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID) JacksonDataFormat custom,
			@Qualifier(DATA_FORMAT_DEFAULT_CXF_PROVIDER_BEAN_ID)  JSONProvider<Object> jsonProvider,
			@Qualifier(DATA_FORMAT_JACKSON_CXF_PROVIDER_BEAN_ID)  JacksonJsonProvider jacksonProvider,
			CamelContext camelContext) {
		return new JsonService(gson, jackson, custom, jsonProvider, jacksonProvider, camelContext);
	}
}
