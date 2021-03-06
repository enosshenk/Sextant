package com.shenko.sextant;

import java.io.*;

import javax.swing.JOptionPane;


public class LogFileHandler extends Thread {

	// Editable Variables
	//static String LogDirectory = sextant.LogDirectory; //fucking cherno's SSD
	static String LogFilePrefix = "custom_";

	// Internal stuff
	public LogParser Parser;
	static File CurrentLog;
	BufferedReader Reader;
	public static String LogDirectory;

	//public LogFileHandler()
	public void run()
	{
		Parser = new LogParser();
		//TODO this is where we check for config file, if DNE prompt for director, else open config file and read for steam directory

		LogDirectory=getConfig(); 


		try {
			sextant.println("Trying to open log file in "+LogDirectory);
			CurrentLog = new File(GetLatestLogFile());
			sextant.println("Opening "+CurrentLog.getName());
			FileReader FileReader = new FileReader(CurrentLog);
			Reader = new BufferedReader(FileReader);

			LogHandlerLoop();

		} catch (FileNotFoundException e) {
			sextant.println("Selected file not found! " + CurrentLog);
			JOptionPane.showMessageDialog(null, "Error: important files not found. \nTry deleting your sextantconfig file\nand doing a better job entering the directory.");
		}
		sextant.println("latest log file: "+LogFileHandler.GetLatestLogFile());	
	}


	public void reload() {} //we do this in sextant, since we need to interrupt this thread and start a new one. 

	public void LogHandlerLoop()
	{
		boolean exit=false;
		String Line = null;
		int logStage=0; //0 is reading initially loaded file. at 1, we've hit EOF. at 2, we've read new info, confirming that we have a live file.

		while(!exit)
		{
			try {
				Line = Reader.readLine();
				//sextant.println("loghandlerloop "+ Line);
				if (Line == null)
				{
					if(logStage==0)
					{
						logStage=1; //ok, we've hit EOF. Let's see if we'll get more info.
						sextant.frame.setLogStatus(0); //red
					}
					if(logStage==2)
					{
						sextant.frame.setLogStatus(2); //green
					}
					Thread.sleep(1*1000);
				}
				else //not null, read the lines. update the log stage
				{
					if(logStage>0)
					{
						logStage=2;
						sextant.frame.setLogStatus(1); //yellow
					}
					ParseLine(Line);
				}

			} catch (IOException e) {
				e.printStackTrace();
				exit=true;
			} catch (InterruptedException e) {
				sextant.println("InterruptedException.");
				try {
					Reader.close();
					sextant.println("File closed.");
					exit=true;
				} catch (IOException e1) {
					e1.printStackTrace();
					exit=true;
				}
				sextant.println("File closed.");
			}
		}

	}

	public void ParseLine(String Line)
	{
		if (Line.contains("EntrancePosition"))
		{
			// Line is a port data block
			//	sextant.println(Line);
			Parser.ParsePortData(Line);
		}

		if (Line.contains("CurrentShipItemId"))
		{
			// Line is a player data block
			Parser.ParsePlayerData(Line);
		}

		if (Line.contains("GetLaborHours")) 
		{
			//5980:[2016-Feb-10 09:14:54.831209] Log: Called PlayerService.GetLaborHours(/PlayerService/GetLaborHours/) with: 
			//{}
			//Received:
			//	1025.40663736647

			String timestamp = Line.substring(Line.indexOf("[")+1, Line.indexOf("."));

			for(int i=0;i<3;i++) //read the next 3 lines
			{
				try {
					Line = Reader.readLine();
					if(Line==null)
					{
						i--;
						Thread.sleep(1*1000);
					}

				} catch (IOException | InterruptedException e) {
					// 
					e.printStackTrace();
				}
			}
			String toPass = Line;
			Parser.ParseLaborData(toPass, timestamp);

		}

		if (Line.contains("SellPrice") && Line.contains("BuyPrice")) // only run this if we're realtime. Old-ass items aren't useful here since the time isn't reliable.
		{
			if(sextant.logStatus>0 && sextant.CurrentPort>0)
			{// Line is a port shop data block
				Parser.ParseSaleData(Line);
			}
			else
			{
				sextant.println("Log or port status insufficient for buy/sell: "+sextant.logStatus+" "+sextant.CurrentPort);
			}

		}

		if (Line.contains("Key") && Line.contains("Value"))
		{
			// Line is consumption/production block for a port
			Parser.ParsePortProductionData(Line);
		}

		if (Line.contains("Services.Items."))
		{
			// Line is an item in the warehouse
			Parser.ParseItemLine(Line);
		}

		if (Line.contains("GetPersonalEvents")) 
		{
			/*			//11204:[2016-Feb-22 20:57:48.644537] Log: Called PlayerService.GetPersonalEvents(/PlayerService/GetPersonalEvents/) with: 
			{}
			Received:
				[{"TargetPosition":{"x":786400,"y":-106400},"Rank":5,"XpReward":135,"GoldReward":5000},{"TargetPosition":{"x":757600,"y":-54400},"Rank":4,"XpReward":118,"GoldReward":5000}]
			 */			

			for(int i=0;i<3;i++) //read the next 3 lines
			{
				try {
					Line = Reader.readLine();
					if(Line==null)
					{
						i--;
						Thread.sleep(1*1000);
					}

				} catch (IOException | InterruptedException e) {
					// 
					e.printStackTrace();
				}
			}
			String toPass = Line;
			Parser.ParseMissionData(toPass, true);

		}

		if (Line.contains("GetEvents")) 
		{
			/*			8624:[2016-Feb-29 21:34:18.132627] Log: Called EventsService.GetEvents(/EventsService/GetEvents/) with: 
				-227947;247007
			Received:
				[{"Rank":1,"CombatType":0,"UseCount":0,"EventType":"CombatEvent","Id":692,"Position":{"x":-220800,"y":236800},"ActivationRadius":2048},{"Rank":2,"CombatType":0,"UseCount":0,"EventType":"CombatEvent","Id":700,"Position":{"x":-232800,"y":236800},"ActivationRadius":2048},{"Rank":0,"CombatType":0,"UseCount":0,"EventType":"CombatEvent","Id":717,"Position":{"x":-244800,"y":256800},"ActivationRadius":2048}]
			 */
			try {
				Line = Reader.readLine();
				while(Line==null)
				{
					Thread.sleep(1*1000);
					Line = Reader.readLine();
				}

			} catch (IOException | InterruptedException e) {
				// 
				e.printStackTrace();
			}

			//get new X, Y
			String x, y;
			sextant.println("event line:"+Line);
			x=Line.substring(1, Line.indexOf(";"));
			y=Line.substring(Line.indexOf(";")+1);
			sextant.println("event x y:"+Integer.parseInt(x)+","+Integer.parseInt(y));
			try {
				sextant.frame.setLoc(-Integer.parseInt(x), -Integer.parseInt(y));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
			}

			for(int i=0;i<2;i++) //read the next 3 lines
			{
				try {
					Line = Reader.readLine();
					if(Line==null)
					{
						i--;
						Thread.sleep(1*1000);
					}

				} catch (IOException | InterruptedException e) {
					// 
					e.printStackTrace();
				}
			}
			String toPass = Line;
			//Parser.ParseMissionData(toPass, false);
			//TODO make this put event markers on the map.

		}

		if (Line.contains("clanTag"))
		{
			// clanTag: "I"
			Parser.ParseClanTag(Line);
		}
		

		if (Line.contains("SteamName "))
		{
			//8824:[2016-Feb-29 22:12:58.639353] Log: [Default] [ClientApplicationStateManager]: OnLogon: SteamID = 76561197980847483 SteamName = absolain UserId = 6bc71687-7c00-4307-a1e5-2d9bdfb30a66.3751
			Parser.ParseSteam(Line);
		}
		
		if (Line.contains("GetPlayerInfo"))
		{
			try {
				Line = Reader.readLine();
				while(Line==null)
				{
					Thread.sleep(1*1000);
					Line = Reader.readLine();
				}
				Parser.ParseName(Line);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}


	public static String GetLatestLogFile()
	{
		// Set up a filter to look for only the prefixed log files
		FilenameFilter Filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				if (name.startsWith(LogFilePrefix))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		};

		// Get all files matching filter
		File LogDir = new File(LogDirectory);
		File[] Logs = LogDir.listFiles(Filter);

		long LastModified = Long.MIN_VALUE;
		File SelectedLog = null;
		for (File L : Logs)
		{
			//sextant.println(L.getName());
			if (L.lastModified() > LastModified)
			{
				SelectedLog = L;
				LastModified = L.lastModified();
			}
		}

		return SelectedLog.getAbsolutePath();
	}

	private String getConfig()
	{
		String LogDirectory = new String();
		try {
			File configFile = new File("sextantconfig.ini");
			if(configFile.exists())
			{	
				FileReader FileReader = new FileReader("sextantconfig.ini");
				Reader = new BufferedReader(FileReader);
				LogDirectory = Reader.readLine();
				System.out.println("read config: "+LogDirectory);

			}
			else
			{
				FileWriter f = new FileWriter("sextantconfig.ini");
				LogDirectory = 
						(String)JOptionPane.showInputDialog(
								null, //frame, let's see if this works			             
								"Please enter your Naval Action directory. The below is the default",
								"There, happy?",
								JOptionPane.PLAIN_MESSAGE,
								null,
								null,
								"C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Naval Action");
				LogDirectory = LogDirectory + "\\logs";
				System.out.println("log directory:"+LogDirectory);
				f.write(LogDirectory);
				f.close();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			sextant.println("Error getting config file");
			e.printStackTrace();
		}
		return LogDirectory;

	}

}
