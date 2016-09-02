Camel Router Project for Spring Framework (OSGi)
=========================================

To build this project use

    mvn install

To run the project you can execute the following Maven goal

    mvn camel:run

To deploy the project in OSGi. For example using Apache ServiceMix
or Apache Karaf. You can run the following command from its shell:

    osgi:install -s mvn:com.pduleba/loan-broker/1.0.0-SNAPSHOT

For more help see the Apache Camel documentation

    http://camel.apache.org/

Based on Christina Lin tutorial (part 1 - 5)

	https://www.youtube.com/watch?v=JMaYhxfup2M&list=PLIS-R80eiu1sYyXHoCD7VlLQwHkuIwEdr&index=1 
	https://github.com/weimeilin79