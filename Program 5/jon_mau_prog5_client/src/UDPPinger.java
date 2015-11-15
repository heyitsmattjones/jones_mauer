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
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
*/

/**
 
 @author Matt
 */
public class UDPPinger 
{
   private final int PACKET_SIZE = 512;
   private int myPort;
   protected DatagramSocket dataSock;
   protected boolean received;
   
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
   
   @param ping 
   */
   public void sendPing(PingMessage ping)
   {
      DatagramPacket dataPack;
      byte[] byteLoad =  ping.getPayload().getBytes(); //convert string to byte
      dataPack = new DatagramPacket(byteLoad,
            byteLoad.length, ping.getIP(), ping.getPort());
      //System.out.println(dataPack.getLength() + "xxxxx " + dataPack.getOffset());
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
   
   @return 
   */
   public PingMessage receivePing()
   {
      PingMessage pm = null;
      try
      {
         byte[] receiveData = new byte[PACKET_SIZE];
         DatagramPacket receivePacket = new DatagramPacket(receiveData,
            PACKET_SIZE); //check if packet size is the right thing to use here
         dataSock.receive(receivePacket);  //hopefully receive a packet from paul
         pm = new PingMessage(receivePacket.getAddress(),
            receivePacket.getPort(), Arrays.toString(receivePacket.getData()));
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
