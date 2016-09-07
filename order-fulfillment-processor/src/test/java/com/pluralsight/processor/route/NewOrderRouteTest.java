package com.pluralsight.processor.route;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.pluralsight.processor.ApplicationConfig;

/**
 * Test case for testing the execution of the SQL component-based route for
 * routing orders from the orders database to a log component.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { ApplicationConfig.class })
@Transactional
@WebAppConfiguration
public class NewOrderRouteTest {

   @Inject
   private JdbcTemplate jdbcTemplate;

   @Before
   public void setUp() throws Exception {
   }

   @After
   public void tearDown() throws Exception {
   }

   /**
    * Test the successful routing of a new website order.
    * 
    * @throws Exception
    */
   @Test
   public void testNewWebsiteOrderRouteSuccess() throws Exception {
      jdbcTemplate
            .execute("insert into orders.orderdata (id, customer_id, orderNumber, timeorderplaced, lastupdate, status) "
                  + "values (99, 1, '1001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'N')");
      jdbcTemplate
            .execute("insert into orders.orderitem (id, order_id, catalogitem_id, status, price, quantity, lastupdate) "
                  + "values (99, 1, 1, 'N', 20.00, 1, CURRENT_TIMESTAMP)");
      Thread.sleep(5000);
      int total = jdbcTemplate.queryForObject(
            "select count(id) from orders.orderdata where status = 'P'",
            Integer.class);
      assertEquals(1, total);

   }
}
