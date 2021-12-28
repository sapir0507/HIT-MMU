package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.EventListener;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.hit.services.CacheUnitController;
import com.hit.util.DataStat;


public class Server implements Runnable, PropertyChangeListener, EventListener 
{
	private ServerSocket serverSocket;
	private int port;
	private int choice;
	private boolean serverIsRunning;
	Executor threadPool;
	@SuppressWarnings("rawtypes")
	CacheUnitController unitController;
	DataStat dataStats;
   
	@SuppressWarnings("rawtypes")
    
	public Server(int port)
    { 
	   setPort(port);
	   setChoice(3);
	   try 
	   {
		   serverSocket = new ServerSocket(getPort());
	   } 
	   catch (IOException e) 
	   {
	    	e.printStackTrace();
	   }
	   unitController = new CacheUnitController();
	   unitController.setChoice(choice);
	   unitController.setCapacity(3);
       threadPool = Executors.newFixedThreadPool (100);
       dataStats = DataStat.getInstance();
       dataStats.setCapacity(unitController.getCapacity());
       dataStats.setCacheAlgo(unitController.getChoice());
   }
	
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		String prop = evt.getPropertyName();
		
		if (prop == "stateChange")
		{
			if (evt.getNewValue() == "start")
			{
				new Thread(this).start();
			}
			else if(evt.getNewValue() == "cache_unit_config") 
			{
				Scanner scanner = new Scanner(System.in);
				System.out.println("\nYour Choice: ");
				String algo = scanner.next();
				System.out.println("please enter the new cache unit capacity: ");
				int capacity = scanner.nextInt();
				unitController.setCapacity(capacity);
				switch(algo.toLowerCase()) 
				{
				case "lru":
					unitController.setChoice(1);
					dataStats.setCapacity(unitController.getCapacity());
				    dataStats.setCacheAlgo(unitController.getChoice());
					break;
				case "secondchance":
					unitController.setChoice(2);
					dataStats.setCapacity(unitController.getCapacity());
				     dataStats.setCacheAlgo(unitController.getChoice());
					break;
				case "randomalgorithmcache":
					unitController.setChoice(3);
					dataStats.setCapacity(unitController.getCapacity());
				    dataStats.setCacheAlgo(unitController.getChoice());
					break;
				default:
					System.out.println("Unknown cache algorithm detected.");
				}
				scanner.close();
				System.out.println("the new algorithem is " +algo+ " Capacity is " +capacity);
			}
			else 
			{
				if(serverIsRunning) 
	        	{
					System.out.println ("Shutdown Server...\n");
	                serverIsRunning = false;
	        	}
	        	else 
	        	{
	        		System.out.println("Server not running\n");
	        	}
			
			}
    	}
	}

	@Override
	public void run()
	{
		
		serverIsRunning = true;
		Socket client = null;
		System.out.println("\nWaiting for client......");
	    while(serverIsRunning)
	    {
	         try
	         {
	        	 client = serverSocket.accept();
	        	 dataStats.addRequest();
	        	  //creating a handle request
		         @SuppressWarnings("unchecked")
		         HandleRequest<String> re = new HandleRequest<String>(client, unitController);
		         //handle request is executed on a new thread
		         threadPool.execute(re);
		         System.out.println("Just connected to " + client.getRemoteSocketAddress());
		     } 
	        /* catch (SocketTimeoutException s)
	         {
	            System.out.println("\nSocket timed out!");
	           
	         } */
	         catch (IOException e) 
	         {
	            e.printStackTrace();
	            
	         }
	    }
	    try
        {
	    	if(!serverSocket.isClosed())
            	    serverSocket.close();
	    	//חדשים threads מונע פתיחת 
        	((ExecutorService)threadPool).shutdownNow();
        }
	    catch (IOException e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	}
	
	public int getPort() 
	{
		return port;
	}

	public void setPort(int port) 
	{
		this.port = port;
	}

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}
}