package com.pluralsight.processor.service;

import java.util.List;

import com.pluralsight.processor.dto.OrderDataDTO;
import com.pluralsight.processor.dto.OrderItemDTO;
import com.pluralsight.processor.dto.OrderStatusDTO;

/**
 * Interface for order functionality
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
public interface OrderService {

   /**
    * Returns the details of the order without the order items.
    * 
    * @return List<Order>
    */
   List<OrderDataDTO> getOrderDetails();

   /**
    * Returns the order details of the order without the order item for the
    * status passed. The result size is limited based on the fetch size passed.
    * 
    * @param OrderStatusDTO
    *           orderStatus
    * @param int fetchSize
    * @return List<Order>
    */
   List<OrderDataDTO> getOrderDetails(OrderStatusDTO orderStatus, int fetchSize);

   /**
    * Executes the business task of order fulfillment
    */
   void processOrderFulfillment();

   /**
    * Updates the order status of one or more orders. Also will update the list
    * of orders passed to match the status passed.
    * 
    * @param orders
    * @param orderStatus
    */
   void processOrderStatusUpdate(List<OrderDataDTO> orders, OrderStatusDTO orderStatus)
         throws Exception;

   /**
    * Retrieves the order items for an order.
    * 
    * @param id
    * @return List<OrderItem>
    */
   List<OrderItemDTO> getOrderItems(long id);

   /**
    * Retrieves the database data for an order based on the order ID passed.
    * Transforms the database order into XML format for an Order type.
    * 
    * @param id
    * @return
    * @throws Exception
    */
   String processCreateOrderMessage(Long orderId) throws Exception;

}
