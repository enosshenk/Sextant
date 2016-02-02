package com.shenko.sextant;

import java.io.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sextant {

	public static LogFileHandler Handler;
	
	public static void main(String [] args) {
		
		Handler = new LogFileHandler();
		
		// Get latest log
		System.out.println(Handler.GetLatestLogFile());	
		
	}	

}
