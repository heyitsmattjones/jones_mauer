/**
Program 5 consists of a basic UDP server and client.
The server listens for a packet from the client and then echoes the packet
back to the client if there is no packet loss.
@author Paul Mauer & Matt Jones
*/
import java.net.*;

/**
The PingMessage class contains all of the data that encapsulated in a ping
request. The data is separated as the dest. IP address, the dest. port#
and the payload, all of which can be accessed through getters.
@author Paul Mauer & Matthew Jones
*/
public class PingMessage
{
   private InetAddress ipAddress;
   private int portNum;
   private String payload;
   
   /**
   PingMessage constructor. Sets  the destination IP address, port number,
   and payload.
   @param addr: Destination IP address
   @param port: Destination IP number
   @param load: Payload to send
   */
   public PingMessage(InetAddress addr, int port, String load)
   {
      ipAddress = addr;
      portNum = port;
      payload = load;
   }

   /**
   getIP returns the InetAddress of the destination server.
   @return InetAddress of destination
   */
   public InetAddress getIP()
   {
      return ipAddress;
   }

   /**
   getPort returns the port number of the destination server
   @return int of server port number
   */
   public int getPort()
   {
      return portNum;
   }

   /**
   getPayload return the string representing the payload to be sent
   @return String of the payload
   */
   public String getPayload()
   {
      return payload;
   }   
}
