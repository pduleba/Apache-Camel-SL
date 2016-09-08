package com.pluralsight.processor.route.camel;

import static com.pluralsight.processor.generated.FulfillmentCenter.ABC_FULFILLMENT_CENTER;
import static com.pluralsight.processor.generated.FulfillmentCenter.FULFILLMENT_CENTER_ONE;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.ConnectionFactory;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.CamelExchangeException;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.test.spring.CamelSpringDelegatingTestContextLoader;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.pluralsight.processor.camel.ABCFulfillmentCenterAggregationStrategy;
import com.pluralsight.processor.generated.FulfillmentCenter;
import com.pluralsight.processor.service.FulfillmentABCProcessor;

/**
 * Test case for the ABC fulfillment center SSH route.
 * 
 * @author Michael Hoffman, Pluralsight
 *
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(
      classes = { ConsumeFulfillmentABCOrderRouteTest.TestConfig.class },
      loader = CamelSpringDelegatingTestContextLoader.class)
public class ConsumeFulfillmentABCOrderRouteTest {

   @org.apache.camel.Produce(uri = "direct:test")
   protected ProducerTemplate testProducer;

   @org.apache.camel.EndpointInject(uri = "mock:direct:result")
   protected MockEndpoint resultEndpoint;

   public static String fulfillmentCenterMessage1 =
         "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
               + "<Order xmlns=\"http://www.pluralsight.com/orderfulfillment/Order\">"
               + "<OrderType>"
               + "<FirstName>Jane</FirstName>"
               + "<LastName>Smith</LastName>"
               + "<Email>jane@somehow.com</Email>"
               + "<OrderNumber>1003</OrderNumber>"
               + "<TimeOrderPlaced>2014-10-24T12:09:21.330-05:00</TimeOrderPlaced>"
               + "<FulfillmentCenter>"
               + ABC_FULFILLMENT_CENTER
                     .value() + "</FulfillmentCenter>" + "<OrderItems>"
               + "<ItemNumber>078-1344200444</ItemNumber>"
               + "<Price>20.00000</Price>" + "<Quantity>1</Quantity>"
               + "</OrderItems>" + "</OrderType>" + "</Order>";

   public static String fulfillmentCenterMessage2 =
         "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
               + "<Order xmlns=\"http://www.pluralsight.com/orderfulfillment/Order\">"
               + "<OrderType>"
               + "<FirstName>Larry</FirstName>"
               + "<LastName>Horse</LastName>"
               + "<Email>larry@somehow.com</Email>"
               + "<OrderNumber>1004</OrderNumber>"
               + "<TimeOrderPlaced>2014-10-24T12:09:21.330-05:00</TimeOrderPlaced>"
               + "<FulfillmentCenter>"
               + com.pluralsight.processor.generated.FulfillmentCenter.ABC_FULFILLMENT_CENTER
                     .value() + "</FulfillmentCenter>" + "<OrderItems>"
               + "<ItemNumber>078-1344200445</ItemNumber>"
               + "<Price>21.00000</Price>" + "<Quantity>1</Quantity>"
               + "</OrderItems>" + "</OrderType>" + "</Order>";

   public static String fulfillmentCenterMessage3 =
         "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
               + "<Order xmlns=\"http://www.pluralsight.com/orderfulfillment/Order\">"
               + "<OrderType>"
               + "<FirstName>Michael</FirstName>"
               + "<LastName>Tester</LastName>"
               + "<Email>mtestersomehow.com</Email>"
               + "<OrderNumber>1005</OrderNumber>"
               + "<TimeOrderPlaced>2014-10-24T12:09:21.330-05:00</TimeOrderPlaced>"
               + "<FulfillmentCenter>"
               + com.pluralsight.processor.generated.FulfillmentCenter.ABC_FULFILLMENT_CENTER
                     .value() + "</FulfillmentCenter>" + "<OrderItems>"
               + "<ItemNumber>078-1344200446</ItemNumber>"
               + "<Price>22.00000</Price>" + "<Quantity>1</Quantity>"
               + "</OrderItems>" + "</OrderType>" + "</Order>";

   public static String fulfillmentCenterMessage4 =
         "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
               + "<Order xmlns=\"http://www.pluralsight.com/orderfulfillment/Order\">"
               + "<OrderType>"
               + "<FirstName>Jane</FirstName>"
               + "<LastName>Smith</LastName>"
               + "<Email>jane@somehow.com</Email>"
               + "<OrderNumber>1003</OrderNumber>"
               + "<TimeOrderPlaced>2014-10-24T12:09:21.330-05:00</TimeOrderPlaced>"
               + "<FulfillmentCenter>"
               + FULFILLMENT_CENTER_ONE
                     .value() + "</FulfillmentCenter>" + "<OrderItems>"
               + "<ItemNumber>078-1344200444</ItemNumber>"
               + "<Price>20.00000</Price>" + "<Quantity>1</Quantity>"
               + "</OrderItems>" + "</OrderType>" + "</Order>";

   @Before
   public void setUp() throws Exception {
   }

   @After
   public void tearDown() throws Exception {
   }

   @Configuration
   public static class TestConfig extends CamelConfiguration {
      @Bean(name = FulfillmentABCProcessor.BEAN_NAME)
      public FulfillmentABCProcessor aBCFulfillmentProcessor() {
         return new FulfillmentABCProcessor();
      }

      @Bean
      public ConnectionFactory jmsConnectionFactory() {
         return new org.apache.activemq.ActiveMQConnectionFactory(
               "tcp://localhost:61616");
      }

      @Bean(initMethod = "start", destroyMethod = "stop")
      public PooledConnectionFactory pooledConnectionFactory() {
         PooledConnectionFactory factory = new PooledConnectionFactory();
         factory.setConnectionFactory(jmsConnectionFactory());
         factory.setMaxConnections(10);
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
      public RouteBuilder fullfillmentABCRoutes() {
         return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
               // Namespace is needed for XPath lookup
               Namespaces namespace = new Namespaces("o", "http://www.pluralsight.com/orderfulfillment/Order");
               SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hhmmss");
               String dateString = sdf.format(new Date(System.currentTimeMillis()));

               onException(CamelExchangeException.class).to(
                     "activemq:queue:ABC_FULFILLMENT_ERROR");

               // 1 - Route from the direct component to an ActiveMQ component
               from("direct:test").to("activemq:queue:ABC_FULFILLMENT_REQUEST");

               // 2 - Aggregate XML messages from the queue.
               from("activemq:queue:ABC_FULFILLMENT_REQUEST")
                     .aggregate(new ABCFulfillmentCenterAggregationStrategy())
                     .xpath(
                           "//*[contains(text(), '"
                                 + FulfillmentCenter.ABC_FULFILLMENT_CENTER.value()
                                 + "')]", String.class, namespace)
                     .ignoreInvalidCorrelationKeys().completionInterval(10000)
                     .beanRef(FulfillmentABCProcessor.BEAN_NAME, "processAggregate")
                     .marshal() // marshal to CSV format
                     .csv()
                     .to("file://C://Users//pduleba//Desktop//out?fileName=abcfc-"
                           + dateString + ".csv")
//                     .setHeader("CamelFileName", constant("abcfc-" + dateString + ".csv")) // for Apache Component FTP
//                     .to("sftp://localhost:22?username=test&password=test")
                     .to("mock:direct:result");
            }
         };
      }
   }

   /**
    * Tests the route for normal, successful execution.
    * 
    * @throws Exception
    */
   @Test
   public void test_success() throws Exception {
      // 1 - Send the XML as the body of the message through the route
      testProducer.sendBody(fulfillmentCenterMessage1);
      testProducer.sendBody(fulfillmentCenterMessage2);
      testProducer.sendBody(fulfillmentCenterMessage3);
      testProducer.sendBody(fulfillmentCenterMessage4);
      // 2 - Wait until aggregation is complete.
      Thread.sleep(20000);
      // 3 - Print out the results to manually verify the aggregated message.
      System.err.println(resultEndpoint.getExchanges().size());
   }
}
