package com.shenko.sextant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

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
	public ArrayList<production> productionArray=new ArrayList<production>();
	public ArrayList<sale> salesArray=new ArrayList<sale>();


	public Port(int ID)
	{
		this.ID=ID;
	}

	public String toString() {
		return "Port: "+ID+" "+name+ " ("+x+", "+y+") Productions:"+productionArray.size()+" Sales:"+productionArray.size(); //TODO add more shit here
	}

	public sale getSale(int ItemID)
	{
		sale toReturn=null, tempSale;
		boolean found=false; //did we find at least 1 result
		if(salesArray.size()>0)
		{
			for(int i=0; i<salesArray.size(); i++)	 
			{

				tempSale=salesArray.get(i);

				//sextant.println(i+ " "+tempSale.shortString()+" : "+ toReturn.shortString());
				if(tempSale.ItemID==ItemID)
				{
					if(found==false)
					{
						found = true;
						toReturn=tempSale;
					}else{
						if(tempSale.modified.compareTo(toReturn.modified)>0)
						{
							toReturn = tempSale;
						}
					}
				}
			}
		}


		if(found)
		{
			sextant.println(name+ " "+ toReturn.shortString());

		}else{
			sextant.println("no sales for "+ItemID+" at "+name);
		}

		return toReturn;
	}

	public production getProduction(int itemID) {
		production toReturn=null;
		production tempProd=null;
		boolean found=false; //did we find at least 1 result
		if(productionArray.size()>0)
		{
			for(int i=0; i<productionArray.size(); i++)	 
			{

				tempProd=productionArray.get(i);

				//sextant.println(i+ " "+tempSale.shortString()+" : "+ toReturn.shortString());
				if(tempProd.ItemID==itemID)
				{
//					if(found==false)
//					{
						found = true;
						toReturn=tempProd;
//					}else{
//						if(tempProd.modified.compareTo(toReturn.modified)>0)
//						{
//							toReturn = tempProd;
//						}
//					}
				}
			}
		}


		if(found)
		{
			sextant.println(name+ " "+ toReturn.shortString());

		}else{
			sextant.println("no production for "+itemID+" at "+name);
		}

		return toReturn;
	}


}


