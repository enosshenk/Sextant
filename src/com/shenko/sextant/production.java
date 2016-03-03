package com.shenko.sextant;

public class production {

	public int PortID, ItemID, quantity;
	public String modified, AddedBy;
	
	public production(int PortID, int ItemID,int quantity,String modified,String AddedBy)
	{
		this.PortID=PortID;
		this.ItemID=ItemID;
		this.quantity=quantity;
		this.modified=modified;
		this.AddedBy=AddedBy;
	}
	
	public String toString() {
		return quantity+" "+sextant.portsHash.get(PortID).name+ " "+modified; //modified so we can use this text for the item tree, yet retain the other properties of the object
        //return "Production: PortID: "+PortID+" ItemID: "+ItemID+" Quantity: "+quantity+" AddedBy: "+AddedBy+" Modified: "+modified;
    }

	public String shortString() {
		return Integer.toString(quantity);
	}
	
}
