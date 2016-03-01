package com.shenko.sextant;

import java.util.ArrayList;

public class missionList {
	public ArrayList<Integer> x= new ArrayList<Integer>();
	public ArrayList<Integer> y= new ArrayList<Integer>();
	public ArrayList<Integer> rank= new ArrayList<Integer>();
	public ArrayList<Integer> xp= new ArrayList<Integer>();
	public ArrayList<Integer> gold= new ArrayList<Integer>();


	public missionList()
	{
		reset();
	}
	
	public void add(int x,int y,int rank,int xp,int gold)
	{
		this.x.add(x);
		this.y.add(y);
		this.rank.add(rank);
		this.xp.add(xp);
		this.gold.add(gold);
		sextant.println("Mission added: "+x+" "+y+" "+rank);
	}

	public void reset()
	{
		x.clear();
		y.clear();
		rank.clear();
		xp.clear();
		gold.clear();
		sextant.println("missions reset");

	}
	
	public String toString()
	{
		return "Missions: "+size();
	}
	
	public int size()
	{
		return x.size();
	}

}