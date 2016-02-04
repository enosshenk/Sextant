package com.shenko.sextant;

public class GridPoint {

	// Just an X Y coordinate point
	
	public int X;
	public int Y;
	
	public GridPoint(int inX, int inY)
	{
		X = inX;
		Y = inY;
	}
	
	public GridPoint()
	{
		X = 0;
		Y = 0;
	}
	
	public void SetLoc(int inX, int inY)
	{
		X = inX;
		Y = inY;		
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
