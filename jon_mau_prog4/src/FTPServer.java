
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
      try
      {
         ServerSocket servSock = new ServerSocket(LISTEN_PORT);
      }
      catch(IOException e)
      {
         
      }
   }
}
