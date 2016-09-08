package com.pluralsight.fulfillmentcenterone;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the response from the fulfillment service restful call.
 * 
 * @author Michael Hoffman, Pluralsight
 *
 */
@XmlRootElement
public class FulfillmentResponse {

   private int responseCode;
   private String response;

   /**
    * Default constructor
    */
   public FulfillmentResponse() {

   }

   /**
    * Full constructor
    * 
    * @param responseCode
    * @param response
    */
   public FulfillmentResponse(int responseCode, String response) {
      super();
      this.responseCode = responseCode;
      this.response = response;
   }

   /**
    * @return the responseCode
    */
   public int getResponseCode() {
      return responseCode;
   }

   /**
    * @param responseCode
    *           the responseCode to set
    */
   public void setResponseCode(int responseCode) {
      this.responseCode = responseCode;
   }

   /**
    * @return the response
    */
   public String getResponse() {
      return response;
   }

   /**
    * @param response
    *           the response to set
    */
   public void setResponse(String response) {
      this.response = response;
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("FulfillmentResponse [responseCode=");
      builder.append(responseCode);
      builder.append(", ");
      if (response != null) {
         builder.append("response=");
         builder.append(response);
      }
      builder.append("]");
      return builder.toString();
   }
}
