package com.pduleba.camel.restful;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DeveloperResponse {

   private int responseCode;
   private String response;

}
