package com.pduleba.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.camel.BeanInject;
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

	@Produce(uri = "direct:toJson_ByDefaultGson")
	protected ProducerTemplate toJson_ByDefaultGson;
	@Produce(uri = "direct:toPojo_ByDefaultGson")
	protected ProducerTemplate toPojo_ByDefaultGson;
	
	@Produce(uri = "direct:toJson_ByDefaultJackson")
	protected ProducerTemplate toJson_ByDefaultJackson;
	@Produce(uri = "direct:toPojo_ByDefaultJackson")
	protected ProducerTemplate toPojo_ByDefaultJackson;
	
	@Produce(uri = "direct:toJson_ByCustomJackson")
	protected ProducerTemplate toJson_ByCustomJackson;
	@Produce(uri = "direct:toPojo_ByCustomJackson")
	protected ProducerTemplate toPojo_ByCustomJackson;
	
	@Autowired
	private JsonService jsonService;
	
	@BeanInject
	private JsonService jsonServiceOther;
	
	private String payload_ByDefaultGson;
	private String payload_ByDefaultJackson;
	private String payload_ByCustomJackson;
	private String payload_ByDefaultProvider;
	private String payload_ByJacksonProvider;
	private DeveloperRequest request;

	@Before
	public void before() throws JsonProcessingException {
		this.request = DeveloperRequest.getRequest();
		this.payload_ByDefaultGson = jsonService.serializeByDefaultGson(request);
		this.payload_ByDefaultJackson = jsonService.serializeByDefaultJackson(request);
		this.payload_ByCustomJackson = jsonService.serializeByCustomJackson(request);
		this.payload_ByDefaultProvider = jsonService.serializeByDefaultProvider(request, DeveloperRequest.class);
		this.payload_ByJacksonProvider = jsonService.serializeByJacksonProvider(request, DeveloperRequest.class);
	}
		
    @Test
    public void shouldUnmarshalByDefaultGson() throws Exception {
    	// Given
    	
		// When
        DeveloperRequest pojo = toPojo_ByDefaultGson.requestBody((Object) payload_ByDefaultGson, DeveloperRequest.class);
        assertNotNull(pojo);
        
    	// Then
		assertNotNull(request);
		assertNotNull(pojo);
        assertEquals(pojo.getFirstName(), request.getFirstName());
        assertEquals(pojo.getLastName(), request.getLastName());
    }

    @Test
    public void shouldMarshalByDefaultGson() throws Exception {
    	// Given

        // When
        String json = toJson_ByDefaultGson.requestBody(request, String.class);
        
        // Then
        assertEquals(payload_ByDefaultGson, json);
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
    public void shouldUnmarshalByCustomJackson() throws Exception {
    	// Given
        DeveloperRequest request = DeveloperRequest.getRequest();
    	
		// When
        DeveloperRequest pojo = toPojo_ByCustomJackson.requestBody((Object) payload_ByCustomJackson, DeveloperRequest.class);
        assertNotNull(pojo);
        
    	// Then
		assertNotNull(request);
		assertNotNull(pojo);
        assertEquals(pojo.getFirstName(), request.getFirstName());
        assertEquals(pojo.getLastName(), request.getLastName());
    }

    @Test
    public void shouldMarshalByCustomJackson() throws Exception {
    	// Given

        // When
        String json = toJson_ByCustomJackson.requestBody(request, String.class);
        
        // Then
        assertEquals(payload_ByCustomJackson, json);
    }
    
    @Test
    public void customJacksonDataFormatShouldNotbeEqualToDefaultJackson() throws Exception {
    	// Given

        // When
        String json = toJson_ByCustomJackson.requestBody(request, String.class);
        
        // Then
        assertNotEquals(payload_ByDefaultJackson, json);
    }

    @Test
    public void testResolveDataFormat() throws Exception {
    	// Given
    	String camelGsonBeanId = CamelConfig.DATA_FORMAT_CAMEL_GSON_BEAN_ID;
    	String camelJacksonBeanId = CamelConfig.DATA_FORMAT_CAMEL_JACKSON_BEAN_ID;
    	String customJacksonBeanId = CamelConfig.DATA_FORMAT_CUSTOM_JACKSON_BEAN_ID;

        // When
    	DataFormat camelGson = jsonService.resolveDataFormat(camelGsonBeanId);
    	DataFormat camelJackson = jsonService.resolveDataFormat(camelJacksonBeanId);
    	DataFormat customJackson = jsonService.resolveDataFormat(customJacksonBeanId);
        
        // Then
        assertNotNull(camelGson);
		assertTrue(camelGson == jsonService.getGson());
        assertNotNull(camelJackson);
		assertTrue(camelJackson == jsonService.getJackson());
        assertNotNull(customJackson);
		assertTrue(customJackson == jsonService.getCustom());
    }
    

    @Test
    public void shouldBeMarshaledByDefaultProvider() throws Exception {
    	// Given

        // When
        
        // Then
        assertNotNull(payload_ByDefaultProvider);
    }
	
    @Test
    public void shouldBeMarshaledByJacksonProvider() throws Exception {
    	// Given

        // When
        
        // Then
        assertNotNull(payload_ByJacksonProvider);
    }

}
