package com.pluralsight.processor.service.command;

import java.util.List;

import com.pluralsight.processor.dto.OrderDataDTO;

/**
 * Context object that will be passed to various commands for processing order
 * fulfillment.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
public class FulfillmentContext {

   private String fulfillmentCenter1FileName;
   private String fulfillmentCenter1OutboundFolder;
   private List<OrderDataDTO> orderDetails;

   /**
    * Default constructor
    */
   public FulfillmentContext() {

   }

   /**
    * @return the orderDetails
    */
   public List<OrderDataDTO> getOrderDetails() {
      return orderDetails;
   }

   /**
    * @param orderDetails
    *           the orderDetails to set
    */
   public void setOrderDetails(List<OrderDataDTO> orderDetails) {
      this.orderDetails = orderDetails;
   }

   /**
    * @return the fulfillmentCenter1OutboundFolder
    */
   public String getFulfillmentCenter1OutboundFolder() {
      return fulfillmentCenter1OutboundFolder;
   }

   /**
    * @param fulfillmentCenter1OutboundFolder
    *           the fulfillmentCenter1OutboundFolder to set
    */
   public void setFulfillmentCenter1OutboundFolder(
         String fulfillmentCenter1OutboundFolder) {
      this.fulfillmentCenter1OutboundFolder = fulfillmentCenter1OutboundFolder;
   }

   /**
    * @return the fulfillmentCenter1FileName
    */
   public String getFulfillmentCenter1FileName() {
      return fulfillmentCenter1FileName;
   }

   /**
    * @param fulfillmentCenter1FileName
    *           the fulfillmentCenter1FileName to set
    */
   public void setFulfillmentCenter1FileName(String fulfillmentCenter1FileName) {
      this.fulfillmentCenter1FileName = fulfillmentCenter1FileName;
   }
}
