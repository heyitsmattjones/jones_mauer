/**
Program 5 consists of a basic UDP server and client.
The server listens for a packet from the client and then echoes the packet
back to the client if there is no packet loss.
@author Paul Mauer & Matt Jones
*/
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
The UDPPinger class manages the socket connections with the client and
server and sends the desired datagram packet to the server over UDP and also
receives packets from the server and converting the data into a PingMessage.
@author Matthew Jones & Paul Mauer
*/
public class UDPPinger 
{
   private final int PACKET_SIZE = 512;
   private int myPort;
   protected DatagramSocket dataSock;
   protected boolean received;  //did we receive a packet from the server
   
   /**
   UDPPinger constructor. Initializes the datagram socket for UDP and
   automatically selects an available port on the client computer for 
   communication.
   */
   public UDPPinger()
   {
      try
      {
         dataSock = new DatagramSocket();
      }
      catch (SocketException ex)
      {
         System.out.println(ex.toString());
      }
   }
   
   /**
   sendPing creates and sends the Ping datagram packet using UDP.
   @param ping 
   */
   public void sendPing(PingMessage ping)
   {
      DatagramPacket dataPack;
      byte[] byteLoad =  ping.getPayload().getBytes(); //convert string to byte
      dataPack = new DatagramPacket(byteLoad,
         byteLoad.length, ping.getIP(), ping.getPort());
      try
      {
         myPort = dataSock.getLocalPort(); //may not be needed anymore
         //System.out.println("" + myPort); //print out the socket we assigned
         dataSock.send(dataPack);
      }
      catch(IOException e)
      {
         System.out.println(e.toString());
      }
   }
   
   /**
   receivePing listens for a packet sent by the Ping server and puts the 
   relevant information into a new PingMessage object
   @return PingMessage object sent by the server.
   */
   public PingMessage receivePing()
   {
      PingMessage pm = null;
      try
      {
         byte[] receiveData = new byte[PACKET_SIZE];
         DatagramPacket receivePacket = new DatagramPacket(receiveData,
            PACKET_SIZE);
         dataSock.receive(receivePacket);  //not receiving throws IOException 
         pm = new PingMessage(receivePacket.getAddress(),
            receivePacket.getPort(),Arrays.toString(receivePacket.getData()));
         received = true;
      }
      catch(IOException e)
      {
         System.out.println("receivePing..." + e.toString());
         received = false;
      }
      return pm;
   }
}
