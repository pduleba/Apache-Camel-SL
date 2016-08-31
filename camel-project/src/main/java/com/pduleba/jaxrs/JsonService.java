package com.pduleba.jaxrs;

import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatResolver;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonService {

	private GsonDataFormat gson;
	private JacksonDataFormat jackson;
	private JacksonDataFormat custom;

	private CamelContext camelContext;
	private DataFormatResolver dataFormatResolver;

	public JsonService(GsonDataFormat gson, JacksonDataFormat jackson,
			JacksonDataFormat custom, CamelContext camelContext) {
		super();
		this.gson = gson;
		this.jackson = jackson;
		this.custom = custom;
		this.camelContext = camelContext;
		this.dataFormatResolver = camelContext.getDataFormatResolver();
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