package com.shenko.sextant;

import java.sql.Timestamp;

public class labor { //record labor hours, etc
	public Timestamp lastTime; //time of last labor hour received from log file
	public int lastHours; //last amount of labor hours read from log file
	
	public void insertLabor(Timestamp time, int hours){
		lastTime=time;
		lastHours=hours;
	}
	
	
	/**
	 * Recalculate the amount of total labor hours the player has
	 * @return amount of labor hours
	 */
	public int updateLabor(){
		 java.util.Date now= new java.util.Date();
		 int toReturn=0;
		 
		 toReturn=(int)((now.getTime()-lastTime.getTime())*42/60/60/1000)+lastHours; //42 per hour
		 sextant.println("Labor hours: "+toReturn);
		return toReturn; //return the amount of labor hours
	}
	
	public String toString(){
		return "Labor hours: "+updateLabor();
	}
	
	
}
