/**
Program 5 consists of a basic UDP server and client.
MORE TEXT GOES HERE
@author Paul Mauer & Matt Jones
*/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
Has UDP service
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
         Logger.getLogger(PingServer.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   
   /**
   Main method to start the server
   @param args
   */
   public static void main(String[] args)
   {
      //Run MAIN here
      PingServer pingServer = new PingServer();
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
      
      while(true)
      {
         try
         {
            //receive the next UDP packet
            //Throws IOException
            udpSocket.receive(inpacket);
            
            //artificially simulate the effects of network packet loss.
            //generate a random number between 0 and 1;
            //it’s a packet loss if the random number is less than LOSS_RATE
            Random random = new Random(new Date().getTime());
            
            //If true: No Packet Loss; else Packet Loss
            if (random.nextFloat() >= LOSS_RATE)
            {
               //simulate transmission delay; DOUBLE = 2
               //Throws InterruptedException
               Thread.sleep((int)(random.nextDouble() * DOUBLE * AVERAGE_DELAY));

               //make an outgoing UDP packet
               DatagramPacket outpacket = new DatagramPacket(inpacket.getData(),
                     inpacket.getLength(), inpacket.getAddress(), inpacket.getPort());

               //send an UDP packet
               //Throws IOException
               udpSocket.send(outpacket);
            }
         }
         catch (IOException ex)
         {
            Logger.getLogger(PingServer.class.getName()).log(Level.SEVERE, null, ex);
         }
         catch (InterruptedException ex)
         {
            Logger.getLogger(PingServer.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
}
