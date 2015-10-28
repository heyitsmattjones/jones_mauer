
import java.net.*;
import java.io.*;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**

@author Pauer
*/
public class FTPThread extends Thread
{
   private Socket sock = null;
   private Socket dataSock = null;
   private final int DATA_PORT = 5720;
   
   public FTPThread(Socket inSock)
   {
      try
      {
         sock = inSock;
         if(sock == null)
            throw new IOException("null Socket");
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   public void run()
   {
      
   }
   
   private void listFiles()
   {
      File dir = new File("Files");
   }
}
