package com.pduleba.jaxrs;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@XmlRootElement
public class DeveloperResponse {

   private int responseCode;
   private String response;

}
