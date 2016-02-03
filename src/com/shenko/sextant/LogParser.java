package com.shenko.sextant;

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
		//System.out.println("found new sale: " + inData);
		
		while(inData.contains("{"))
		{
			if((inData.indexOf("{")>0) && (inData.indexOf("}")+1>inData.indexOf("{")) ) // trap bad indices
			{
				String sale = inData.substring(inData.indexOf("{"), inData.indexOf("}")+1); // set sale to a probably-valid JSON object
				//System.out.println("sale: " + sale);
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
							/*System.out.println( "TemplateID: " + item.get("TemplateId").getAsInt() );	
							System.out.println( "Quantity:   " + item.get("Quantity").getAsInt() );	
							System.out.println( "Buy:        " + item.get("BuyPrice").getAsInt() );
							System.out.println( "Sell:       " + item.get("SellPrice").getAsInt() );
							System.out.println( "Port:        we don't know");
							*/
							SQLHandler mySQL = new SQLHandler();
							String name = mySQL.verifyItem(item);
	
							System.out.println( "Item:       " + name +"\n" );	
							mySQL.writeSale(item);
						}
					}
					else
					{
						System.out.println("kill me now");
					}
				}
			}
		}
	}	
	
	public void ParsePortData(String inData)
	{
		// Parse a port data block
		
	
		JsonElement Element = Parser.parse(inData);
		
		//System.out.println("found new Port");
		
		if (Element.isJsonObject())
		{
			// Get json objects
			JsonObject Port = Element.getAsJsonObject();
			JsonObject PortPosition = Port.get("Position").getAsJsonObject();
			
	
			SQLHandler mySQL = new SQLHandler();
			mySQL.writePort(Port);
			
			/*System.out.println( "Port Name: " + Port.get("Name").getAsString() );	
			System.out.println( "Port Nation: " + Port.get("Nation").getAsInt() );	
			System.out.println( "Port ID: " + Port.get("Id").getAsInt() );
			System.out.println( "Port Position X " + PortPosition.get("x").getAsInt() );
			System.out.println( "Port Position Z " + PortPosition.get("z").getAsInt() );*/

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
			System.out.println("Port JSON data invalid");
		}
	}
	
	public void ParsePlayerData(String inData)
	{
		// Parse player data block
		// This is a lot of shit
		

		inData = inData.substring(inData.indexOf("{")); // let's just get the JSON
		//System.out.println(inData);
		JsonElement Element = Parser.parse(inData);
		

		System.out.println("Parsing player data");
		
		if (Element.isJsonObject())
		{
			// Get json objects
			JsonObject Player = Element.getAsJsonObject();
			JsonObject PlayerPosition = Player.get("WorldPosition").getAsJsonObject();
			JsonArray PlayerOutposts = Player.get("PortContainers").getAsJsonArray();
			
			String Name = Player.get("Name").getAsString();
			//System.out.println("Player Name: " + Name);
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
				System.out.println("CurrentPort: "+sextant.CurrentPort);
			}
			
		}
		else
		{
			System.out.println("Player JSON data invaid");
		}
	}

	
	public void ParsePortProductionData(String inData)
	{
		// Parse port consumption and production data
		// Expecting a JSON array here
		JsonElement Element = Parser.parse(inData);
		
		if (Element.isJsonArray())
		{
			JsonArray ProductionData = Element.getAsJsonArray();
			
			for (int i=0; i < ProductionData.size(); i++)
			{
				JsonObject Data = ProductionData.get(i).getAsJsonObject();
				
				int TemplateID = Data.get("Key").getAsInt();
				int Value = Data.get("Value").getAsInt();
				
				// Value is number produced or consumed per hour. Game displays this as per day
				// Do we * 24 here or somewhere else?
			}
		}
		else
		{
			System.out.println("Port Production/Consumption JSON data invalid or was not JSONArray");
		}
	}
}
