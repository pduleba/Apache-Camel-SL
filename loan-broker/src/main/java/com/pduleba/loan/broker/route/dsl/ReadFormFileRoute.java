package com.pduleba.loan.broker.route.dsl;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;

import com.pduleba.loan.broker.route.ReadFormFileRouteBuilder;

public class ReadFormFileRoute {
	
	static int WAIT_TIMEOUT = 500;
	static int TOTAL_TIME = 30;
	static int LOOP_COUNT = 30*1000/WAIT_TIMEOUT;
	
	static String from = "file://data/datafile";
	static String to = "file://data/datafile-out";

	
	public static void main(String args[]) throws Exception {
		// S1 : create context
        CamelContext context = new DefaultCamelContext();
        // S2 : configure route
        context.addRoutes(new ReadFormFileRouteBuilder(from, to));
        // S3 : Camel template - a handy class for kicking off exchanges
        @SuppressWarnings("unused")
		ProducerTemplate template = context.createProducerTemplate();
        // S4 : start context
        context.start();
        
        // S5 : wait for file in 'from' location
        int count = 0;
        while (LOOP_COUNT > count) {
        	count++;
            Thread.sleep(WAIT_TIMEOUT);
        }

        // S6 : finish
        context.stop();
	}
}
