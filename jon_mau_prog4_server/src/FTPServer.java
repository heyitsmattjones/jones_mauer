
import java.io.*;
import java.net.*;

/**

@author Pauer
*/
public class FTPServer
{
   private int nextPort;
   private final int LISTEN_PORT = 5721;
   private final int START_PORT = 5700;
   private final int MAX_PORT = 5800;
   
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
   
   public FTPServer()
   {
      nextPort = START_PORT;
   }
   
   public void run()
   {
      System.out.println("FTP Server running...");
      try
      {
         ServerSocket servSock = new ServerSocket(LISTEN_PORT);
         Socket sockThread = null;
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
      catch(IOException e)
      {
         e.toString();
      }
   }
   
   private int calcPort() //assuming data port overlap won't occur
   {
      if (nextPort == LISTEN_PORT)
         nextPort++;
      else if (nextPort > MAX_PORT)
         nextPort = START_PORT;
      return nextPort++;
   }
}
