
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
   private Socket sock = null;  //contorl connection
   private Socket dataSock = null;  //data transport connection
   private final int DATA_PORT = 5720;
   private final int CHUNK_SIZE = 1024;
   private FileInputStream inCtrlReq; //for requests from control connect
   private FileInputStream inStreamFile;  //used for sending a local file
   private BufferedReader readCtrlBuff;      //Control reader
   private DataOutputStream writeCtrlSock;   //Control output stream
   private DataOutputStream writeDataSock;   //Data output stream
   private DataInputStream readDataSock;     //Data input stream
   
   public FTPThread(Socket inSock)
   {
      try
      {
         sock = inSock;  //control connection socket
         if(sock == null)
            throw new IOException("null Socket");
         readCtrlBuff = new BufferedReader(
               new InputStreamReader(sock.getInputStream()));
         writeCtrlSock = new DataOutputStream(sock.getOutputStream());
         writeDataSock = null;   //initiallize only when needed
         inStreamFile = null;    //initiallized when sending a file
         readDataSock = null;    //initiallized when data is being received
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   @Override
   public void run()
   {
      listFiles();
   }
   
   private String listFiles()  //completed, not verified
   {
      String toSend = "";
      try
      {
         File dir = new File("Files");     //creates File & sets file pathname, I think this specifies which folder to look in.
         File[] files = dir.listFiles();   //get the list of all files in dir
         for(int i = files.length; i > 0; i--)
         {
            if(files[i].isFile())
               toSend += files[i].getName() + " ";
         }
      }
      catch(Exception e)
      {
         e.toString();
      }
      return toSend;
   }
   
   private void sendFile(String fileName) //completed, not tested, make sure writeDataSock is initialized
   {
      try
      {
         if (writeDataSock == null)
            throw new Exception("DataOutput not initialized");
         inStreamFile = new FileInputStream(fileName);
         byte[] buffer = new byte[CHUNK_SIZE];
         int numBytes = inStreamFile.read(buffer); //number of bytes read
         while(numBytes != -1)
         {
            writeDataSock.write(buffer, 0, numBytes);
            numBytes = inStreamFile.read(buffer);
         }
      }
      catch(FileNotFoundException fnf)
      {
         fnf.toString();
      }
      catch(IOException e)
      {
         e.toString();
      }
      catch(Exception e)
      {
         e.toString();
      }
   }
   
   private void getFile(String fileName)  //completed not tested
   {
      String filePath = "Files\\" + fileName;
      FileOutputStream outStreamFile; //used for writing local files
      try
      {
         outStreamFile  = new FileOutputStream(filePath);
         BufferedOutputStream out = new BufferedOutputStream(outStreamFile);
         byte[] buffer = new byte[CHUNK_SIZE];
         int numBytes = readDataSock.read(buffer);
         while(numBytes != -1)
         {
            outStreamFile.write(buffer, 0, numBytes);
         }
         out.close();
         outStreamFile.close();
         outStreamFile = null;      //for clarification
      }
      catch(FileNotFoundException e)
      {
         e.toString();
      }
      catch(IOException e)
      {
         e.toString();
      }
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
         writeCtrlSock.writeBytes(toSend);
         byte[] buffer = new byte[CHUNK_SIZE];
         int numBytes;
         numBytes = inRequests.read(buffer);
         while(numBytes != -1)
         {
            writeCtrlSock.write(buffer, 0, numBytes);
            numBytes = inRequests.read(buffer);
         }
      }
      catch(IOException e)
      {
         e.toString();
      }
   }
}
