package com.shenko.sextant;

public class sale {

	/*This is the sale object. It contains information relating to an individual item offered for sale at a specific port..
	 *
	 * 
	 * location
	 * production()
	 * sales()
	 * name
	 * ID
	 */
private int id;
public int PortID, ItemID, quantity, sellPrice, buyPrice;
public String modified, addedBy;
public Port port; //set in sqlhandler. such a bad practice.


	public sale(int PortID, int ItemID, int quantity, int sellPrice, int buyPrice, String modified, String addedBy)
	{
		this.PortID=PortID;
		this.ItemID=ItemID;
		this.quantity=quantity;
		this.sellPrice=sellPrice;
		this.buyPrice=buyPrice;
		this.modified=modified;
		this.addedBy=addedBy;
	}
	
	public sale()
	{
		this.PortID=0;
		this.ItemID=0;
		this.quantity=0;
		this.sellPrice=0;
		this.buyPrice=0;
		this.modified=null;
		this.addedBy=null;
	}

	public String toString() {
		return quantity+" b"+ buyPrice+" s"+sellPrice+" "+port.name+ " "+modified;
		//return "Sale: PortID"+PortID+" ItemID: "+ItemID+ " ("+sellPrice+", "+buyPrice+"), "+modified+"("+addedBy+")"; //TODO add more shit here
	}
	
	public String shortString(){
		return quantity+" b"+ buyPrice+" s"+sellPrice+" "+modified;
	}
}


