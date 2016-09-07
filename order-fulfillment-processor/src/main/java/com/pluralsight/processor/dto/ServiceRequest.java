package com.pluralsight.processor.dto;

import java.util.List;

/**
 * Represents an order request to the fulfillment center one RESTful web
 * service.
 * 
 * @author Michael Hoffman, Pluralsight
 *
 */
public class ServiceRequest {

   private List<ServiceOrder> orders;

   public ServiceRequest() {

   }

   public ServiceRequest(List<ServiceOrder> orders) {
      super();
      this.orders = orders;
   }

   /**
    * @return the orders
    */
   public List<ServiceOrder> getOrders() {
      return orders;
   }

   /**
    * @param orders
    *           the orders to set
    */
   public void setOrders(List<ServiceOrder> orders) {
      this.orders = orders;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("OrderRequest [");
      if (orders != null) {
         builder.append("orders=");
         builder.append(orders);
      }
      builder.append("]");
      return builder.toString();
   }
}
