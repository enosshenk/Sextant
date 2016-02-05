package com.shenko.sextant;

public class production {

	public int PortID, ItemID, quantity;
	public String Modified, AddedBy;
	
	public production(int PortID, int ItemID,int quantity,String Modified,String AddedBy)
	{
		this.PortID=PortID;
		this.ItemID=ItemID;
		this.quantity=quantity;
		this.Modified=Modified;
		this.AddedBy=AddedBy;
	}
	
	public String toString() {
        return "Production: PortID: "+PortID+" ItemID: "+ItemID+" Quantity: "+quantity+" AddedBy: "+AddedBy+" Modified: "+Modified;
    }
	
}
