/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**

 @author Pauer
 */
public class FTP_Client extends javax.swing.JFrame
{
   Socket sock = null;       // Socket used to connect to Server
   String filename = "";
   boolean fileExists = true;
   boolean lineIsValid = true;
   
   /**
    Creates new form FTP_Client
    */
   public FTP_Client()
   {
      initComponents();
      this.setLocationRelativeTo(null);
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

      getBtn.setText("Get");

      putBtn.setText("Put");

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

   /**
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
         public void run()
         {
            new FTP_Client().setVisible(true);
         }
      });
   }
   
   private void listRemoteFiles()
   {
      try
      {
         BufferedReader readSock;  // Used to read data from socket
         readSock = new BufferedReader(new InputStreamReader(sock.getInputStream()));
         String remoteFiles = readSock.readLine();
         if (remoteFiles == null)
         {
            lineIsValid = false;
            return;
         }
         StringTokenizer st = new StringTokenizer(remoteFiles);
         
         remoteFiles = readSock.readLine();
      }
      catch (IOException ex)
      {
         
      }
   }
   
   private void listLocalFiles()
   {
      
   }
   
   private void sendFile(String filename)
   {
      
   }
   
   private void getFile(String filename)
   {
      
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
