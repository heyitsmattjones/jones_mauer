
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
