package com.shenko.sextant;

import java.util.ArrayList;

import com.google.gson.JsonObject;

/*This is the Port object. It contains information relating to an individual port.
 * 
 * location
 * production()
 * sales()
 * name
 * ID
 */

public class Port {

	public String name=null;
	public int ID=0;
	public int x=0, y=0, nation=0, capital=0, regional=0, depth=0, size=0, contested=0, startcity=0;
	public String capturer=null, modified = null, AddedBy=null;
	
	public Port(int passedId)
	{
		ID=passedId;
	}
	
	public ArrayList<JsonObject> production()
	{
		return null;
	}
	
	public ArrayList<JsonObject> sales()
	{
		return null;
	}
	
	
	
}
