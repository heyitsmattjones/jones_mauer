/**
Program 4 consists of a basic FTP server and client that utilizes the File
Transfer Protocol to transfer files from the client to the server and from
the server to the client. This program runs a server and waits for a
request from a client, and it also includes the client that connects to the
server. It then sets up a separate connection for each FTP data request and
transfers the appropriate files / information.
@author Matt Jones & Paul Mauer
*/
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.ListSelectionModel;

/**
FTP_Client is the GUI client application that connects to a FTP server.
It provides functionality to initiate a connection with a FTP server and
transfer files back and forth as requested.
@author Paul Mauer & Matt Jones
*/
public class FTP_Client extends javax.swing.JFrame
{
   Socket controlSock = null;    // Socket used to connect to Server
   Socket dataSock = null;       // Socket used to transfer data with Server
   String filename = "";
   boolean fileExists = true;
   boolean lineIsValid = true;
   Vector remoteFilesList;
   Vector localFilesList;
   String hostAddress = "";
   PrintWriter writeControlSock;    // Used to write data to control socket
   BufferedReader readControlSock;  // Used to read data from control socket
   DataOutputStream writeDataSock;       // Used to write data to data socket
   DataInputStream readDataSock;     // Used to read data from data socket
   private final int CHUNK_SIZE = 1024;
   
   /**
   FTP_Client Constructor: Creates new form FTP_Client and initializes data
   and components
   */
   public FTP_Client()
   {
      this.localFilesList = new Vector();
      this.remoteFilesList = new Vector();
      initComponents();
      this.setLocationRelativeTo(null);
      listLocalFiles();
      serverFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      clientFileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
   }

   /**
    This method is called from within the constructor to initialize the form.
    WARNING: Do NOT modify this code. The content of this method is always
    regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
   // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
   private void initComponents()
   {

      jScrollPane1 = new javax.swing.JScrollPane();
      outputTxtArea = new javax.swing.JTextArea();
      connectionBtn = new javax.swing.JButton();
      getBtn = new javax.swing.JButton();
      putBtn = new javax.swing.JButton();
      hostLbl = new javax.swing.JLabel();
      portLbl = new javax.swing.JLabel();
      hostTxtFld = new javax.swing.JTextField();
      portTxtFld = new javax.swing.JTextField();
      jScrollPane2 = new javax.swing.JScrollPane();
      serverFileList = new javax.swing.JList();
      jScrollPane3 = new javax.swing.JScrollPane();
      clientFileList = new javax.swing.JList();
      serverFilesLbl = new javax.swing.JLabel();
      clientFilesLbl = new javax.swing.JLabel();
      outputMsgLbl = new javax.swing.JLabel();

      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
      setTitle("*COMPLETELY LEGAL* Awesome FTP Tool + Ultra Platinum Premium Rewards ©1993-2015");
      setResizable(false);

      outputTxtArea.setColumns(20);
      outputTxtArea.setRows(5);
      jScrollPane1.setViewportView(outputTxtArea);

      connectionBtn.setText("Connect");
      connectionBtn.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            connectionBtnActionPerformed(evt);
         }
      });

      getBtn.setText("Get");
      getBtn.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            getBtnActionPerformed(evt);
         }
      });

      putBtn.setText("Put");
      putBtn.addActionListener(new java.awt.event.ActionListener()
      {
         public void actionPerformed(java.awt.event.ActionEvent evt)
         {
            putBtnActionPerformed(evt);
         }
      });

      hostLbl.setText("Host");

      portLbl.setText("Port");

      hostTxtFld.setText("localhost");

      portTxtFld.setText("5721");

      jScrollPane2.setViewportView(serverFileList);

      jScrollPane3.setViewportView(clientFileList);

      serverFilesLbl.setText("Server Files");

      clientFilesLbl.setText("Client Files");

      outputMsgLbl.setText("Output Messages");

      javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
      getContentPane().setLayout(layout);
      layout.setHorizontalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(outputMsgLbl)
                  .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
               .addGroup(layout.createSequentialGroup()
                  .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                     .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                     .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                              .addGroup(layout.createSequentialGroup()
                                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                       .addComponent(hostLbl)
                                       .addGap(18, 18, 18))
                                    .addGroup(layout.createSequentialGroup()
                                       .addComponent(portLbl)
                                       .addGap(23, 23, 23)))
                                 .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(hostTxtFld)
                                    .addGroup(layout.createSequentialGroup()
                                       .addComponent(portTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                       .addGap(18, 18, 18)
                                       .addComponent(connectionBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                              .addComponent(getBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                              .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                           .addComponent(serverFilesLbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                           .addComponent(clientFilesLbl)
                           .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                              .addComponent(jScrollPane3)
                              .addComponent(putBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)))))
                  .addGap(32, 32, 32))))
      );
      layout.setVerticalGroup(
         layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
         .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(hostLbl)
               .addComponent(hostTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGap(14, 14, 14)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(portLbl)
               .addComponent(portTxtFld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
               .addComponent(connectionBtn))
            .addGap(32, 32, 32)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
               .addComponent(serverFilesLbl)
               .addComponent(clientFilesLbl))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(putBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
               .addGroup(layout.createSequentialGroup()
                  .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                  .addComponent(getBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGap(32, 32, 32)
            .addComponent(outputMsgLbl)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(32, Short.MAX_VALUE))
      );

      pack();
   }// </editor-fold>//GEN-END:initComponents

   private void connectionBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_connectionBtnActionPerformed
   {//GEN-HEADEREND:event_connectionBtnActionPerformed
      if (!isConnectedToServer())
         openControlSocket();
      else
         closeControlSocket();
   }//GEN-LAST:event_connectionBtnActionPerformed

   private void getBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_getBtnActionPerformed
   {//GEN-HEADEREND:event_getBtnActionPerformed
      if (isConnectedToServer())
      {
         if (serverFileList.getSelectedIndex() != -1)
            getFile(serverFileList.getSelectedValue().toString());
         else
            writeCommLine("Select a file first!");
      }
      else
         writeCommLine("Not connected to server!");
   }//GEN-LAST:event_getBtnActionPerformed

   private void putBtnActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_putBtnActionPerformed
   {//GEN-HEADEREND:event_putBtnActionPerformed
      if (isConnectedToServer())
      {
         if (clientFileList.getSelectedIndex() != -1)
            sendFile(clientFileList.getSelectedValue().toString());
         else
            writeCommLine("Select a file first!");
      }
      else
         writeCommLine("Not connected to server!");
   }//GEN-LAST:event_putBtnActionPerformed

   /**
   Main method of FTP_Client
   Runs an instance of the GUI application
   @param args the command line arguments
   */
   public static void main(String args[])
   {
      /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
       * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
       */
      try
      {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
         {
            if ("Nimbus".equals(info.getName()))
            {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      }
      catch (ClassNotFoundException ex)
      {
         java.util.logging.Logger.getLogger(FTP_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (InstantiationException ex)
      {
         java.util.logging.Logger.getLogger(FTP_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (IllegalAccessException ex)
      {
         java.util.logging.Logger.getLogger(FTP_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
      catch (javax.swing.UnsupportedLookAndFeelException ex)
      {
         java.util.logging.Logger.getLogger(FTP_Client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
      }
        //</editor-fold>

      /* Create and display the form */
      java.awt.EventQueue.invokeLater(new Runnable()
      {
         @Override
         public void run()
         {
            new FTP_Client().setVisible(true);
         }
      });
   }
   
   /**
   Listens for a list of remote files from the server and formats them to
   display in a JList for file selection.
   */
   private void listRemoteFiles()
   {
      try
      {
         String remoteFiles = readControlSock.readLine();
         if (remoteFiles == null)
         {
            lineIsValid = false;
            return;
         }
         StringTokenizer st = new StringTokenizer(remoteFiles);
         int numTokens = st.countTokens();
         remoteFilesList.clear();
         for (int i = 0; i < numTokens; i++)
         {
            remoteFilesList.add(st.nextToken());
         }
         serverFileList.setListData(remoteFilesList);
      }
      catch (IOException ex)
      {
         System.out.println(ex.toString());
      }
   }
   
   /**
   Generates a list of all local files from the client and formats them to
   display in a JList for file selection.
   */
   private void listLocalFiles()
   {
      File dir = new File("./Files");
      File[] files = dir.listFiles();
      localFilesList.clear();
      for (File file : files)
      {
         if (file.isFile())
         {
            localFilesList.add(file.getName());
         }
      }
      clientFileList.setListData(localFilesList);
   }
   
   /**
   Sends a file to the server.
   Tells the server to receive a file and what the filename is.
   Then sends the file in chunks defined by CHUNK_SIZE until the transfer is
   complete.
   @param filename is the name of the file to send to the server.
   */
   private void sendFile(String filename)
   {
      try
      {
         writeControlSock.println("PUT " + filename);
         openDataSocket();
         writeDataSock = new DataOutputStream(dataSock.getOutputStream());
         FileInputStream fileToSend = new FileInputStream("./Files/" + filename);
         byte[] buffer = new byte[CHUNK_SIZE];
         writeCommLine("Sending the file...");
         writeCommLine("File: " + filename);
         int numBytes = fileToSend.read(buffer); //number of bytes read
         int sizeOfSentFile = 0;
         while(numBytes != -1)
         {
            writeDataSock.write(buffer, 0, numBytes);
            sizeOfSentFile += numBytes;
            numBytes = fileToSend.read(buffer);
         }
         writeCommLine(sizeOfSentFile + " bytes sent.");
      }
      catch (IOException ex)
      {
         
      }
      finally
      {
         try
         {
            writeDataSock.close();
            writeCommLine("Data Connection Closed.");
            updateFileLists();
         }
         catch (IOException ex)
         {
            
         }
      }
   }
   
   /**
   Receives a file from the server.
   Tells the server to send a file and what the filename is.
   Then receives the file in chunks defined by CHUNK_SIZE until the transfer
   is complete.
   @param filename is the name of the file to receive from the server.
   */
   private void getFile(String filename)
   {
      try
      {
         writeControlSock.println("GET " + filename);
         openDataSocket();
         readDataSock = new DataInputStream(new BufferedInputStream(dataSock.getInputStream()));
         FileOutputStream outStreamFile = new FileOutputStream("./Files/" + filename);
         BufferedOutputStream fileToReceive = new BufferedOutputStream(outStreamFile);
         byte[] buffer = new byte[CHUNK_SIZE];
         writeCommLine("Receiving the file...");
         int numBytes = readDataSock.read(buffer); //number of bytes read
         int sizeOfReceivedFile = 0;
         while(numBytes != -1)
         {
            fileToReceive.write(buffer, 0, numBytes);
            sizeOfReceivedFile += numBytes;
            numBytes = readDataSock.read(buffer);
         }
         fileToReceive.close();
         outStreamFile.close();
         writeCommLine("Got the file: " + filename);
         writeCommLine("Size: " + sizeOfReceivedFile + " Bytes.");
      }
      catch (IOException ex)
      {
         System.out.println(ex.toString());
      }
      finally
      {
         try
         {
            readDataSock.close();
            writeCommLine("Data Connection Closed.");
            updateFileLists();
         }
         catch (IOException ex)
         {
            
         }
      }
   }
   
   /**
   Checks the status of the connection to the server.
   @return true if a connection exists, false otherwise
   */
   public boolean isConnectedToServer()
   {
      return connectionBtn.getText().equals("Disconnect");
   }
   
   /**
   Establishes a control connection with the specified FTP server.
   Appropriate error messages are printed if the program is unable to
   establish a connection with the specified server of if the port number is
   invalid.
   */
   public void openControlSocket()
   {
      try
      {
         if (addressAndPortNumHaveValue())
         {
            int portNum = Integer.parseInt(portTxtFld.getText());
            hostAddress = hostTxtFld.getText();
            controlSock = new Socket(hostAddress, portNum);
            readControlSock = new BufferedReader(
                  new InputStreamReader(controlSock.getInputStream()));
            writeControlSock = 
                  new PrintWriter(controlSock.getOutputStream(), true);
            connectedToServer();
            listRemoteFiles();
         }
      }
      catch (IOException ex)
      {
         writeCommErrorLine("Unable to establish a connection", ex);
      }
      catch (NumberFormatException ex)
      {
         writeCommErrorLine("Invalid port number", ex);
      }
   }
   
   /**
   Establishes a data connection with the specified FTP server.
   Appropriate error messages are printed if the program is unable to
   establish a connection with the specified server.
   */
   public void openDataSocket()
   {
      try
      {
         String dataPortNum = readControlSock.readLine();
         int portNum = Integer.parseInt(dataPortNum);
         System.out.println(portNum +"");
         dataSock = new Socket(hostAddress, portNum);
         System.out.println("Socket Created");
      }
      catch (IOException ex)
      {
         writeCommErrorLine("Unable to establish data connection", ex);
      }
   }
   
   /**
   Checks for null values in the IP Address and Port Number text fields.
   @return true if neither the address nor the port number are null,
   false otherwise
   */
   public boolean addressAndPortNumHaveValue()
   {
      if (hostTxtFld.getText().equals(""))
      {
         writeCommLine("Enter server address!");
         return false;
      }
      
      if (portTxtFld.getText().equals(""))
      {
         writeCommLine("Enter server port number!");
         return false;
      }
      return true;
   }
   
   /**
   Terminates a connection with the connected server. Appropriate error
   messages are printed if the program encounters a problem closing the
   connection with the server.
   */
   public void closeControlSocket()
   {
      try
      {
         controlSock.close();
         readControlSock.close();
         writeControlSock.close();
         disconnectedFromServer(controlSock);
      }
      catch (IOException ex)
      {
         writeCommErrorLine("Problem closing connection", ex);
         disconnectedFromServer(controlSock);
      }
   }
   
   public void closeDataSocket()
   {
      try
      {
         dataSock.close();
      }
      catch (IOException ex)
      {
         writeCommErrorLine("Problem closing connection", ex);
      }
      finally
      {
         dataSock = null;
         writeCommLine("Data Connection Closed.");
      }
   }
   
   /**
   Prints a message in the window and changes the text on the connection
   button to "Disconnect" when the connection with the server is established.
   */
   public void connectedToServer()
   {
      writeCommLine("Connected to Server");
      connectionBtn.setText("Disconnect");
   }
   
   /**
   Prints a message in the window and changes the text on the connection
   button to "Connect" when the connection with the server is terminated.
    * @param sock
   */
   public void disconnectedFromServer(Socket sock)
   {
      writeCommLine("Disconnected from Server");
      connectionBtn.setText("Connect");
      sock = null;
   }
   
   /**
   Writes a line of text to the Client/Server Communication text area
   @param textToWrite is the text to be printed in the window
   */
   public void writeCommLine(String textToWrite)
   {
      outputTxtArea.append(textToWrite + "\n");
   }
   
   /**
   Writes an error message to the Client/Server Communication text area
   including the exception thrown as a result of the error.
   @param errorMsg is the written text description of the error
   @param ex is the exception thrown as a result of the error
   */
   public void writeCommErrorLine(String errorMsg, Exception ex)
   {
      outputTxtArea.append("Error: " + errorMsg + "\n     " + ex.toString() + "\n");
   }
   
   private void updateFileLists()
   {
      listRemoteFiles();
      listLocalFiles();
   }

   // Variables declaration - do not modify//GEN-BEGIN:variables
   private javax.swing.JList clientFileList;
   private javax.swing.JLabel clientFilesLbl;
   private javax.swing.JButton connectionBtn;
   private javax.swing.JButton getBtn;
   private javax.swing.JLabel hostLbl;
   private javax.swing.JTextField hostTxtFld;
   private javax.swing.JScrollPane jScrollPane1;
   private javax.swing.JScrollPane jScrollPane2;
   private javax.swing.JScrollPane jScrollPane3;
   private javax.swing.JLabel outputMsgLbl;
   private javax.swing.JTextArea outputTxtArea;
   private javax.swing.JLabel portLbl;
   private javax.swing.JTextField portTxtFld;
   private javax.swing.JButton putBtn;
   private javax.swing.JList serverFileList;
   private javax.swing.JLabel serverFilesLbl;
   // End of variables declaration//GEN-END:variables
}
