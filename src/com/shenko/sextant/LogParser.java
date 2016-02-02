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
			
			System.out.println( "Port Name: " + Port.get("Name").getAsString() );	
			System.out.println( "Port Nation: " + Port.get("Nation").getAsInt() );	
			System.out.println( "Port ID: " + Port.get("Id").getAsInt() );
			System.out.println( "Port Position X " + PortPosition.get("x").getAsInt() );
			System.out.println( "Port Position Z " + PortPosition.get("z").getAsInt() );
		}
		else
		{
			System.out.println("kill me now");
		}
	}
	
}
