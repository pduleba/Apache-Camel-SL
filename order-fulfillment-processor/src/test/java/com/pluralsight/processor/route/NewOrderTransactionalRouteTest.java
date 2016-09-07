package com.pluralsight.processor.route;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

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
@ContextConfiguration(classes = { ApplicationConfig.class })
@ActiveProfiles("test")
@Transactional
@WebAppConfiguration
public class NewOrderTransactionalRouteTest {

	@Inject
	private JdbcTemplate jdbcTemplate;

	@Test
	public void testNewWebsiteOrderRouteSuccess() throws Exception {
		assertEquals(6, customerNumber());
		
		clear();
		
		assertEquals(0, customerNumber());
	}

	@Test
	public void testNewWebsiteOrderRouteSuccessOnceMore() throws Exception {
		assertEquals(6, customerNumber());
		
		clear();
		
		assertEquals(0, customerNumber());
	}

	private void clear() {
		jdbcTemplate.execute("delete from orders.orderitem");
		jdbcTemplate.execute("delete from orders.orderdata");
		jdbcTemplate.execute("delete from orders.customer");
	}
	
	private int customerNumber() {
		return jdbcTemplate.queryForObject("select count(id) from orders.customer", Integer.class);
	}
}
