package com.pduleba.loan.broker.route.dsl;

import javax.sql.DataSource;

import org.apache.camel.component.sql.SqlComponent;
import org.apache.camel.main.Main;
import org.apache.commons.dbcp.BasicDataSource;

import com.pduleba.loan.broker.route.TutorialRouteBuilder;

public class TutorialRoute {
	
	static int TOTAL_TIME = 30; // seconds
	static int WAIT_TIMEOUT = 500;
	static int LOOP_COUNT = 30*1000/WAIT_TIMEOUT;
	
	static String from = "file://data/datafile?delete=true";
	
	
	
	public static void main(String args[]) throws Exception {
		TutorialRoute route = new TutorialRoute();
		route.boot();
	}
	
    public void boot() throws Exception {
        Main main = new Main();
        
        SqlComponent sql = new SqlComponent();
        DataSource dataSource = getDataSource();
		sql.setDataSource(dataSource);
        
        main.bind("dataSource", dataSource);
		main.bind("sql", sql);
        
        main.addRouteBuilder(new TutorialRouteBuilder(from));
 
        System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
        main.run();
    }

	private DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://localhost/demo?allowMultiQueries=true");
		dataSource.setUsername("demo");
		dataSource.setPassword("demo");
		
		return dataSource;
	}
 
}
