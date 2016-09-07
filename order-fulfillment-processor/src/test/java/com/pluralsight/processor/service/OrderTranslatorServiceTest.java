package com.pluralsight.processor.service;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pluralsight.processor.ApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfig.class })
@ActiveProfiles("test")
@WebAppConfiguration
@TransactionConfiguration(transactionManager = "transactionManager")
@TestExecutionListeners(
      listeners = { DependencyInjectionTestExecutionListener.class,
            DirtiesContextTestExecutionListener.class,
            TransactionalTestExecutionListener.class })
@Transactional
public class OrderTranslatorServiceTest {

   @Inject
   private OrderTranslatorService translator;
   @Inject
   private JdbcTemplate jdbcTemplate;

   private Long orderId;

   @Before
   public void setUp() {
	   orderId = jdbcTemplate.queryForObject("select id from orders.orderdata FETCH FIRST ROW ONLY", Long.class);
   }

   @Test
   public void test_transformToOrderItemMessageSuccess() throws Exception {
      Map<String, Integer> orderIds = new HashMap<>();
      orderIds.put("id", Integer.valueOf(orderId.intValue()));
      String xml = translator.transform(orderIds);
      System.err.println(xml);
      assertNotNull(xml);
   }
}
