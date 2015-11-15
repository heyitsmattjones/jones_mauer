/**
Program 5 consists of a basic UDP server and client.
The server listens for a packet from the client and then echoes the packet
back to the client if there is no packet loss.
@author Paul Mauer & Matt Jones
*/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
PingServer provides a UDP service for the client.
@author Paul Mauer
*/
public class PingServer
{
   //========== DEFINE CONSTANTS - START ==========
   
   //use any port number between 5700 and 5800 to receive the UDP packets
   private static final int PORT_NUMBER = 5700;
   
   //determines the percentage of packets that should be lost
   private static final float LOSS_RATE = 0.3f;
   
   //simulate transmission delay (milliseconds)
   //Set AVERAGE_DELAY = 0 to find out the true RTT of the packets
   //(default is 100)
   private static final int AVERAGE_DELAY = 100;
   
   //UDP packet size
   private static final int PACKET_SIZE = 512;
   
   //DOUBLE = 2; used to simulate transmission delay
   private static final int DOUBLE = 2;
   
   //=========== DEFINE CONSTANTS - END ===========
   
   
   
   //====== DEFINE CLASS ATTRIBUTES - START =======
   
   //socket for receiving UDP packets
   public DatagramSocket udpSocket;
   
   //======= DEFINE CLASS ATTRIBUTES - END ========
   
   
   
   /**
   Ping Server constructor
   */
   public PingServer()
   {
      try
      {
         this.udpSocket = new DatagramSocket(PORT_NUMBER);
      }
      catch (SocketException ex)
      {
         printLine("Unable to use Port: " + PORT_NUMBER + " to create a "
               + "UDP socket", ex);
      }
   }
   
   /**
   Main method to start the server
   @param args
   */
   public static void main(String[] args)
   {
      //Create server
      PingServer pingServer = new PingServer();
      //Start server
      pingServer.run();
   }
   
   /**
   Runs the server
   */
   public void run()
   {
      //allocate the memory space for an UDP packet
      byte[] buff = new byte[PACKET_SIZE];

      //make an empty UDP packet
      DatagramPacket inpacket = new DatagramPacket(buff, PACKET_SIZE);
      
      int sleepTime;
      
      boolean packetReceivedSuccessfully;
      printLine("Ping Server running....");
      while(true)
      {
         packetReceivedSuccessfully = false;
         //clear sleepTime value
         sleepTime = -1;
         
         try
         {
            printLine("Waiting for UDP packet....");
            
            //receive the next UDP packet
            //May throw IOException
            udpSocket.receive(inpacket);
            
            packetReceivedSuccessfully = true;
            printLine("Received from: " + inpacket.getAddress()
                  + Arrays.toString(inpacket.getData()));
            
            //artificially simulate the effects of network packet loss.
            //generate a random number between 0 and 1;
            //it’s a packet loss if the random number is less than LOSS_RATE
            Random random = new Random(new Date().getTime());
            
            //If true: No Packet Loss; else Packet Loss
            if (random.nextFloat() >= LOSS_RATE)
            {
               //simulate transmission delay; DOUBLE = 2
               //May throw InterruptedException
               sleepTime = (int)(random.nextDouble() * DOUBLE * AVERAGE_DELAY);
               Thread.sleep(sleepTime);

               //make an outgoing UDP packet
               DatagramPacket outpacket = new DatagramPacket(inpacket.getData(),
                     inpacket.getLength(), inpacket.getAddress(), inpacket.getPort());

               //send an UDP packet
               //May throw IOException
               udpSocket.send(outpacket);
               
               printLine("Reply sent.");
            }
            else
               printLine("Packet loss...., reply not sent.");
         }
         catch (IOException ex)
         {
            if (packetReceivedSuccessfully)
               printLine("There was a problem sending the packet", ex);
            else
               printLine("There was a problem receiving the packet", ex);
         }
         catch (InterruptedException ex)
         {
            if (sleepTime == -1)
            {
               printLine("There was a problem telling the program to sleep", ex);
            }
            printLine("There was a problem telling the program to sleep for "
                  + sleepTime + " milliseconds", ex);
         }
      }
   }
   
   /**
   Prints a message to the system output
   @param msg is the text to be printed to the System Output
   */
   private void printLine(String textToWrite)
   {
         System.out.println(textToWrite);
   }
   
   /**
   Prints an error message to the system output including the exception
   thrown as a result of the error.
   @param errorMsg is the written text description of the error
   @param ex is the exception thrown as a result of the error
   */
   private void printLine(String errorMsg, Exception ex)
   {
      System.out.println("Error: " + errorMsg + "\n     " + ex.toString());
   }
}
