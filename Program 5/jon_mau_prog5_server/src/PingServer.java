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
   
   boolean validPort = true;
   
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
         printLine("Unable to start server on port: " + PORT_NUMBER, ex);
         validPort = false;
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
   Runs the UDP server. Waits for an incoming packet, then replies by echoing
   the same data back to the client.
   */
   public void run()
   {
      if (!validPort)
         return;
      //allocate the memory space for an UDP packet
      byte[] buff = new byte[PACKET_SIZE];

      //make an empty UDP packet
      DatagramPacket inpacket = new DatagramPacket(buff, PACKET_SIZE);
      
      printLine("Ping Server running....");
      while(true)
      {
         if(receivePacket(inpacket)) //Receive a packet
         {
            //If true: Packet Loss; else No Packet Loss
            if(simulatePacketLoss())
               printLine("Packet loss...., reply not sent.");
            else
            {
               addTransmissionDelay();
               sendPacket(inpacket); //Send the response packet
            }
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

   /**
   Receives an incoming packet
   @param inpacket is the DatagramPacket to assign the incoming packet to
   @return true if a packet is successfully received; false otherwise
   */
   private boolean receivePacket(DatagramPacket inpacket)
   {
      try
      {
         printLine("Waiting for UDP packet....");
         
         //receive the next UDP packet
         //May throw IOException
         udpSocket.receive(inpacket);
         
         printLine("Received from: " + inpacket.getAddress() + " "
               + new String(inpacket.getData()).trim());
         return true;
      }
      catch (IOException ex)
      {
         printLine("There was a problem receiving the packet", ex);
         return false;
      }
   }

   /**
   Sends an outgoing packet
   @param inpacket is the DatagramPacket with the data to send
   */
   private void sendPacket(DatagramPacket inpacket)
   {
      try
      {
         //make an outgoing UDP packet
         DatagramPacket outpacket = new DatagramPacket(inpacket.getData(),
               inpacket.getLength(), inpacket.getAddress(), inpacket.getPort());
         
         //send an UDP packet
         //May throw IOException
         udpSocket.send(outpacket);
         
         printLine("Reply sent.");
      }
      catch (IOException ex)
      {
         printLine("There was a problem sending the packet", ex);
      }
   }
   
   /**
   Artificially simulate the effects of network packet loss.
   It is a packet loss if the random number is less than LOSS_RATE
   @return true if the random number is less than LOSS_RATE
   */
   private boolean simulatePacketLoss()
   {
      return getRandom().nextFloat() < LOSS_RATE;
   }
   
   /**
   generates a random number between 0 and 1;
   @return the Random object
   */
   private Random getRandom()
   {
      return new Random(new Date().getTime());
   }

   /**
   Simulates transmission delay
   */
   private void addTransmissionDelay()
   {
      int sleepTime = -1;
      try
      {
         sleepTime = (int)(getRandom().nextDouble() * DOUBLE * AVERAGE_DELAY);
         Thread.sleep(sleepTime);
      }
      catch (InterruptedException ex)
      {
         if (sleepTime == -1)
            printLine("There was a problem telling the program to sleep", ex);
         else
            printLine("There was a problem telling the program to sleep for "
                  + sleepTime + " milliseconds", ex);
      }
   }
}
