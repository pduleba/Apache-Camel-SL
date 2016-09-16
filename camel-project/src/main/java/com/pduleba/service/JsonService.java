package com.pduleba.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import lombok.Getter;

import org.apache.camel.CamelContext;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatResolver;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class JsonService {

	public static final Logger LOG = Logger.getLogger(JsonService.class);
	
	@Getter private JacksonDataFormat jackson;

	private JacksonJsonProvider jacksonProvider;

	private CamelContext camelContext;
	private DataFormatResolver dataFormatResolver;

	public JsonService(JacksonDataFormat jackson,
			JacksonJsonProvider jacksonProvider,
			CamelContext camelContext) {
		super();
		this.jackson = jackson;
		this.jacksonProvider = jacksonProvider;
		this.camelContext = camelContext;
		this.dataFormatResolver = camelContext.getDataFormatResolver();
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

	public <T> String serializeByDefaultJackson(T in)
			throws JsonProcessingException {
		return jackson.getObjectMapper().writeValueAsString(in);
	}

	@SuppressWarnings("unchecked")
	public <T extends DataFormat> T resolveDataFormat(String beanId) {
		return (T) this.dataFormatResolver.resolveDataFormat(beanId,
				camelContext);
	}
}
