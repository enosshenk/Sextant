package com.shenko.sextant;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class sextant {
	
	public static String version ="0.2";

	public static LogFileHandler Handler;
	public static SQLHandler mySql;
	public static Window frame;
	public static String playerName;
	public static ItemTable items;
	public static int logStatus; 
	public static boolean GUILoaded;
		//this will be 0 until we hit EOF and then receive a new message. This indicate we've got an active log file.
		//then it'll be 1 when reading, and 2 when EOF.
	public static int CurrentPort;
	public static boolean hi=false;
	public static HashMap<Integer, Port> portsHash;	
	public static String LogDirectory = "C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Naval Action\\logs";
	
	public static void main(String [] args) {
		System.out.println("Started...");
		
		GUILoaded=false;
		CurrentPort=0;
		logStatus=0;
		
		mySql = new SQLHandler();
		
		
		items= new ItemTable();
		//System.out.println(items);
		
		portsHash = mySql.getPorts(); //TODO so we should 1) load gui including map, 2) read log, 3) getPorts, 4) push ports to the GUI
		 //here's some hash usage examples:
		
		 Iterator<Integer> portIterator = portsHash.keySet().iterator();
		 while(portIterator.hasNext())
		 {
			 Integer key = portIterator.next();
			 Port myPort = portsHash.get(key);
			 System.out.println(myPort);
		 }
		 
		 mySql.getProduction(portsHash); 
		 mySql.getSales(portsHash);
		
		
		 portIterator = portsHash.keySet().iterator();
		 while(portIterator.hasNext())
		 {
			 Integer key = portIterator.next();
			 Port myPort = portsHash.get(key);
			 System.out.println(myPort);
			 for(int i=0; i<myPort.productionArray.size(); i++)
			 {
				 //System.out.println(myPort.productionArray.get(i));
			 }
		 }
		
		 
		 System.out.println("Number of Ports: "+ portsHash.size());
		 //boolean x=portsHash.isEmpty();
		 //boolean portsHash.containsKey(24);
		 //boolean portsHash.containsValue("Islet");
		 //Port portsHash.get(24); retrieves the port with ID 24.
		 
		
		EventQueue.invokeLater(new Runnable() { //this makes the window.
		//EventQueue.invokeAndWait(new Runnable() { //this makes the window.
			public void run() {
				try {
					frame = new Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		while (GUILoaded==false) // wait for gui elements to load.
		{
			try {
				Thread.sleep(2*100); //this is to pause and let the gui load before it breaks shit.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("GUI Loaded: main");
		
		
		Handler = new LogFileHandler();
		Handler.start();

		// Get latest log
		System.out.println(Handler.GetLatestLogFile());	
		
	}	
	
	public static void reloadLog(){
			
		Handler.interrupt();
		frame.setLogStatus(-1); //loading new log file, and wait.
		System.out.println(Handler.getState());
		Handler = new LogFileHandler();
		Handler.start();
	}
	
	public static void reloadLog(int cherno){
		
		if(cherno==1)
		{
			LogDirectory = "D:\\Steam Games\\steamapps\\common\\Naval Action\\logs";
			System.out.println("Cherno Mode");
			Handler.interrupt();
			frame.setLogStatus(-1); //loading new log file, and wait.
			System.out.println(Handler.getState());
			Handler = new LogFileHandler();
			Handler.start();
		}
	}
	
	public static void setPlayerName(String name)
	{
		//System.out.println("setting player name: "+name);
		//while(!frame.lblPlayerName.isEnabled())
		frame.lblPlayerName.setText(name);
		frame.lblPlayerName.setForeground(Color.green);
		playerName=name;
		if(!hi)
		{
			mySql.hi(playerName);
		}
		hi=true;
	}

}