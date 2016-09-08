package com.pluralsight.processor.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.pluralsight.processor.dto.ServiceOrder;
import com.pluralsight.processor.dto.ServiceOrderItem;
import com.pluralsight.processor.dto.ServiceRequest;
import com.pluralsight.processor.generated.Order;
import com.pluralsight.processor.generated.OrderItemType;
import com.pluralsight.processor.generated.OrderType;

/**
 * Processor for the fulfillment center one restful web service route. Accepts
 * the order XML from the exchange, converts it to JSON format and then returns
 * the JSON as a string.
 * 
 * @author Michael Hoffman, Pluralsight
 *
 */
public class FulfillmentCenterOneProcessor {

   private static final Logger log = LoggerFactory
         .getLogger(FulfillmentCenterOneProcessor.class);
   
   /**
    * Accepts the order XML from the route exchange's inbound message body and
    * then converts it to JSON format.
    * 
    * @param orderXml
    * @return
    */
   public String transform(String orderXml) {
      String output = null;
      try {
         if (orderXml == null) {
            throw new Exception(
                  "Order xml was not bound to the method via integration framework.");
         }

         output = processCreateOrderRequestMessage(orderXml);
      } catch (Exception e) {
         log.error(
               "Fulfillment center one message translation failed: "
                     + e.getMessage(), e);
      }
      return output;
   }

   protected String processCreateOrderRequestMessage(String orderXml)
         throws Exception {
      // 1 - Unmarshall the Order from an XML string to the generated order
      // class.
      JAXBContext context =
            JAXBContext
                  .newInstance(Order.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Order order =
            (Order) unmarshaller
                  .unmarshal(new StringReader(orderXml));

      // 2 - Build an Order Request object and return the JSON-ified object
      return new Gson().toJson(buildOrderRequestType(order));
   }

   protected ServiceRequest buildOrderRequestType(
         Order orderFromXml) {
      OrderType orderTypeFromXml = orderFromXml.getOrderType();

      // 1 - Build order item types
      List<OrderItemType> orderItemTypesFromXml =
            orderTypeFromXml.getOrderItems();
      List<ServiceOrderItem> orderItems =
            new ArrayList<ServiceOrderItem>();
      for (OrderItemType orderItemTypeFromXml : orderItemTypesFromXml) {
         orderItems
               .add(new ServiceOrderItem(
                     orderItemTypeFromXml.getItemNumber(), orderItemTypeFromXml
                           .getPrice(), orderItemTypeFromXml.getQuantity()));
      }

      // 2 - Build order
      List<ServiceOrder> orders =
            new ArrayList<ServiceOrder>();
      ServiceOrder order =
            new ServiceOrder();
      order.setFirstName(orderTypeFromXml.getFirstName());
      order.setLastName(order.getLastName());
      order.setEmail(orderTypeFromXml.getEmail());
      order.setOrderNumber(orderTypeFromXml.getOrderNumber());
      order.setTimeOrderPlaced(orderTypeFromXml.getTimeOrderPlaced()
            .toGregorianCalendar().getTime());
      order.setOrderItems(orderItems);
      orders.add(order);

      // 3 - Build order request
      ServiceRequest orderRequest = new ServiceRequest();
      orderRequest.setOrders(orders);

      // 4 - Return the order request
      return orderRequest;
   }

}