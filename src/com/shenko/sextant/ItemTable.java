package com.shenko.sextant;

import java.util.ArrayList;

public class ItemTable {
//this is where we hold all the item names and stuff
	private ArrayList<Integer> ItemIDs= new ArrayList<Integer>();
	private ArrayList<String> Names= new ArrayList<String>();
	private ArrayList<String> Modifieds= new ArrayList<String>();
	private ArrayList<String> AddedBys= new ArrayList<String>();
	
	public ItemTable()
	{
		sextant.mySql.getItemTable(this);
	}

	public void add(int ItemID, String Name, String Modified, String AddedBy)
	{
		ItemIDs.add(ItemID);
		Names.add(Name);
		Modifieds.add(Modified);
		AddedBys.add(AddedBy);
	}
	
	public String get(int ItemID)
	{
		return Names.get(ItemIDs.indexOf(ItemID)); 
	}
	
	public int find(String Name)
	{
		try {
			return ItemIDs.get(Names.indexOf(Name));
		} catch (Exception e) {
			sextant.println("Tried to find "+Name+" but failed because "+Names.indexOf(Name));
			e.printStackTrace();
		}
		return -1;
	}
	
	public int count()
	{
		return ItemIDs.size();
	}
	
	public int index(int index)
	{
		return ItemIDs.get(index);
	}
	
	public String toString() {
		String toReturn=null;
		toReturn="The Item Table has : "+ItemIDs.size()+" items:";
		for(int i=0; i<ItemIDs.size(); i++)
		{
			toReturn=toReturn+"\n"+ItemIDs.get(i)+" "+Names.get(i)+" "+Modifieds.get(i)+" "+AddedBys.get(i);
		}
		return toReturn;
	}
	
	public boolean has(int itemID){
		
		return ItemIDs.contains(itemID);
	}
	
}
