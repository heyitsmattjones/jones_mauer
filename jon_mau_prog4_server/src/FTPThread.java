/**
Program 4 consists of a basic FTP server and client that utilizes the File
Transfer Protocol to transfer files from the client to the server and from
the server to the client. This program runs a server and waits for a
request from a client, and it also includes the client that connects to the
server. It then sets up a separate connection for each FTP data request and
transfers the appropriate files / information.
@author Matt Jones & Paul Mauer
*/
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;

/**
FTPThread handles the interactions and file transfers with a specific client.
Control communication is kept alive until the client disconnects. Data
communication sockets are created on demand and closed after use.
Extends Thread.
@author Matthew P Jones & Paul Mauer
*/
public class FTPThread extends Thread
{
   private Socket sock = null;   //contorl socket connection  
   private Socket dataSock;      //data socket connection
   private ServerSocket dataServer;   //data socket connection
   private final int DATA_PORT;    //passed in initially
   private final int CHUNK_SIZE = 1024;   //maximum byte size for x-fer
   private FileInputStream inStreamFile;  //used for sending a local file
   private BufferedReader readCtrlBuff;      //Control reader (reads reqs)
   private PrintWriter writeCtrlSock;   //Control output stream
   private DataOutputStream writeDataSock;   //Data output stream
   private DataInputStream readDataSock;     //Data input stream
   private boolean stillConnected;           //is client still connected
   
   /**
   FTPTread constructor takes in a socket connection to the client and a 
   specific data port number for file transfer communication.
   @param inSock  socket connection to client (Control Connection)
   @param DataPortNum   port number for data communication
   */
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
         writeCtrlSock = new PrintWriter(sock.getOutputStream(),true);
         writeDataSock = null;   //initiallize only when needed
         inStreamFile = null;    //initiallized when sending a file
         readDataSock = null;    //initiallized when data is being received
         stillConnected = true;  //currently connnected to client
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   @Override
   /**
   run method manages whether to send the list of files or to listen and act
   on a response. If the client disconnects, calls methods to terminate
   control connection.
   */
   public void run()
   {
      sendList();    //initially send the list
      while(stillConnected)
      {
         readCommand(); //list is automatically sent after command is executed
      }
      closeControlConnections();
   }
   
   /**
   readCommand listens for a GET or PUT request from the client and calls 
   appropriate acting methods if these command are sent. If an invalid
   request is sent, it will display on the server console.
   */
   private void readCommand()
   {
      try
      {
         String request = readCtrlBuff.readLine(); //read command from client
         if (request == null)
            throw new Exception("null request send from client");
         StringTokenizer st = new StringTokenizer(request);
         String method = st.nextToken();  //1st should be GET or PUT
         switch (method)
         {
            case "GET":
               //perform GET Operations (giving to client)
               performGet(st.nextToken());  //2nd should be file name
               break;
            case "PUT":
               //perform PUT operations (getting from client)
               performPut(st.nextToken());   //2nd should be filename
               break;
            default:
               throw new Exception("Invalid request sent: " + method);
         }
         sendList(); //resend the new list
      }
      catch(SocketException e)
      {
         stillConnected = false; //client has disconnected
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
      catch(Exception e)
      {
         stillConnected = false ;   //client has disconnencted
      }
   }
   
   /**
   sendLists sends a string to the client over the control socket with the 
   contents of the files in the ./Files/ directory.
   */
   private void sendList()
   {
      try
      {
         writeCtrlSock.println(listFiles());
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
   }
   
   /**
   performGet calls sub methods to send a file to the client. It also opens
   and closes the writeDataSock
   @param fileName name of the requested file by the client
   */
   private void performGet(String fileName)
   {
      try
      {
         openDataPort();   //open data connections
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
   
   /**
   performPut calls sub methods to retrieve a file from the client. It also
   manages readDataSock.
   @param fileName   name of the file being sent by the client
   */
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
   
   /**
   listFiles creates a string containing all the filenames on the server
   separated by a space.
   @return String of filenames
   */
   private String listFiles()  //completed, not verified
   {
      String toSend = "";
      try
      {
         File dir = new File("./Files");    //creates File, sets file pathname
         File[] files = dir.listFiles();   //get the list of all files in dir
         int j = 0;  //file counter, needed incase of folders in Files
         for (File file : files)
         {
            if (file.isFile())
            {
               toSend += files[j++].getName() + " ";
            }
         }
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
      }
      return toSend;
   }
   
   /**
   sendFile send the requested file to the client over the data connection
   @param fileName file requested by client
   */
   private void sendFile(String fileName)
   {
      System.out.println("Sending the file...");
      int fileSize = 0;
      try
      {
         if (writeDataSock == null)
            throw new Exception("DataOutput not initialized");
         inStreamFile = new FileInputStream("./Files/" + fileName);
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
         writeDataSock.close();
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
   
   /**
   getFile downloads the file sent by the client. It stores it as fileName.
   If the file cannot be written to it will throw FileNotFoundException
   @param fileName name of file being sent by client
   */
   private void getFile(String fileName)
   {
      String filePath = "./Files/" + fileName;
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
            out.write(buffer, 0, numBytes);
            numBytes = readDataSock.read(buffer);
         }
         out.close();   //closes the file as it should be all downloaded
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
   
   /**
   Creates a new data socket connection for file transfer. Send the 
   port number that the client should connect to over the control connection.
   */
   private void openDataPort()
   {
      try
      {
         String toSend = "" + DATA_PORT;
         writeCtrlSock.println(toSend); //send the client the data port
         dataServer = new ServerSocket(DATA_PORT);
         dataSock = dataServer.accept();  //creates a socket
         System.out.println("Established Data Connections with "
                  + dataSock.getInetAddress().toString() + " on Port #"
                  + dataSock.getPort() + " with local port: " + DATA_PORT);
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   /**
   Closes the data socket connection. read/write streams should be closed at
   the time this is called
   */
   private void closeDataConnections()
   {
      try
      {
         //writeDataSock.close();
         dataSock.close();
         dataServer.close();
         System.out.println("Data connection closed.");
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
   
   /**
   closeControlConnections closes the connections of the control socket after
   the client has disconnected from the server.
   */
   private void closeControlConnections()
   {
      try
      {
         writeCtrlSock.close();
         readCtrlBuff.close();
         sock.close();
         System.out.println("Disconnected from Client");
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
}
