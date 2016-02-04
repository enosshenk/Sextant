package com.shenko.sextant;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class sextant {

	public static LogFileHandler Handler;
	public static SQLHandler mySql;
	public static Window frame;
	public static String playerName;
	public static int logStatus; 
	public static boolean GUILoaded;
		//this will be 0 until we hit EOF and then receive a new message. This indicate we've got an active log file.
		//then it'll be 1 when reading, and 2 when EOF.
	public static int CurrentPort;
		
	
	public static void main(String [] args) {
		System.out.println("Started...");
		
		GUILoaded=false;
		CurrentPort=0;
		logStatus=0;
				
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
		
		mySql = new SQLHandler();
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
	
	public static void setPlayerName(String name)
	{
		//System.out.println("setting player name: "+name);
		//while(!frame.lblPlayerName.isEnabled())
		frame.lblPlayerName.setText(name);
		frame.lblPlayerName.setForeground(Color.green);
		playerName=name;
	}

}