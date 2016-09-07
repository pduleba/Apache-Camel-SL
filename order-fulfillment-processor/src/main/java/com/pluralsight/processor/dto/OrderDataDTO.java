package com.pluralsight.processor.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDataDTO {
	
   private long id;
   private CustomerDTO customer;
   private String orderNumber;
   private Date timeOrderPlaced;
   private Date lastUpdate;
   private String status;

}
