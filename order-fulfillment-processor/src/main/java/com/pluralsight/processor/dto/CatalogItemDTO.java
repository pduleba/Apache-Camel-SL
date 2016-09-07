package com.pluralsight.processor.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogItemDTO implements Serializable {

   private static final long serialVersionUID = -3048858539620507612L;

   private long id;
   private String itemNumber;
   private String itemName;
   private String itemType;


}
