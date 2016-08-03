package com.pduleba.loan.broker.processor;

import org.apache.camel.LoggingLevel;
import org.apache.camel.util.CamelLogger;

import com.pduleba.loan.broker.xsd.HouseInfo;

public class HomeProcessor {
	
	private final CamelLogger logger = new CamelLogger(HomeProcessor.class.getCanonicalName(), LoggingLevel.INFO);

	public HouseInfo process(HouseInfo house) {
		
		house.setAppraisedValue(house.getAppraisedValue() + 1000);
		
		return house;
	}

	public HouseInfo updateNumberOfSchools(HouseInfo houseInfo, int noSchool) {

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
