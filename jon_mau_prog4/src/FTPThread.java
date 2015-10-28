
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
   private final int CHUNK_SIZE = 1024;
   private FileInputStream inRequests; //for requests from control connect
   private BufferedReader readBuff;
   private DataOutputStream writeSock;
   
   public FTPThread(Socket inSock)
   {
      try
      {
         sock = inSock;
         if(sock == null)
            throw new IOException("null Socket");
         readBuff = new BufferedReader(
               new InputStreamReader(sock.getInputStream()));
         writeSock = new DataOutputStream(sock.getOutputStream());
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   public void run()
   {
      listFiles();
   }
   
   private String listFiles()
   {
      String toSend = "";
      try
      {
         File dir = new File("Files");     //creates File & sets file pathname
         File[] files = dir.listFiles();   //get the list of all files
         for(int i = files.length; i > 0; i--)
         {
            if(files[i].isFile())
               toSend += files[i].getName();
         }
      }
      catch(Exception e)
      {
         e.toString();
      }
      return toSend;
   }
   
   private void sendFile(String fileName)
   {
      
   }
   
   private void openDataSock()
   {
      try
      {
         dataSock = new Socket(sock.getInetAddress(), DATA_PORT);
      }
      catch(IOException e)
      {
         e.toString();
      }
   }
   
   private void closeConnections()
   {
      try
      {
         dataSock.close();
      }
      catch(IOException e)
      {
         e.toString();
      }
   }
   
   private void sendThroughControl(String toSend)
   {
      try
      {
         writeSock.writeBytes(toSend);
         byte[] buffer = new byte[CHUNK_SIZE];
         int numBytes;
         numBytes = inRequests.read(buffer);
         while(numBytes != -1)
         {
            writeSock.write(buffer, 0, numBytes);
            numBytes = inRequests.read(buffer);
         }
      }
      catch(IOException e)
      {
         e.toString();
      }
   }
}
