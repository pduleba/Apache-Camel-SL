package com.pduleba.loan.broker.processor;

import java.util.ArrayList;
import java.util.Map;

import org.apache.camel.LoggingLevel;
import org.apache.camel.util.CamelLogger;

import com.pduleba.loan.broker.xsd.HouseInfo;

public class HouseInfoProcessor {

	private final CamelLogger logger = new CamelLogger(
			HouseInfoProcessor.class.getCanonicalName(), LoggingLevel.INFO);

	public String processAddress(HouseInfo houseInfo) {
		String newAddress = "";

		newAddress = houseInfo.getAddress().replaceAll(" ", "+");
		newAddress = newAddress.replace("", "");

		return newAddress;
	}

	@SuppressWarnings("unchecked")
	public String findGeoLocation(Map<String, ArrayList<?>> resultmap) {

		String latNlng = "";
		try {

			Map<String, Map<String, Object>> geometryList = (Map<String, Map<String, Object>>) resultmap
					.get("results").get(0);
			Map<String, Object> geometrymap = (Map<String, Object>) geometryList
					.get("geometry");
			Map<String, Object> locationMap = (Map<String, Object>) geometrymap
					.get("location");

			double lat = (Double) locationMap.get("lat");
			double lng = (Double) locationMap.get("lng");

			latNlng = lat + "," + lng;

			latNlng.replaceAll("=", "%2D");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latNlng;
	}

	public HouseInfo updateQuoteWithSchools(HouseInfo houseInfo, int noSchool) {

		double upPrice = ((110.00 + noSchool) / 100.00)
				* houseInfo.getAppraisedValue();

		logger.doLog("Up percent [" + (110 + noSchool) / 100.00 + "]");

		int appraisedValue = (int) Math.round(upPrice);
		logger.doLog("This price has gone up from ["
				+ houseInfo.getAppraisedValue() + "] to [" + appraisedValue
				+ "]");

		houseInfo.setAppraisedValue(appraisedValue);

		return houseInfo;
	}
}
