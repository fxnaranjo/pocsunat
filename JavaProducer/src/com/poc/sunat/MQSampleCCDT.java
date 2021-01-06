package com.poc.sunat;


import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Hashtable;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

/**
 * Simple example program
 */
public class MQSampleCCDT {


  // define the name of the QueueManager
   private static final String qManager = "QM1";
  // private static final String qManager = "GTWQM";
   
   
  // and define the name of the Queue
 // private static final String qName = "INVENTQ";
  private static final String qName = "SUNAT.IN";
  
  public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
  public static final String ANSI_RESET = "\u001B[0m";
  
  public static boolean isNumeric(String strNum,int option) {
	  	int min=0;
	  	int max=0;
	   if (option==1)
	  	{
	  		min=0;
	  		max=4;
	  	}else
	  	{
	  		min=0;
	  		max=50;
	  	}
	  
	    if (strNum == null) {
	    	 System.out.println("INVALID OPTION");
	        return false;
	    }
	    try {
	        double d = Double.parseDouble(strNum);
	        if (d<=min || d>max)
	        	throw new Exception("INVALID OPTION");
	    } catch (Exception nfe) {
	    	 System.out.println("INVALID OPTION");
	    	 
	        return false;
	    }
	    return true;
	}
  
  public static Integer readNumber(int opcion) throws IOException {
	  
	  String mensaje = (opcion==1) ? ANSI_RED_BACKGROUND + "ENTER YOUR OPTION:"+ANSI_RESET:ANSI_RED_BACKGROUND +"NUMBER OF MESSAGES TO SEND:"+ANSI_RESET;
	  
	  BufferedReader obj = new BufferedReader(new InputStreamReader(System.in));   
      String str; 
      
      do
      {
      	System.out.println(mensaje);
      	str = obj.readLine(); 
      
      
      }while(!isNumeric(str,opcion));
	  	
	  return Integer.parseInt(str);  
      
	}
  
 
  /**
   * Main entry point
   * 
   * @param args - command line arguments (ignored)
 * @throws InterruptedException 
   */
  public static void main(String args[]) throws InterruptedException {
    try {
    	
    	do 
    	{
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        System.out.println("*******WELCOME TO MQ CLIENT DEMO PROGRAM*******************");
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        System.out.println("***********************************************************");
        System.out.println("OPTIONS:");
        System.out.println("1.- USE QMANAGER IN US");
        System.out.println("2.- USE QMANAGER IN GERMANY");
        System.out.println("3.- USE BOTH QMANAGERS");
        System.out.println("4.- EXIT");
        
        int numMessages=0;
        
        String str=String.valueOf((readNumber(1)));
        
        java.net.URL chanTab1=null;
        
        switch (str) {
        case "1":
			System.out.println("CONNECTING TO QMANAGER IN US");
			chanTab1 = new URL("file:///mq/client/ccdt2.json");
			numMessages=readNumber(2);
			break;
        case "2":
			System.out.println("CONNECTING TO QMANAGER IN GERMANY");
			chanTab1 = new URL("file:///mq/client/ccdt3.json");
			numMessages=readNumber(2);
			break;
        case "3":
			System.out.println("CONNECTING TO BOTH QMANAGERS");
			chanTab1 = new URL("file:///mq/client/ccdt4.json");
			numMessages=readNumber(2);
			break;
		case "4":
			System.out.println("CLOSE PROGRAM");
			System.exit(0);
		}
        
        
        
    	
       //java.net.URL chanTab1 = new URL("file:///mq/client/AMQCLCHL.TAB");
       
        //java.net.URL chanTab1 = new URL("file:///mq/client/ccdt2.json");
       //java.net.URL chanTab1 = new URL("file:///mq/client/ccdt3.json");
    	//java.net.URL chanTab1 = new URL("file:///mq/client/ccdt4.json");
       //java.net.URL chanTab1 = new URL("file:///mq/client/ccdt-ssl.json");
       
      // java.net.URL chanTab1 = new URL("file:///home/fnaranjo/PROYECTOS/ALL/SUNAT/MQ-Opp/POC/CCDT.json");
    	
      // Create a connection to the QueueManager
      System.out.println("Connecting to queue manager: " + qManager);
    
      

      MQQueueManager qMgr = null;
      

      // Define a simple IBM MQ Message ...
      MQMessage msg = new MQMessage();

     // int numMessages = ((System.getenv("NUM_MENSAJES"))!=null ? Integer.parseInt(System.getenv("NUM_MENSAJES")):10);
		
		
		System.out.println("******************************************");
		
		long uniqueNumber=0;
		
		do
		{
			qMgr = new MQQueueManager(qManager,chanTab1);
			
			System.out.println(qMgr.getDescription());
			
			
      
		      // Specify the default put message options
		      MQPutMessageOptions pmo = new MQPutMessageOptions();
		      
		      uniqueNumber = System.currentTimeMillis() % 1000;
		      msg.writeUTF("Your lucky number today is " + uniqueNumber);
		      
		      qMgr.put(qName,msg,pmo);
		  	  System.out.println("Sent message:"+numMessages);
		  	System.out.println("******************************************");
		      numMessages=numMessages-1;
		      qMgr.disconnect();
			   //Thread.sleep(250);
			   
		}while(numMessages!=0);

      // Now get the message back again. First define an IBM MQ
      // message
      // to receive the data
     // MQMessage rcvMessage = new MQMessage();

      // Specify default get message options
     // MQGetMessageOptions gmo = new MQGetMessageOptions();

      // Get the message off the queue.
     // System.out.println("...and getting the message back again");
     // queue.get(rcvMessage, gmo);

      // And display the message text...
      //String msgText = rcvMessage.readUTF();
      //System.out.println("The message is: " + msgText);


      // Disconnect from the QueueManager
      System.out.println("Disconnecting from the Queue Manager");
     // qMgr.disconnect();
      System.out.println("Done!");
    	}while(true);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
        System.out.println("... Caused by ");
        t.printStackTrace();
      }

    }
    return;
  }
}
