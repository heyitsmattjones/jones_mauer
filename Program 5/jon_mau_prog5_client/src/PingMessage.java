/**
Program 5 consists of a basic UDP server and client.
The server listens for a packet from the client and then echoes the packet
back to the client if there is no packet loss.
@author Paul Mauer & Matt Jones
*/
import java.net.*;

/**

@author Paul Mauer & Matthew Jones
*/
public class PingMessage
{
   private InetAddress ipAddress;
   private int portNum;
   private String payload;
   private int seqNum; //is this the correct way?
   
   /**
   @param addr
   @param port
   @param load 
   */
   public PingMessage(InetAddress addr, int port, String load)
   {
      ipAddress = addr;
      portNum = port;
      payload = load;
      seqNum = 0;
   }

   public InetAddress getIP()
   {
      return ipAddress;
   }

   public int getPort()
   {
      return portNum;
   }

   public String getPayload()
   {
      return payload;
   }   
}
