package com.shenko.sextant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

public class ShotLogger extends Thread{
	// Internal stuff
	public LogParser Parser;
	static File CurrentLog;
	BufferedReader Reader;
	private boolean EOF; //we don't want to activate the tab on startup, so let's read the file until we hit EOF, then all us to activate the tab
	// because it'll be new shots taking place.

	//public LogFileHandler()
	public void run()
	{
		Parser = new LogParser();

		EOF = false;

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

		while(!exit)
		{
			try {
				Line = Reader.readLine();
				//sextant.println("loghandlerloop "+ Line);
				if (Line == null)
				{
					Thread.sleep(1*200);
					EOF = true; //we've read the full file on startup, so now let's activate the shot logger tab if we see new shit
				}
				else //not null, read the lines. update the log stage
				{
					if(EOF)
					{
						sextant.frame.activateShots();
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
		/*if (Line.contains("EntrancePosition"))
			{
				// Line is a port data block
			//	sextant.println(Line);
				Parser.ParsePortData(Line);
			}
		 */

		sextant.frame.shotText.append(Line+"\n");
	}




	public static String GetLatestLogFile()
	{
		// Set up a filter to look for only the prefixed log files
		FilenameFilter Filter = new FilenameFilter()
		{
			public boolean accept(File dir, String name)
			{
				if (name.startsWith("bullet"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		};

		// Get all files matching filter //TODO this shit can be cleaned up.
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




