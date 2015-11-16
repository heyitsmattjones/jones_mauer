/**
Program 5 consists of a basic UDP server and client.
The server listens for a packet from the client and then echoes the packet
back to the client if there is no packet loss.
@author Paul Mauer & Matt Jones
*/
import java.net.*;
import java.util.Date;

/**
The PingClient class is responsible for creating and sending 10 Ping 
messages to the Ping Server. This class extends UDPPinger to send the messages
and implements runnable to become a java runnable interface. The Round Trip
Time (RTT) is calculated for each ping message and the average, max, and min
time will be displayed after all requests have been completed
@author Matthew Jones & Paul Mauer
*/
public class PingClient extends UDPPinger implements Runnable
{
   private final int ONE_SEC = 1000;
   private final int FIVE_SEC = 5000;
   private final int NUM_PINGS = 10;
   private InetAddress serv_IP;
   private final int SERV_PORT = 5700;
   private long maxTime, minTime, sumRTT;
   
   /**
   Constructor for PingClient. initializes RTT variables and sets the 
   server IP address
   */
   public PingClient()
   {
      serv_IP = null;
      minTime = FIVE_SEC;
      maxTime = 0;
      sumRTT = 0;
      try
      {
         serv_IP = InetAddress.getByName("localhost");
      }
      catch(UnknownHostException e)
      {
         System.out.println(e.toString());
      }
   }
   
   /**
   main method creates a new thread of PingClient and starts it.
   @param args 
   */
   public static void main(String[] args)
   {
      Thread t = new Thread(new PingClient(), "Ping Client");
      t.start();      
   }

   /**
   run method controls a loop that sends a ping for each iteration. It will
   display the RTT information when finished with all iterations.
   */
   @Override
   public void run()
   {
      System.out.println("Contacting host: " + serv_IP.getHostAddress() 
         + " at port " + SERV_PORT);
      String timingOutput = "";
      try
      {
         dataSock.setSoTimeout(ONE_SEC);  //One second timeout for pings
         for(int i = 0; i < NUM_PINGS; i++)
         {
            timingOutput += evalPing(i);
         }
         System.out.print(timingOutput);
         computeRTT();
      }
      catch (SocketException | NullPointerException ex)
      {
         System.out.println(ex.toString());
      }
      finally
      {
         dataSock.close();
      }
   }
   
   /**
   evalPing controls the process of sending and receiving a PingMessage to and
   from the server.
   @param i integer of the sequence number of ping
   @return String representing the RTT information of this specific ping
   */
   private String evalPing(int i)
   {
      try
      {
         if(i == NUM_PINGS - 1)
            dataSock.setSoTimeout(FIVE_SEC); //last ping has extra time
         long sendTime = new Date().getTime();
         String payload =  "PING " + i + " " + sendTime;
         PingMessage pm = new PingMessage(serv_IP, SERV_PORT, payload);
         sendPing(pm);
         PingMessage rm = receivePing();
         long receiveTime = new Date().getTime();
         String rTime = new Date().toString();
         if(received)
            System.out.println("Recieved packet from " + rm.getIP()
                  + " " + rm.getPort() + " " + rTime);
         long rtt = receiveTime - sendTime;
         if(!received && i < NUM_PINGS - 1)  //if we didnt get a response
            rtt = ONE_SEC;
         else if(!received)
            rtt = FIVE_SEC;
         sumRTT += rtt;
         computeLimits(rtt);
         return "PING " + i +": " + received + " RTT: " + rtt
               + "\n";
      }
      catch (SocketException ex)
      {
         System.out.println(ex.toString());
      }
      return "Error in evalPing";
   }
   
   /**
   compute RTT computes the average, maximum, and min RTT for the 10 ping
   messages. Prints the information out afterwards.
   */
   private void computeRTT()
   {
      long avg = sumRTT / (long)NUM_PINGS;
      System.out.println("Minimum = " + minTime +"ms, Maximum = " + maxTime
         + "ms, Average = " + avg + "ms.");
   }
   
   /**
   computeLimits checks if current RTT is a min or max and sets it
   accordingly.
   @param inNum RTT to be evaluated
   */
   private void computeLimits(long inNum)
   {
      if(inNum < minTime)
         minTime = inNum;
      if(inNum > maxTime)
         maxTime = inNum;
   }
}
