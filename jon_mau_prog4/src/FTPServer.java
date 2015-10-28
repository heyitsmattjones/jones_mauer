
import java.io.*;
import java.net.*;

/**

@author Pauer
*/
public class FTPServer
{
   private final int LISTEN_PORT = 5721;
   
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
         }
      }
      catch(IOException e)
      {
         e.toString();
      }
   }
}
