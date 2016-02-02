package com.shenko.sextant;

import com.google.gson.*;

public class LogParser {

	JsonParser Parser;
	
	int CurrentPort;
	
	public LogParser()
	{
		Parser = new JsonParser();
		
	}
	
	public void ParsePortData(String inData)
	{
		// Parse a port data block	
		JsonElement Element = Parser.parse(inData);
		
		if (Element.isJsonObject())
		{
			// Get json objects
			JsonObject Port = Element.getAsJsonObject();
			JsonObject PortPosition = Port.get("Position").getAsJsonObject();
			
			int ID = Port.get("Id").getAsInt();
			String Name = Port.get("Name").getAsString();							
			int PositionX = PortPosition.get("x").getAsInt();
			int PositionZ = PortPosition.get("z").getAsInt();
			int Nation = Port.get("Nation").getAsInt();	
			boolean Capital = Port.get("Capital").getAsBoolean();
			boolean NationStartingPort = Port.get("NationStartingPort").getAsBoolean();
			int Depth = Port.get("Depth").getAsInt();
			int Contested = Port.get("Contested").getAsInt();
			
			// Update player current port maybe
			if (CurrentPort != ID)
			{
				CurrentPort = ID;
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
		JsonElement Element = Parser.parse(inData);
		
		if (Element.isJsonObject())
		{
			// Get json objects
			JsonObject Player = Element.getAsJsonObject();
			JsonObject PlayerPosition = Player.get("WorldPosition").getAsJsonObject();
			JsonArray PlayerOutposts = Player.get("PortContainers").getAsJsonArray();
			
			String Name = Player.get("Name").getAsString();
			int Nation = Player.get("Nation").getAsInt();
			int WorldPositionX = PlayerPosition.get("x").getAsInt();
			int WorldPositionY = PlayerPosition.get("y").getAsInt();
			int ShipTemplateID = Player.get("CurrentShipItemTemplateID").getAsInt();
			int CurrentPortID = Player.get("CurrentPortId").getAsInt();
			
			// Iterate outpost array
			for (int i=0; i < PlayerOutposts.size(); i++)
			{
				// Add to list of player outposts in DB here
				JsonObject OutpostObject = PlayerOutposts.get(i).getAsJsonObject();
				int OutpostPortID = OutpostObject.get("PortId").getAsInt();
			}
			
			// Update current port more
			if (CurrentPort != CurrentPortID)
			{
				CurrentPort = CurrentPortID;
			}
			
		}
	}
	
	public void ParseShopData(String inData)
	{
		// Parse a block of port trade data
		// Expecting a JSON array here, anything else is cocked up
		JsonElement Element = Parser.parse(inData);
		
		if (Element.isJsonArray())
		{
			JsonArray ShopData = Element.getAsJsonArray();
			
			for (int i=0; i < ShopData.size(); i++)
			{
				JsonObject Data = ShopData.get(i).getAsJsonObject();
				
				int TemplateID = Data.get("TemplateId").getAsInt();
				int Quantity = Data.get("Quantity").getAsInt();
				int SellPrice = Data.get("SellPrice").getAsInt();
				int BuyPrice = Data.get("BuyPrice").getAsInt();
				
				// Have to just assume CurrentPort is where we are for this
				// So send the data to the DB
			}
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
	}
}
