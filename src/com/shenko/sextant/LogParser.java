package com.shenko.sextant;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.*;

public class LogParser {

	JsonParser Parser;
	
	
	public LogParser()
	{
		Parser = new JsonParser();
		
	}
	
	public void ParseSaleData(String inData) //, int portID) //passed the line with multiple items for sale. We'll also need the port location, so add that 
	{
		// Parse a port data block
		//sextant.println("found new sale: " + inData);
		
		while(inData.contains("{"))
		{
			if((inData.indexOf("{")>0) && (inData.indexOf("}")+1>inData.indexOf("{")) ) // trap bad indices
			{
				String sale = inData.substring(inData.indexOf("{"), inData.indexOf("}")+1); // set sale to a probably-valid JSON object
				//sextant.println("sale: " + sale);
				inData=inData.substring(inData.indexOf("}")+1); //(no longer) recursively call this function with the remaining string, so sale gets processed below, and we run the rest of this shit through this function again
				if((sale != null) && (sale.length()>0))
				{
					JsonElement Element = Parser.parse(sale);
					if (Element.isJsonObject())
					{
						// Get json objects
						JsonObject item = Element.getAsJsonObject();
						if (!item.isJsonNull())
						{
							/*sextant.println( "TemplateID: " + item.get("TemplateId").getAsInt() );	
							sextant.println( "Quantity:   " + item.get("Quantity").getAsInt() );	
							sextant.println( "Buy:        " + item.get("BuyPrice").getAsInt() );
							sextant.println( "Sell:       " + item.get("SellPrice").getAsInt() );
							sextant.println( "Port:        we don't know");
							*/
							
							String name = sextant.mySql.verifyItem(item);
	
							sextant.println( "Item:       " + name +"\n" );	
							sextant.mySql.writeSale(item);
						}
					}
					else
					{
						sextant.println("kill me now");
					}
				}
			}
		}
	}	
	
	public void ParsePortData(String inData)
	{
		// Parse a port data block
		
	
		JsonElement Element = Parser.parse(inData);
		
		//sextant.println("found new Port");
		
		if (Element.isJsonObject())
		{
			// Get json objects
			JsonObject Port = Element.getAsJsonObject();
			JsonObject PortPosition = Port.get("Position").getAsJsonObject();
			
	
			
			sextant.mySql.writePort(Port);
			
			/*sextant.println( "Port Name: " + Port.get("Name").getAsString() );	
			sextant.println( "Port Nation: " + Port.get("Nation").getAsInt() );	
			sextant.println( "Port ID: " + Port.get("Id").getAsInt() );
			sextant.println( "Port Position X " + PortPosition.get("x").getAsInt() );
			sextant.println( "Port Position Z " + PortPosition.get("z").getAsInt() );*/

			int ID = Port.get("Id").getAsInt(); 
			//got a little mixed up on the github and had to merge. Is this shit old, or did you make these changes and recommit?
			// ES - Just assigning these values to variables for ease of use later
			String Name = Port.get("Name").getAsString();							
			int PositionX = PortPosition.get("x").getAsInt();
			int PositionZ = PortPosition.get("z").getAsInt();
			int Nation = Port.get("Nation").getAsInt();	
			boolean Capital = Port.get("Capital").getAsBoolean();
			boolean NationStartingPort = Port.get("NationStartingPort").getAsBoolean();
			int Depth = Port.get("Depth").getAsInt();
			int Contested = Port.get("Contested").getAsInt();
			
			// Update player current port maybe
			if (sextant.CurrentPort != ID)
			{
				sextant.CurrentPort = ID; //TODO did you want to do more here? if not, we can delete that if statement and just leave this line.
			}
		}
		else
		{
			sextant.println("Port JSON data invalid");
		}
	}
	
	public void ParsePlayerData(String inData)
	{
		// Parse player data block
		// This is a lot of shit
		

		inData = inData.substring(inData.indexOf("{")); // let's just get the JSON
		//sextant.println(inData);
		JsonElement Element = Parser.parse(inData);
		

		sextant.println("Parsing player data");
		
		if (Element.isJsonObject())
		{
			// Get json objects
			JsonObject Player = Element.getAsJsonObject();
			JsonObject PlayerPosition = Player.get("WorldPosition").getAsJsonObject();
			JsonArray PlayerOutposts = Player.get("PortContainers").getAsJsonArray();
			
			String Name = Player.get("Name").getAsString();
			//sextant.println("Player Name: " + Name);
			sextant.setPlayerName(Name);
			
			int Nation = Player.get("Nation").getAsInt();
			int WorldPositionX = PlayerPosition.get("x").getAsInt();
			int WorldPositionY = PlayerPosition.get("y").getAsInt();
			
			sextant.frame.setLoc(PlayerPosition.get("x").getAsInt(), PlayerPosition.get("z").getAsInt());
			
			
			int ShipTemplateID = Player.get("CurrentShipItemTemplateId").getAsInt();
			int CurrentPortID = Player.get("CurrentPortId").getAsInt();
			
			// Iterate outpost array
			for (int i=0; i < PlayerOutposts.size(); i++)
			{
				// Add to list of player outposts in DB here
				JsonObject OutpostObject = PlayerOutposts.get(i).getAsJsonObject();
				int OutpostPortID = OutpostObject.get("PortId").getAsInt();
			}
			
			// Update current port more
			if (sextant.CurrentPort != CurrentPortID)
			{
				sextant.CurrentPort = CurrentPortID;
				sextant.println("CurrentPort: "+sextant.CurrentPort);
			}
			
		}
		else
		{
			sextant.println("Player JSON data invaid");
		}
	}

	
	public void ParsePortProductionData(String inData)
	{
		// Parse port consumption and production data
		// Expecting a JSON array here
		JsonElement Element;
		try {
			Element = Parser.parse(inData);
		
		
		if (Element.isJsonArray())
		{
			JsonArray ProductionData = Element.getAsJsonArray();

			sextant.mySql.writeProduction(ProductionData);
			
			for (int i=0; i < ProductionData.size(); i++)
			{
				JsonObject Data = ProductionData.get(i).getAsJsonObject();
				
				int TemplateID = Data.get("Key").getAsInt();
				int Value = Data.get("Value").getAsInt();
				
				
				//sextant.println("prod: "+TemplateID+" "+Value);
				// Value is number produced or consumed per hour. Game displays this as per day
				// Do we * 24 here or somewhere else?
			}
		}
		else
		{
			sextant.println("Port Production/Consumption JSON data invalid or was not JSONArray");
		}
		} catch (Exception e) {
			sextant.println("JSON ERROR: "+inData);
		}
	}
	
	public void ParseLaborData(String hours, String timestamp)
	{
		int hoursInt = new Integer((int) Double.parseDouble(hours));
		
		sextant.println("Labor data found: "+hours);
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); //2016-Feb-10 09:14:54.831209
		Date parsedDate;
		try {
			parsedDate = dateFormat.parse(timestamp);
		
		Timestamp timestamp1 = new Timestamp(parsedDate.getTime());
		sextant.println("timestamp1="+timestamp1+" hoursInt="+hoursInt);
		sextant.laborHours.insertLabor(timestamp1, hoursInt);
		sextant.println(sextant.laborHours.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
