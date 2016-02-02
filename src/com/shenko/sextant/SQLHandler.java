package com.shenko.sextant;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class SQLHandler {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	
	public void connect() {
		System.out.println("Starting SQLHandler...");
		
	//	connect();
	//}
	//public void connect() throws Exception {
	try {
		// This will load the MySQL driver, each DB has its own driver
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		System.out.println("Connecting to database...");
		connect = DriverManager
			.getConnection("jdbc:mysql://sql5.freemysqlhosting.net?"
			+ "user=sql5105183&password=sCHT8AcwwT");
		/*connect = DriverManager
				.getConnection("jdbc:mysql://sql5.freemysqlhosting.net","sql5105183","sCHT8AcwwT");*/
			
		
		//return connect;
		

		preparedStatement = connect
				.prepareStatement("insert into sql5105183.Ports(Id, name, x, y, nation) values (?, ?, ?, ?, ?)");
		// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
		// Parameters start with 1
		preparedStatement.setString(1, "9999");
		preparedStatement.setString(2, "TestCity");
		preparedStatement.setString(3, "-217801");
		preparedStatement.setString(4, "-168800");
		preparedStatement.setString(5, "1");
		preparedStatement.executeUpdate();

		preparedStatement.close();
		connect.close();
		System.out.println("done.");
		//preparedStatement = connect
		//		.prepareStatement("SELECT * from sql5105183.Ports");
		//resultSet = preparedStatement.executeQuery();
		//System.out.println(resultSet);
	  }catch(SQLException se){
	      //Handle errors for JDBC
		  System.out.println("ono...");
	      se.printStackTrace();
	} catch (Exception e) {
		System.out.println("onono...");
		e.printStackTrace();
	} finally {
		close();
	}
		
	}
	
	

	
	
	public void readDataBase() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/feedback?"
					+ "user=sqluser&password=sqluserpw");

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			resultSet = statement
					.executeQuery("select * from feedback.comments");
			writeResultSet(resultSet);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert intofeedback.comments values (default, ?, ?, ?, ? , ?, ?)");
			// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
			// Parameters start with 1
			preparedStatement.setString(1, "Test");
			preparedStatement.setString(2, "TestEmail");
			preparedStatement.setString(3, "TestWebpage");
			preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
			preparedStatement.setString(5, "TestSummary");
			preparedStatement.setString(6, "TestComment");
			preparedStatement.executeUpdate();

			preparedStatement = connect
					.prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from feedback.comments");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

			// Remove again the insert comment
			preparedStatement = connect
					.prepareStatement("delete from feedback.comments where myuser= ? ; ");
			preparedStatement.setString(1, "Test");
			preparedStatement.executeUpdate();
	
			resultSet = statement
					.executeQuery("select * from feedback.comments");
			writeMetaData(resultSet);
	
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
	// Now get some metadata from the database
	// Result set get the result of the SQL query
	
		System.out.println("The columns in the table are: ");
	
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for(int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
			System.out.println("Column " +i+ " "+ resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writePort(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1
			// e.g. resultSet.getSTring(2);
			String user = resultSet.getString("myuser");
			String website = resultSet.getString("webpage");
			String summery = resultSet.getString("summery");
			Date date = resultSet.getDate("datum");
			String comment = resultSet.getString("comments");
			System.out.println("User: " + user);
			System.out.println("Website: " + website);
			System.out.println("Summery: " + summery);
			System.out.println("Date: " + date);
			System.out.println("Comment: " + comment);
		}
	}

		// You need to close the resultSet
		private void close() {
			try {
				if (resultSet != null) {
					resultSet.close();
				}

				if (statement != null) {
					statement.close();
				}

				if (connect != null) {
					connect.close();
				}
			} catch (Exception e) {

			}
		}

} 
