package com.shenko.sextant;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sextant {

	public static LogFileHandler Handler;
	public static SQLHandler mySql;
	
	public static void main(String [] args) {
		System.out.println("Started...");
		mySql = new SQLHandler();
		Handler = new LogFileHandler();
		Handler.start();

		// Get latest log
		System.out.println(Handler.GetLatestLogFile());	
		
	}	

}
