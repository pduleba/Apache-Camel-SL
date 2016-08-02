package com.pduleba.loan.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcMultiQueries {

	public static void main(String[] args) {
		try (Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost/demo?allowMultiQueries=true", "demo", "demo"); PreparedStatement stmt = getPS(connection)) {
			
			Class.forName("com.mysql.jdbc.Driver");

			stmt.execute();
			
			System.out.println("Finished!");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static PreparedStatement getPS(Connection connection) throws SQLException {
		PreparedStatement stmt = connection
				.prepareStatement("delete from demo.CustInfo where nationalID = ?; INSERT INTO demo.CustInfo (nationalID, firstName, lastName, age, occupation) values (?, ?, ?, ?, ?);");
		
		stmt.setString(1, "A234567");
		stmt.setString(2, "A234567");
		stmt.setString(3, "Christina");
		stmt.setString(4, "Lin");
		stmt.setLong(5, 31L);
		stmt.setString(6, "Sofrware Developer");
		
		return stmt;
	}

}
