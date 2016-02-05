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

	public String toString() {
		return "Sale: PortID"+PortID+" ItemID: "+ItemID+ " ("+sellPrice+", "+buyPrice+"), "+modified+"("+addedBy+")"; //TODO add more shit here
	}
}

