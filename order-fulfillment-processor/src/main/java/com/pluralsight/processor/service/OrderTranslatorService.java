package com.pluralsight.processor.service;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component(OrderTranslatorService.BEAN_NAME)
public class OrderTranslatorService {

	private static final Logger log = LoggerFactory
			.getLogger(OrderTranslatorService.class);

	public static final String BEAN_NAME = "OrderTranslatorService";

	@Inject
	private OrderService orderService;

	public String transform(Map<String, Integer> orderIds) {
		String output = null;
		try {
			if (orderIds == null) {
				throw new Exception(
						"Order id was not bound to the method via integration framework.");
			}
			if (!orderIds.containsKey("id")) {
				throw new Exception(
						"Could not find a valid key of 'id' for the order ID.");
			}
			Integer id = orderIds.get("id");
			if (id == null || !(id instanceof Integer)) {
				throw new Exception(
						"The order ID was not correctly provided or formatted.");
			}

			output = orderService.processCreateOrderMessage(Long.valueOf(id.longValue()));
		} catch (Exception e) {
			log.error("Order processing failed: " + e.getMessage(), e);
		}
		return output;
	}

}
