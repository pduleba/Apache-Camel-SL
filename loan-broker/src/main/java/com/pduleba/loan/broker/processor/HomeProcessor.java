package com.pduleba.loan.broker.processor;

import com.pduleba.loan.broker.xsd.HouseInfo;

public class HomeProcessor {

	public HouseInfo process(HouseInfo house) {
		
		house.setAppraisedValue(house.getAppraisedValue() + 1000);
		
		return house;
	}
	
}
