package com.shenko.sextant;

import java.io.*;


public class LogFileHandler {
	
	// Editable Variables
	static String LogDirectory = "C:\\Program Files (x86)\\Steam\\SteamApps\\common\\Naval Action\\logs";
	static String LogFilePrefix = "custom_";
	
	// Internal stuff
	public LogParser Parser;
	static File CurrentLog;
	BufferedReader Reader;
	
	public LogFileHandler()
	{
		Parser = new LogParser();
		
		try {
			CurrentLog = new File(GetLatestLogFile());
			FileReader FileReader = new FileReader(CurrentLog);
			Reader = new BufferedReader(FileReader);
			
			LogHandlerLoop();
			
		} catch (FileNotFoundException e) {
			System.out.println("Selected file not found! " + CurrentLog);
		}
	}
	
	public void LogHandlerLoop()
	{
		String Line = null;
		
		while (true)
		{
			// Load them sexy new lines
			try {
				Line = Reader.readLine();
				if (Line == null)
				{
					Thread.sleep(1*1000);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("BARF");
			}
			
			ParseLine(Line);
			
		}
	}
	
	public void ParseLine(String Line)
	{
		if (Line.contains("EntrancePosition"))
		{
			// Line is a port data block
		//	System.out.println(Line);
			Parser.ParsePortData(Line);
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
			System.out.println(L.getName());
			if (L.lastModified() > LastModified)
			{
				SelectedLog = L;
				LastModified = L.lastModified();
			}
		}
		
		return SelectedLog.getAbsolutePath();
	}
	
}
