package com.pduleba.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.camel.CamelContext;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatResolver;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonService {

	public static final Logger LOG = Logger.getLogger(JsonService.class);
	
	private GsonDataFormat gson;
	private JacksonDataFormat jackson;
	private JacksonDataFormat custom;

	private JSONProvider<Object> defaultProvider;
	private JacksonJsonProvider jacksonProvider;

	private CamelContext camelContext;
	private DataFormatResolver dataFormatResolver;

	public JsonService(GsonDataFormat gson, JacksonDataFormat jackson,
			JacksonDataFormat custom, 
			JSONProvider<Object> defaultProvider,
			JacksonJsonProvider jacksonProvider,
			CamelContext camelContext) {
		super();
		this.gson = gson;
		this.jackson = jackson;
		this.custom = custom;
		this.jacksonProvider = jacksonProvider;
		this.defaultProvider = defaultProvider;
		this.camelContext = camelContext;
		this.dataFormatResolver = camelContext.getDataFormatResolver();
	}

	public <T> String serializeByDefaultProvider(T in, Class<T> clazz) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			defaultProvider.writeTo(in, clazz, clazz.getAnnotations(),
		            MediaType.APPLICATION_JSON_TYPE, null, out);
			
			return out.toString();
		} catch (WebApplicationException | IOException e) {
			LOG.error("Unable to serialize", e);
		}
		
		return null;
	}


	public <T> String serializeByJacksonProvider(T in, Class<T> clazz) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			jacksonProvider.writeTo(in, clazz, clazz, clazz.getAnnotations(),
		            MediaType.APPLICATION_JSON_TYPE, null, out);
			
			return out.toString();
		} catch (WebApplicationException | IOException e) {
			LOG.error("Unable to serialize", e);
		}
		
		return null;
	}

	
	public <T> String serializeByDefaultGson(T in) {
		return gson.getGson().toJson(in);
	}

	public <T> String serializeByDefaultJackson(T in)
			throws JsonProcessingException {
		return jackson.getObjectMapper().writeValueAsString(in);
	}

	public <T> String serializeByCustomJackson(T in)
			throws JsonProcessingException {
		return custom.getObjectMapper().writeValueAsString(in);
	}

	@SuppressWarnings("unchecked")
	public <T extends DataFormat> T resolveDataFormat(String beanId) {
		return (T) this.dataFormatResolver.resolveDataFormat(beanId,
				camelContext);
	}

	public <T> T deserializeByCustomJackson(String value, Class<T> valueType)
			throws IOException {
		return custom.getObjectMapper().readValue(value, valueType);
	}
}
