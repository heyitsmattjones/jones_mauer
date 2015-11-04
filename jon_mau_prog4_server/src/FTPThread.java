
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
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
   private Socket sock = null;  //contorl socket connection  
   private Socket dataSock;
   private ServerSocket dataServer;   //data socket connection
   private final int DATA_PORT;    //may need to change
   private final int CHUNK_SIZE = 1024;
   private FileInputStream inStreamFile;  //used for sending a local file
   private BufferedReader readCtrlBuff;      //Control reader (reads reqs)
   private DataOutputStream writeCtrlSock;   //Control output stream
   private DataOutputStream writeDataSock;   //Data output stream
   private DataInputStream readDataSock;     //Data input stream
   
   public FTPThread(Socket inSock, int DataPortNum)
   {
      DATA_PORT = DataPortNum;
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
      sendList();
      readCommand();
   }
   
   private void readCommand()
   {
      try
      {
         String request = readCtrlBuff.readLine();
         System.out.println("SUCCESS :D");
         if (request == null)
            throw new Exception("null request send from client");
         StringTokenizer st = new StringTokenizer(request);
         String method = st.nextToken();  //1st should be GET or PUT
         if(method.equalsIgnoreCase("GET"))
         {
            //perform GET Operations (giving to client)
            performGet(st.nextToken());  //2nd should be file name
            
         }
         else if(method.equals("PUT"))
         {
            //perform PUT operations (getting from client)
            performPut(st.nextToken());   //2nd should be filename
            
         }
         else
            throw new Exception("Invalid request sent: " + method);
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
   
   private void sendList()
   {
      try
      {
         System.out.println("Sending List");
         writeCtrlSock.writeChars(listFiles());
         writeCtrlSock.flush();
         System.out.println("List sent");
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   private void performGet(String fileName)
   {
      try
      {
         openDataPort();
         writeDataSock = new DataOutputStream(dataSock.getOutputStream());
         sendFile(fileName);
         writeDataSock.close();
         closeDataConnections();
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   private void performPut(String fileName)
   {
      try
      {
         openDataPort();
         readDataSock = 
               new DataInputStream(
                     new BufferedInputStream(dataSock.getInputStream()));
         getFile(fileName);
         readDataSock.close();
         closeDataConnections();
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   private String listFiles()  //completed, not verified
   {
      String toSend = "";
      try
      {
         File dir = new File("Files");     //creates File & sets file pathname, I think this specifies which folder to look in.
         File[] files = dir.listFiles();   //get the list of all files in dir
         int j = 0;
         for (File file : files)
         {
            if (file.isFile())
            {
               toSend += files[j++].getName() + " ";
            }
         }
         toSend += "\n";
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
         System.out.println("UH oH");
      }
      return toSend;
   }
   
   //sending file to client
   private void sendFile(String fileName) //completed, not tested, make sure writeDataSock is initialized
   {
      System.out.println("Sending the file...");
      int fileSize = 0;
      try
      {
         if (writeDataSock == null)
            throw new Exception("DataOutput not initialized");
         inStreamFile = new FileInputStream(fileName);
         byte[] buffer = new byte[CHUNK_SIZE];
         int numBytes = inStreamFile.read(buffer); //number of bytes read
         while(numBytes != -1)
         {
            fileSize += numBytes;
            writeDataSock.write(buffer, 0, numBytes);
            numBytes = inStreamFile.read(buffer);
         }
         System.out.println("File: " + fileName);
         System.out.println(fileSize + " bytes sent.");
      }
      catch(FileNotFoundException fnf)
      {
         fnf.toString();
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
   
   //getting a file from the client
   private void getFile(String fileName)  //completed not tested
   {
      String filePath = "Files\\" + fileName;
      FileOutputStream outStreamFile; //used for writing local files
      System.out.println("Receiving file...");
      int fileSize = 0;
      try
      {
         outStreamFile  = new FileOutputStream(filePath);
         BufferedOutputStream out = new BufferedOutputStream(outStreamFile);
         byte[] buffer = new byte[CHUNK_SIZE];
         int numBytes = readDataSock.read(buffer);
         while(numBytes != -1)
         {
            fileSize += numBytes;
            outStreamFile.write(buffer, 0, numBytes);
            numBytes = readDataSock.read(buffer);     //added
         }
         out.close();
         outStreamFile.close();
         System.out.println("Got the file: " + fileName);
         System.out.println("Size: " + fileSize + " bytes");
      }
      catch(FileNotFoundException e)
      {
         System.out.println(e.toString());
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   private void openDataPort()   //complete not tested
   {
      try
      {
         writeCtrlSock.writeInt(DATA_PORT); //send the client the data port
         dataServer = new ServerSocket(DATA_PORT);
         dataSock = dataServer.accept();  //creates a socket
         System.out.println("Established Data Connections with "
                  + dataSock.getInetAddress().toString() + " on Port #"
                  + DATA_PORT);
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   private void closeDataConnections() //complete not tested
   {
      try
      {
         writeDataSock.close();
         dataSock.close();
         dataServer.close();
         System.out.println("Data connection closed.");
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
}