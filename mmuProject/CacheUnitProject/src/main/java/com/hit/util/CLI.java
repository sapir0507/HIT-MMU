package com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;





//our observable

public class CLI implements Runnable {

	private PropertyChangeSupport support;
	private Scanner scanner;
	private PrintWriter printWriter;
	
	private static final String STARTING = "\nstarting server.......";
	private static final String SHUTDOWN = "\nshutdown server";
	private static final String INVALID = "\nnot a valid command";
	private static final String ENTER_COMMAND = "\nPlease Enter Your Command: ";
	
	private String serverStatus = "on";
	private int port;
	
	//constructor
	public CLI(InputStream in, OutputStream out)
	{
		this.scanner = new Scanner(in);
		this.printWriter = new PrintWriter(out);
		this.support = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) 
	{
		support.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl)
	{
		support.removePropertyChangeListener(pcl);
	}

	public void write(String string) 
	{
		this.printWriter.write(string);
		printWriter.flush();
	}

	@Override
	public void run() 
	{
		String input, algo = "Second Chance Algorithm\n";
		int capacity = 3;
		write("Default parameters\nAlgorithm: " + algo + " Capacity: " + capacity);
			
			
			do {
				write(ENTER_COMMAND);
				
				input = scanner.next();
				
				switch(input.toLowerCase()) 
				{
				case "start":
					write(STARTING);
					support.firePropertyChange("stateChange", serverStatus, "start");
					serverStatus = "start";
					break;
				case "shutdown":
					write(SHUTDOWN);
					support.firePropertyChange("stateChange", serverStatus, "shutdown");
					serverStatus = "shutdown";
					break;
				case "cache_unit_config":
					write("please enter the new cache unit method: LRU, SecondChance or RandomAlgorithmCache");
					support.firePropertyChange("stateChange", serverStatus, "cache_unit_config");
					break;
				default:
					write(INVALID);
					break;
				}
		} 
			while (!input.equals("shutdown"));
			      scanner.close();
	  }

	public int getPort()
	{
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	}
//}
