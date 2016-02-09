package com.shenko.sextant;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.Spring;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SQLHandler {
	public Connection connect = null;
	public Statement stmt = null;
	public PreparedStatement preparedStatement = null;
	public ResultSet rs = null;

	private DatabaseCredentials Credentials = new DatabaseCredentials();
	
	public void open()
	{
		// This will load the MySQL driver, each DB has its own driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
			connect = DriverManager.getConnection("jdbc:mysql://sql5.freemysqlhosting.net?" + Credentials.UserPass);
		
			stmt = connect.createStatement();
		
		} catch (ClassNotFoundException e) {
			
		}
		catch (SQLException e) {
			
			e.printStackTrace();
		}
		

	}
	
	private void close()
	{
		try {
			stmt.close();
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void executePush(String query) {
		//System.out.println("Starting SQLHandler with: "+query);
		//Connection connect = null;
		//Statement stmt = null;
		//ResultSet rs = null;
		String toReturn = null; 

	try {
		// This will load the MySQL driver, each DB has its own driver
		//Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		//System.out.println("Connecting to database...");
		//connect = DriverManager.getConnection("jdbc:mysql://sql5.freemysqlhosting.net?" + Credentials.UserPass);
	
		//stmt = connect.createStatement();
		open();
		//String lquery=query.toLowerCase(); //let's make the next statement easier.		
		stmt.executeUpdate(query); //that's Update for insert / update / delete, and executeQuery for just queries.
		//stmt.close();
		//connect.close();
		//System.out.println("executed " + query);
		close();
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
		//Connection connect = null;
		//Statement stmt = null;
		//ResultSet rs = null;
		String toReturn = null; 

	try {
		// This will load the MySQL driver, each DB has its own driver
		//Class.forName("com.mysql.jdbc.Driver");
		// Setup the connection with the DB
		//System.out.println("Connecting to database...");
		//connect = DriverManager.getConnection("jdbc:mysql://sql5.freemysqlhosting.net?" + Credentials.UserPass);
	
		//stmt = connect.createStatement();
		
		//String lquery=query.toLowerCase(); //let's make the next statement easier.		
		open();
		rs = stmt.executeQuery(query);
		if(rs.next())
			{
			toReturn = rs.getString("Name");
			}
		close();
		//stmt.close();
		//connect.close();
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
			open();
			stmt.executeUpdate(query);
			close();
			//System.out.println("done.");
			
		} catch (Exception e) {
			System.out.println("onono...writePort");
			e.printStackTrace();
		} finally {
			//close();
		}
			
	}

	public void writeSale(JsonObject item) {
		//System.out.println("Starting SQLHandler for WritePort...");
		
		try {

			/*System.out.println( "TemplateID: " + item.get("TemplateId").getAsInt() );	
			System.out.println( "Quantity:   " + item.get("Quantity").getAsInt() );	
			System.out.println( "Buy:        " + item.get("BuyPrice").getAsInt() );
			System.out.println( "Sell:       " + item.get("SellPrice").getAsInt() );
			System.out.println( "Port:        we don't know");*/
			
			
			String query = "insert into sql5105183.Sales("
				+ "PortID, "
				+ "ItemID, "
				+ "quantity, "
				+ "sellPrice, "
				+ "buyPrice, "
				+ "addedBy) "
				+ "values ("
				+ sextant.CurrentPort + ", "
				+ item.get("TemplateId").getAsInt() + ", "	
				+ item.get("Quantity").getAsInt() + ", "
				+ item.get("SellPrice").getAsInt() + ", "
				+ item.get("BuyPrice").getAsInt() + ", "
				+ "\""+sextant.playerName+"\")";
						
			
			//System.out.println(query);
			System.out.println("executing "+query);
			open();
			stmt.executeUpdate(query);
			close();
			
			//System.out.println("done.");
			
		} catch (Exception e) {
			System.out.println("onono...writeSale");
			e.printStackTrace();
		} finally {
			//close();
		}
			
	}
	

	
	public void writeProduction(JsonArray productionData) { //TODO: don't replicate existing port production data. 
		//System.out.println("Starting SQLHandler for WritePort...");
		String query=null;
		JsonObject item=null;
		int myCount=0;
		
		try {

			/*System.out.println( "TemplateID: " + item.get("TemplateId").getAsInt() );	
			System.out.println( "Quantity:   " + item.get("Quantity").getAsInt() );	
			System.out.println( "Buy:        " + item.get("BuyPrice").getAsInt() );
			System.out.println( "Sell:       " + item.get("SellPrice").getAsInt() );
			System.out.println( "Port:        we don't know");*/
			open(); //first, we're going to check and see if it exists already
			rs=stmt.executeQuery("select count(*) from sql5105183.Production where PortID="+sextant.CurrentPort);
			if(rs.next())
			{
			myCount = Integer.parseInt(rs.getString("count(*)"));
			}
			close();
			System.out.println("Production info found in log, table reveals "+myCount+" existing records.");
			if(myCount==0)
			{
			
				for (int i=0; i < productionData.size(); i++)
				{
					item = productionData.get(i).getAsJsonObject();
					query = "insert into sql5105183.Production("
						+ "PortID, "
						+ "ItemID, "
						+ "quantity, "
						+ "AddedBy) "
						+ "values ("
						+ sextant.CurrentPort + ", "
						+ item.get("Key").getAsInt() + ", "	
						+ item.get("Value").getAsInt() + ", "
						+ "\""+sextant.playerName+"\")";
								
					
					//System.out.println(query);
					System.out.println("executing "+query);
		
					stmt.executeUpdate(query);
				}
			}
			close();
			
			//System.out.println("done.");
			
		} catch (Exception e) {
			System.out.println("onono...writeProduction");
			e.printStackTrace();
		} finally {
			//close();
		}
			
	}
	
	/**verifyItem
	 * fuck yeah bitches
	 */
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
			open();
			rs = stmt.executeQuery(query);
			if(rs.next())
			{
			name = rs.getString("Name");
			}
			
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
					stmt.executeUpdate(query);
					System.out.println("Thanks! Item name updated / added.");
				}
				else
				{
					System.out.println("Empty box. Enjoy getting that box again and again.");
				}
			
				
								
			}
			close();
			
		} catch (Exception e) {
			System.out.println("onono...");
			e.printStackTrace();
		} finally {
		}

		return name;
	}	
	
	
	/*
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
*/
		
	public HashMap<Integer, Port> getPorts()
	{
		//returns a HashMap of Ports with basic info. Does not include production or sales.
		
		HashMap<Integer, Port> toReturn = new HashMap<Integer, Port>();
		
		
		open();
		
		try {
			rs=stmt.executeQuery("SELECT * FROM sql5105183.Ports");
			while(rs.next())
			{
				String[] fields = {"Id", "name", "x", "y", "nation", "capital", "regional", "depth", "size", "contested", "startcity", "capturer", "modified", "addedBy"};
				
				for(String element:fields)
				{
					//toAdd.addProperty(element, rs.getString(element));
					Port tempPort = new Port(rs.getInt("Id"));
					tempPort.ID=rs.getInt("Id");
					tempPort.name = rs.getString("name");
					tempPort.x = rs.getInt("x");
					tempPort.y = rs.getInt("y");
					tempPort.nation = rs.getInt("nation");
					tempPort.capital = rs.getInt("capital");
					tempPort.regional = rs.getInt("regional");
					tempPort.depth = rs.getInt("depth");
					tempPort.size = rs.getInt("size");
					tempPort.contested = rs.getInt("contested");
					tempPort.startcity = rs.getInt("startcity");
					tempPort.capturer = rs.getString("capturer");
					tempPort.modified = rs.getString("modified");
					tempPort.AddedBy = rs.getString("addedBy");
					toReturn.put(tempPort.ID, tempPort );
					
				}
				
				
				
				
			}
		
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
		return toReturn;
		
		
	}
	
	/*OK, in one fell swoop, we're going to read the whole production table, and push all the results to the ports
	 * Here's how it looks: 
	 * read the table
	 * for each result, identify the port it belongs to, and insert a JsonObject with all the info
	 * 
	 */
	
	
	
	
	public HashMap<Integer, Port> getProduction(HashMap<Integer, Port> hm)
	{
		try {
			open();
			rs=stmt.executeQuery("SELECT * FROM sql5105183.Production");
			while(rs.next())
			{
				//construct the JsonObject
				production myProduction;
				
				myProduction = new production(rs.getInt("PortID"), 
							rs.getInt("ItemID"), rs.getInt("quantity"), 
							rs.getString("Modified"), rs.getString("AddedBy"));
				//System.out.println(myProduction);
				Port myPort=hm.get(myProduction.PortID);		
				//System.out.println(myPort);
				myPort.productionArray.add(myProduction);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
		return hm;
	}
	
	public HashMap<Integer, Port> getSales(HashMap<Integer, Port> hm)
	{
		try {
			open();
			rs=stmt.executeQuery("SELECT * FROM sql5105183.Sales");
			while(rs.next())
			{
				sale mySale;
				
				mySale = new sale(rs.getInt("PortID"), 
							rs.getInt("ItemID"),  rs.getInt("quantity"), rs.getInt("sellPrice"), rs.getInt("buyPrice"), 
							rs.getString("Modified"), rs.getString("AddedBy"));
				//System.out.println(mySale);
				Port myPort=hm.get(mySale.PortID);		
				//System.out.println(myPort);
				myPort.salesArray.add(mySale);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
		return hm;
	}
	
	public void getItemTable(ItemTable items)
	{
		try {
			open();
			rs=stmt.executeQuery("SELECT * FROM sql5105183.ItemIDs");
			while(rs.next())
			{
				items.add(rs.getInt("ItemID"), rs.getString("Name"), rs.getString("Modified"), rs.getString("AddedBy"));
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		close();
	}
	
		public void fillDB() {
			String baseName=null;
			int baseID=0;
		try {
			open();
			rs = stmt.executeQuery("SELECT * FROM sql5105183.ItemIDs WHERE name LIKE  '%Fine%'"); //that's Update for insert / update / delete, and executeQuery for just queries.
			//System.out.println("executed " + query);
			
			List<String> Names = new ArrayList<String>();
			List<Integer> IDs = new ArrayList<Integer>();
			while(rs.next())
			{
				baseName=rs.getString("Name");
				baseName=baseName.substring(0,baseName.indexOf("-")-1); //fine
				baseID=rs.getInt("ItemID")-4; //fine
				Names.add(baseName);
				IDs.add(baseID);
			}
			
			rs = stmt.executeQuery("SELECT * FROM sql5105183.ItemIDs WHERE name LIKE  '%Common%'"); //that's Update for insert / update / delete, and executeQuery for just queries.
			
			while(rs.next())
			{
				baseName=rs.getString("Name");
				baseName=baseName.substring(0,baseName.indexOf("-")-1); //common also
				baseID=rs.getInt("ItemID")-1; //common
				Names.add(baseName);
				IDs.add(baseID);
			}
			
			rs = stmt.executeQuery("SELECT * FROM sql5105183.ItemIDs WHERE name LIKE  '%Mastercraft%'"); //that's Update for insert / update / delete, and executeQuery for just queries.
			
			while(rs.next())
			{
				baseName=rs.getString("Name");
				baseName=baseName.substring(0,baseName.indexOf("-")-1); //common also
				baseID=rs.getInt("ItemID")-3; //MC
				Names.add(baseName);
				IDs.add(baseID);
			}
				
			for(int i=0; i<Names.size(); i++)
			{
			
				baseName=Names.get(i);
				baseID=IDs.get(i);
				//stmt.executeUpdate(
				//System.out.println(
						stmt.executeUpdate("insert ignore sql5105183.ItemIDs (ItemId, Name, AddedBy) values (" + baseID + ", '" + baseName + "', 'abso-fillDB') ");
				
				//System.out.println(
				stmt.executeUpdate(
					"insert ignore sql5105183.ItemIDs (ItemId, Name, AddedBy) values (" + (baseID+1) + ", '" + baseName +" - Common" +"', 'abso-fillDB')");
				//System.out.println(
				stmt.executeUpdate(
					"insert ignore sql5105183.ItemIDs (ItemId, Name, AddedBy) values (" + (baseID+3) + ", '" + baseName + " - Mastercraft"+"', 'abso-fillDB')");
				//System.out.println(
				stmt.executeUpdate(
					"insert ignore sql5105183.ItemIDs (ItemId, Name, AddedBy) values (" + (baseID+4) + ", '" + baseName + " - Fine" + "', 'abso-fillDB')");
				//System.out.println("insert into sql5105183.ItemIDs (ItemId, Name, AddedBy) values (" + (baseID+2) + ", '" + baseName + " - Exceptional" + "', 'abso-fillDB')");
				
				
				
			}
			System.out.println("filled.");
			close();

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

		public void hi(String name)
		{
			open();
			try {
				stmt.executeUpdate("insert ignore sql5105183.users (name, version) values ('" + name + "', '" + sextant.version + "')");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			close();
		}
		
}
