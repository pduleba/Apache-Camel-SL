package com.pluralsight.processor.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO implements Serializable {

   private static final long serialVersionUID = 8218385047375857766L;

   private long id;
   private CatalogItemDTO catalogItem;
   private String status;
   private BigDecimal price;
   private Date lastUpdate;
   private int quantity;

}
