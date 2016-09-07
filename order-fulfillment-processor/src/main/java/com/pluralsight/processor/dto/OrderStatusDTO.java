package com.pluralsight.processor.dto;

import org.apache.commons.lang3.*;

/**
 * Key and value for order statuses.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
public enum OrderStatusDTO {

   NEW("N", "New"), PROCESSING("P", "Processing");

   private String code;
   private String description;

   private OrderStatusDTO(String code, String description) {
      this.code = code;
      this.description = description;
   }

   /**
    * @return the code
    */
   public String getCode() {
      return code;
   }

   /**
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   public static OrderStatusDTO getOrderStatusByCode(String code) {
      OrderStatusDTO orderStatus = null;
      if (StringUtils.isNotBlank(code)) {
         for (OrderStatusDTO status : OrderStatusDTO.values()) {
            if (status.getCode().equals(code)) {
               orderStatus = status;
               break;
            }
         }
      }
      return orderStatus;
   }
}
