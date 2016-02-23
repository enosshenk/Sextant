package com.shenko.sextant;

public class GridPoint {

	// Just an X Y coordinate point
	
	public int X;
	public int Y;
	boolean isNull;
	
	public GridPoint(int inX, int inY)
	{
		X = inX;
		Y = inY;
		isNull=false;
	}
	
	public GridPoint()
	{
		X = 0;
		Y = 0;
		isNull=true;
	}
	
	public void SetLoc(int inX, int inY)
	{
		X = inX;
		Y = inY;
		isNull=false;
	}
	
	public void SetX(int inX)
	{
		X = inX;
	}
	
	public void SetY(int inY)
	{
		Y = inY;
	}
}
