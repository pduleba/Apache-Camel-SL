package com.pduleba.jaxrs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeveloperResponse {

   private int responseCode;
   private String response;

}
