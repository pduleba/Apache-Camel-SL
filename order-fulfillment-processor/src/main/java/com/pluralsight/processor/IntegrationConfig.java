package com.pluralsight.processor;

import static com.pluralsight.processor.dto.OrderStatusDTO.NEW;
import static com.pluralsight.processor.dto.OrderStatusDTO.PROCESSING;
import static java.text.MessageFormat.format;

import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.pluralsight.processor.generated.FulfillmentCenter;
import com.pluralsight.processor.service.OrderTranslatorService;

@Configuration
@ComponentScan("com.pluralsight.processor")
public class IntegrationConfig extends CamelConfiguration {

	@Inject
	private Environment environment;

	@Bean
	public ConnectionFactory jmsConnectionFactory() {
		return new ActiveMQConnectionFactory(
				environment.getProperty("activemq.broker.url"));
	}

	@Bean(initMethod = "start", destroyMethod = "stop")
	public PooledConnectionFactory pooledConnectionFactory() {
		PooledConnectionFactory factory = new PooledConnectionFactory();
		factory.setConnectionFactory(jmsConnectionFactory());
		factory.setMaxConnections(Integer.parseInt(environment
				.getProperty("pooledConnectionFactory.maxConnections")));
		return factory;
	}

	@Bean
	public JmsConfiguration jmsConfiguration() {
		JmsConfiguration jmsConfiguration = new JmsConfiguration();
		jmsConfiguration.setConnectionFactory(pooledConnectionFactory());
		return jmsConfiguration;
	}

	@Bean
	public ActiveMQComponent activeMq() {
		ActiveMQComponent activeMq = new ActiveMQComponent();
		activeMq.setConfiguration(jmsConfiguration());
		return activeMq;
	}

	@Bean
	public SqlComponent sql(DataSource dataSource) {
		SqlComponent sqlComponent = new SqlComponent();

		sqlComponent.setDataSource(dataSource);

		return sqlComponent;
	}

	@Bean
	public RouteBuilder producerRoute() {
		return new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from(
						new StringBuilder()
								.append(format(
										"sql:SELECT id FROM orders.orderdata WHERE status = ''{0}''",
										NEW.getCode()))
								.append("?consumer.onConsume=")
								.append(format(
										"UPDATE orders.orderdata SET status = ''{0}'' WHERE id = :#id",
										PROCESSING.getCode())).toString()).routeId("producerRoute")
						.log("SQL RESULT = ${body}")
						.beanRef(OrderTranslatorService.BEAN_NAME, "transform")
						.to("activemq:queue:ORDER_ITEM_PROCESSING");
			}
		};
	}

	@Bean
	public RouteBuilder proxyRoute() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				Namespaces namespace = new Namespaces(
						"o",
						"http://www.pluralsight.com/orderfulfillment/Order");
				// Send from the ORDER_ITEM_PROCESSING queue to the correct
				// fulfillment center queue.
				from("activemq:queue:ORDER_ITEM_PROCESSING").routeId("proxyRoute")
						.log("Before Choice ${body}")
						.choice()
							.when()
								.xpath("/o:Order/o:OrderType/o:FulfillmentCenter = '"
										+ FulfillmentCenter.ABC_FULFILLMENT_CENTER.value()
										+ "'", namespace)
								.to("activemq:queue:ABC_FULFILLMENT_REQUEST")
							.when()
								.xpath("/o:Order/o:OrderType/o:FulfillmentCenter = '"
										+ FulfillmentCenter.FULFILLMENT_CENTER_ONE.value()
										+ "'", namespace)
								.to("activemq:queue:FC1_FULFILLMENT_REQUEST")
							.otherwise()
								.to("activemq:queue:ERROR_FULFILLMENT_REQUEST");
				
			}
		};
	}

	@Bean
	public RouteBuilder dispatcherRoute() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("activemq:queue:FC1_FULFILLMENT_REQUEST").routeId("dispatcherRoute")
						.beanRef("fulfillmentCenterOneProcessor", "transform")
						.setHeader(org.apache.camel.Exchange.CONTENT_TYPE,
								constant("application/json"))
						.to("http4://localhost:8090/services/orderFulfillment/processOrders");
			}
		};
	}
}
