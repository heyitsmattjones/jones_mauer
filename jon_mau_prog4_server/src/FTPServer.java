
import java.io.*;
import java.net.*;

/**
FTPServer runs a File Transfer Protocol server to which clients 
can connect to. The clients are assigned their own server thread with a 
unique port number for data transfer (Assuming no more than 100 active
clients.)
@author Matthew P Jones & Paul Mauer
*/
public class FTPServer
{
   private int nextPort;      //next data port to use
   private final int LISTEN_PORT = 5721;
   private final int START_PORT = 5700;
   private final int MAX_PORT = 5800;
   
   /**
   main creates a new instance of FTPServer and calls the run method on it.
   @param args no arguments
   */
   public static void main(String[] args)
   {
      try
      {
         FTPServer fileServ = new FTPServer();
         fileServ.run();
      }
      catch(Exception e)
      {
         e.toString();
      }
   }
   
   /**
   Constructor for FTPServer. Sets the initial data port number.
   */
   public FTPServer()
   {
      nextPort = START_PORT;
   }
   
   /**
   The run method accepts connections from incoming client server and creates
   a separate FTPThread for them.
   */
   public void run()
   {
      System.out.println("FTP Server running...");
      try
      {
         ServerSocket servSock = new ServerSocket(LISTEN_PORT);
         Socket sockThread = null;  //for clarity
         while(true)
         {
            sockThread = servSock.accept();
            System.out.println("Got a connectiono: " 
                  + servSock.getInetAddress() + " on port " 
                  + LISTEN_PORT + " remote port # "+ servSock.getLocalPort());
            FTPThread thread = new FTPThread(sockThread, calcPort());
            thread.start();
         }
      }
      catch(IOException e)    //should not be hit
      {
         System.out.println(e.toString());
      }
   }
   
   /**
   calcPort gets the next data port to use for a specific client.
   @return 
   */
   private int calcPort() //assuming data port overlap won't occur
   {
      if (nextPort == LISTEN_PORT)
         nextPort++;
      else if (nextPort > MAX_PORT)
         nextPort = START_PORT;
      return nextPort++;
   }
}
