package com.pluralsight.orderfulfillment.order;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.pluralsight.config.TestIntegration;
import com.pluralsight.orderfulfillment.config.IntegrationConfig;

/**
 * Test case for testing the execution of the SQL component-based route for
 * routing orders from the orders database to a log component.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = { TestIntegration.class,
		IntegrationConfig.class })
public class NewWebsiteOrderRouteTransactionalTest {

	@Inject
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testNewWebsiteOrderRouteSuccess() throws Exception {
		assertEquals(0, customerNumber());
		
		create();
		
		assertEquals(1, customerNumber());
	}

	@Test
	public void testNewWebsiteOrderRouteSuccessOnceMore() throws Exception {
		assertEquals(0, customerNumber());
		
		create();
		
		assertEquals(1, customerNumber());
	}

	private void create() {
		jdbcTemplate
				.execute("insert into orders.catalogitem (id, itemnumber, itemname, itemtype) "
						+ "values (1, '078-1344200444', 'Build Your Own JavaScript Framework in Just 24 Hours', 'Book')");
		jdbcTemplate
				.execute("insert into orders.customer (id, firstname, lastname, email) "
						+ "values (1, 'Larry', 'Horse', 'larry@hello.com')");
		jdbcTemplate
				.execute("insert into orders.\"order\" (id, customer_id, orderNumber, timeorderplaced, lastupdate, status) "
						+ "values (1, 1, '1001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'N')");
		jdbcTemplate
				.execute("insert into orders.orderitem (id, order_id, catalogitem_id, status, price, quantity, lastupdate) "
						+ "values (1, 1, 1, 'N', 20.00, 1, CURRENT_TIMESTAMP)");
	}
	
	private int customerNumber() {
		return jdbcTemplate.queryForObject("select count(id) from orders.customer", Integer.class);
	}
}
