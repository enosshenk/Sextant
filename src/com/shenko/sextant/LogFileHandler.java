package com.shenko.sextant;

import java.io.*;


public class LogFileHandler extends Thread {
	
	// Editable Variables
	//static String LogDirectory = sextant.LogDirectory; //fucking cherno's SSD
	static String LogFilePrefix = "custom_";
	
	// Internal stuff
	public LogParser Parser;
	static File CurrentLog;
	BufferedReader Reader;
	
	//public LogFileHandler()
	public void run()
	{
		Parser = new LogParser();
		
		try {
			sextant.println("Trying to open log file in "+sextant.LogDirectory);
			CurrentLog = new File(GetLatestLogFile());
			sextant.println("Opening "+CurrentLog.getName());
			FileReader FileReader = new FileReader(CurrentLog);
			Reader = new BufferedReader(FileReader);
			
			LogHandlerLoop();
			
		} catch (FileNotFoundException e) {
			sextant.println("Selected file not found! " + CurrentLog);
		}
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
			Parser.ParseMissionData(toPass);

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
		File LogDir = new File(sextant.LogDirectory);
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
	
}
