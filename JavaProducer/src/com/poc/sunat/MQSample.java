package com.poc.sunat;


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
public class MQSample {


  // define the name of the QueueManager
   //private static final String qManager = "GTWQM";
   private static final String qManager = "QM1";
   
   
  // and define the name of the Queue
  //private static final String qName = "INVENTQ";
  //private static final String qName = "DEV.QUEUE.1";
  private static final String qName = "SUNAT.IN";
  
  private static final String HOSTNAME="qm1-ibm-mq-qm-mq.mqcluster2-028793c348bcbafefe835476dfb2d2fa-0000.eu-de.containers.appdomain.cloud";
  //private static final String HOSTNAME="393cf95d-us-east.lb.appdomain.cloud";
  //private static final String HOSTNAME="localhost";
  
  //private static final String PORT="1416";
  //private static final String PORT="1414";
  private static final String PORT="443";
  
  //private static final String CHANNEL="GTW.SERCONN";
  private static final String CHANNEL="QM1.SVRCONN";
  
  private static final String USER="fnaranjo";
  private static final String PASSWORD="020kw31x3";
  
  

  /**
   * Main entry point
   * 
   * @param args - command line arguments (ignored)
 * @throws InterruptedException 
   */
  public static void main(String args[]) throws InterruptedException {
    try {
    	
       Hashtable<String, Object> properties = new Hashtable<String, Object>();	
       properties.put("hostname", HOSTNAME);
       properties.put("port", new Integer(Integer.parseInt(PORT)));
       properties.put("channel", CHANNEL);
       //properties.put(MQConstants.USER_ID_PROPERTY, USER);
      // properties.put(MQConstants.USE_MQCSP_AUTHENTICATION_PROPERTY, true);
      // properties.put(MQConstants.PASSWORD_PROPERTY, PASSWORD);
       
       
    	
      // Create a connection to the QueueManager
      System.out.println("Connecting to queue manager: " + qManager);
    
      

      MQQueueManager qMgr = new MQQueueManager(qManager,properties);
      

      // Define a simple IBM MQ Message ...
      MQMessage msg = new MQMessage();

      int numMessages = ((System.getenv("NUM_MENSAJES"))!=null ? Integer.parseInt(System.getenv("NUM_MENSAJES")):10);
		
		System.out.println("Num Messages="+numMessages);
		
		long uniqueNumber=0;
		
		do
		{
      
		      // Specify the default put message options
		      MQPutMessageOptions pmo = new MQPutMessageOptions();
		      
		      uniqueNumber = System.currentTimeMillis() % 1000;
		      msg.writeUTF("Your lucky number today is " + uniqueNumber);
		      
		      qMgr.put(qName,msg,pmo);
		  	  System.out.println("Sent message:"+numMessages);
		      numMessages=numMessages-1;
			   Thread.sleep(1000);
			   
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
      qMgr.disconnect();
      System.out.println("Done!");
    }
    catch (MQException ex) {
      System.out.println("An IBM MQ Error occurred : Completion Code " + ex.completionCode
          + " Reason Code " + ex.reasonCode);
      ex.printStackTrace();
      for (Throwable t = ex.getCause(); t != null; t = t.getCause()) {
        System.out.println("... Caused by ");
        t.printStackTrace();
      }

    }
    catch (java.io.IOException ex) {
      System.out.println("An IOException occurred whilst writing to the message buffer: " + ex);
    }
    return;
  }
}
