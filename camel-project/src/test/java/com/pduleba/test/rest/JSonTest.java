package com.pduleba.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pduleba.config.ApplicationConfig;
import com.pduleba.config.CamelConfig;
import com.pduleba.jaxrs.DeveloperRequest;
import com.pduleba.service.JsonService;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class }, loader = CamelSpringDelegatingTestContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JSonTest {

	@Produce(uri = "direct:toJson_ByDefaultJackson")
	protected ProducerTemplate toJson_ByDefaultJackson;
	@Produce(uri = "direct:toPojo_ByDefaultJackson")
	protected ProducerTemplate toPojo_ByDefaultJackson;
	
	@Autowired
	private JsonService jsonService;
	
	private String payload_ByDefaultJackson;
	private String payload_ByJacksonProvider;
	private DeveloperRequest request;

	@Before
	public void before() throws JsonProcessingException {
		this.request = DeveloperRequest.getRequest();
		this.payload_ByDefaultJackson = jsonService.serializeByDefaultJackson(request);
		this.payload_ByJacksonProvider = jsonService.serializeByJacksonProvider(request, DeveloperRequest.class);
	}
		
    @Test
    public void shouldUnmarshalByDefaultJackson() throws Exception {
    	// Given
    	
		// When
        DeveloperRequest pojo = toPojo_ByDefaultJackson.requestBody((Object) payload_ByDefaultJackson, DeveloperRequest.class);
        assertNotNull(pojo);
        
    	// Then
		assertNotNull(request);
		assertNotNull(pojo);
        assertEquals(pojo.getFirstName(), request.getFirstName());
        assertEquals(pojo.getLastName(), request.getLastName());
    }

    @Test
    public void shouldMarshalByDefaultJackson() throws Exception {
    	// Given

        // When
        String json = toJson_ByDefaultJackson.requestBody(request, String.class);
        
        // Then
        assertEquals(payload_ByDefaultJackson, json);
    }

    @Test
    public void testResolveDataFormat() throws Exception {
    	// Given
    	String camelJacksonBeanId = CamelConfig.DATA_FORMAT_JSON_BEAN_ID;

        // When
    	DataFormat camelJackson = jsonService.resolveDataFormat(camelJacksonBeanId);
        
        // Then
        assertNotNull(camelJackson);
		assertTrue(camelJackson == jsonService.getJackson());
    }

    @Test
    public void shouldBeMarshaledByJacksonProvider() throws Exception {
    	// Given

        // When
        
        // Then
        assertNotNull(payload_ByJacksonProvider);
    }

}
