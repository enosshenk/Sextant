package com.shenko.sextant;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.Spring;

import com.google.gson.JsonObject;

public class SQLHandler {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private DatabaseCredentials Credentials = new DatabaseCredentials();
	
	private void executePush(String query) {
		//System.out.println("Starting SQLHandler with: "+query);
		Connection connect = null;
		Statement stmt = null;
		ResultSet rs = null;
		String toReturn = null; 

	try {
		// This will load the MySQL driver, each DB has its own driver
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		//System.out.println("Connecting to database...");
		connect = DriverManager.getConnection("jdbc:mysql://sql5.freemysqlhosting.net?" + Credentials.UserPass);
	
		stmt = connect.createStatement();
		
		String lquery=query.toLowerCase(); //let's make the next statement easier.		
		stmt.executeUpdate(query); //that's Update for insert / update / delete, and executeQuery for just queries.
		stmt.close();
		connect.close();
		//System.out.println("executed " + query);
	
	  }catch(SQLException se){
	      //Handle errors for JDBC
		  System.out.println("ono... SQL error");
	      se.printStackTrace();
	      	} catch (Exception e) {
		System.out.println("onono... SQLHandler");
		e.printStackTrace();
			} finally {
	}
	
	
	}
	private String executePull(String query) {
		//System.out.println("Starting SQLHandler with: "+query);
		Connection connect = null;
		Statement stmt = null;
		ResultSet rs = null;
		String toReturn = null; 

	try {
		// This will load the MySQL driver, each DB has its own driver
		Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		//System.out.println("Connecting to database...");
		connect = DriverManager.getConnection("jdbc:mysql://sql5.freemysqlhosting.net?" + Credentials.UserPass);
	
		stmt = connect.createStatement();
		
		String lquery=query.toLowerCase(); //let's make the next statement easier.		
		rs = stmt.executeQuery(query);
		if(rs.next())
			{
			toReturn = rs.getString("Name");
			}
		stmt.close();
		connect.close();
		//System.out.println("executed " + query);
	
	  }catch(SQLException se){
	      //Handle errors for JDBC
		  System.out.println("ono... SQL error");
	      se.printStackTrace();
	      return toReturn;
	} catch (Exception e) {
		System.out.println("onono... SQLHandler");
		e.printStackTrace();
		return toReturn;
	} finally {
	}
	
	return toReturn;
	}
	
	public void writePort(JsonObject Port) {
		//System.out.println("Starting SQLHandler for WritePort...");
		
		try {

			JsonObject PortPosition = Port.get("Position").getAsJsonObject();	
			
			String query = "insert into sql5105183.Ports("
				+ "Id, "
				+ "name, "
				+ "x, "
				+ "y, "
				+ "nation, "
				+ "capital, "
				+ "startcity, " // start and starting are both sql reserved keywords haha, bullshit.
				+ "regional, "
				+ "size, "
				+ "depth, "
				+ "contested, "
				+ "addedBy) "
				+ "values ("
				+ Integer.toString(Port.get("Id").getAsInt()) + ", "
				+ "\"" + Port.get("Name").getAsString() + "\", " // don't forget the quotes!
				+ Integer.toString(PortPosition.get("x").getAsInt()) + ", "
				+ Integer.toString(PortPosition.get("z").getAsInt()) + ", "
				+ Integer.toString(Port.get("Nation").getAsInt())	+ ", " 
				+ (Port.get("Capital").getAsBoolean()?1:0) +", " //neat, huh? ?1:0 returns 1 if the preceding is true, 0 if false. I think java does this anyway though. No, in this case, this is necessary
				+ (Port.get("NationStartingPort").getAsBoolean()?1:0) + ", " // No, in this case, this is necessary
				+ (Port.get("Regional").getAsBoolean()?1:0) + ", " // No, in this case, this is necessary
				+ Integer.toString(Port.get("Size").getAsInt()) + ", "
				+ Integer.toString(Port.get("Depth").getAsInt()) + ", "
				+ Integer.toString(Port.get("Contested").getAsInt()) + ", \""
				+ sextant.playerName + "\") "
				+ "ON DUPLICATE KEY UPDATE "
				+ "nation="	+ Integer.toString(Port.get("Nation").getAsInt())
				+ ", addedBy=\"" + sextant.playerName + "\""
				;
	
			
			
			//System.out.println(query);
			
				executePush(query);
			
			//System.out.println("done.");
			
		} catch (Exception e) {
			System.out.println("onono...writePort");
			e.printStackTrace();
		} finally {
			close();
		}
			
	}

	public String verifyItem(JsonObject item) { 
		//feed this an item like {"TemplateId":173,"Quantity":1,"SellPrice":9380,"BuyPrice":10318}, 
		//get back a name (even if we have to prompt the user)
		//System.out.println("Starting SQLHandler for VerifyItem...");
		String name = null;
		//ResultSet rs = null;
		String id = null;
		try {

			id = Integer.toString(item.get("TemplateId").getAsInt());		
			String query = "select Name from sql5105183.ItemIDs where ItemID="
				+ id; 
	
			//System.out.println(query);
			
			name = executePull(query);
			if(name==null || name.isEmpty())
			{
				name = (String)JOptionPane.showInputDialog(
			                    null, //frame, let's see if this works			             
			                    "You found a new item!\n"
			                    + "Please find this item and type the name.\n"
			                    + "Quantity: " + item.get("Quantity").getAsInt() 
			                    + "\nBuy: " + item.get("BuyPrice").getAsInt() 
			                    + "\nSell: "+ item.get("SellPrice").getAsInt()
			                    + "Please be careful and check for multiple items with\n"
			                    + "the same information",
			                    "Teach me, O Great One.",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    null,
			                    "item name");

				//If a string was returned, say so.
				if ((name != null) && (name.length() > 0)) {
					if (name.contains("item name"))
					{
						System.out.println("\"Item Name\" is not the name of that item. You are a bad person.");
						System.exit(1);
					}
					query = "insert into sql5105183.ItemIDs (ItemID, Name, addedBy) values ("+ id +", \""+ name +"\", \""+sextant.playerName+"\") "
							+ "on duplicate key update Name=\""+name+"\", addedBy=\""+sextant.playerName+"\"";
					executePush(query);
					System.out.println("Thanks! Item name updated / added.");
				}
				else
				{
					System.out.println("Empty box. Enjoy getting that box again and again.");
				}
			
				
								
			}
			
		} catch (Exception e) {
			System.out.println("onono...");
			e.printStackTrace();
		} finally {
		}

		return name;
	}	
	
	public void readDataBase() throws Exception { //this is an example i pasted in here, none of this shit works yet.
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
			//writeResultSet(resultSet);

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect
					.prepareStatement("insert intofeedback.comments values (default, ?, ?, ?, ? , ?, ?)");
			// "myuser, webpage, datum, summery, COMMENTS from feedback.comments");
			// Parameters start with 1
			preparedStatement.setString(1, "Test");
			preparedStatement.setString(2, "TestEmail");
			preparedStatement.setString(3, "TestWebpage");
			preparedStatement.setString(5, "TestSummary");
			preparedStatement.setString(6, "TestComment");
			preparedStatement.executeUpdate();

			preparedStatement = connect
					.prepareStatement("SELECT myuser, webpage, datum, summery, COMMENTS from feedback.comments");
			resultSet = preparedStatement.executeQuery();
			//writeResultSet(resultSet);

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
