package com.pluralsight.processor.service;

import javax.inject.Inject;

import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.pluralsight.processor.service.command.FulfillmentCommand;
import com.pluralsight.processor.service.command.FulfillmentContext;

/**
 * This is the entry point for order fulfillment processing. It provides a
 * single method for orchestrating the fulfillment process. Execution is started
 * by a scheduler or direct calls.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@Component
public class FulfillmentService {

   @SuppressWarnings("unused")
   private static final Logger log = LoggerFactory
         .getLogger(FulfillmentService.class);

   @Setter 
   @Inject
   @Qualifier("newOrderProducerCommand")
   private FulfillmentCommand newOrderProducerCommand;

   @Setter 
   @Inject
   @Qualifier("newOrderConsumerCommand")
   private FulfillmentCommand newOrderConsumerCommand;

   /**
    * Orchestrates order fulfillment.
    */
   public void run() {
      // 1 - Build a context to be passed into each of the commands
      FulfillmentContext context = new FulfillmentContext();

      // 2 - Call the order retrieval command to discover what orders need to be
      // fulfilled. New orders will be added to the context.
      newOrderProducerCommand.execute(context);

      // 3 - Call the order fulfill command to fulfill new orders through
      // fulfillment center 1.
      newOrderConsumerCommand.execute(context);
   }

}
