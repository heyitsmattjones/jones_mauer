/**
Program 5 consists of a basic UDP server and client.
The server listens for a packet from the client and then echoes the packet
back to the client if there is no packet loss.
@author Paul Mauer & Matt Jones
*/
import java.io.*;
import java.net.*;
import java.util.Date;

/**
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
*/

/**
 
 @author Matt
 */
public class PingClient extends UDPPinger implements Runnable
{
   private final int ONE_SEC = 1000;
   private final int FIVE_SEC = 5000;
   private final int NUM_PINGS = 10;
   private InetAddress serv_IP;
   private final int SERV_PORT = 5700;
   private long maxTime, minTime, sumRTT;
   
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
   
   public static void main(String[] args)
   {
      Thread t = new Thread(new PingClient(), "Ping Client");
      t.start();      
   }

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
                  + " " + rTime);
            long rtt = receiveTime - sendTime;
            if(!received && i < NUM_PINGS - 1)  //if we didnt get a response
               rtt = ONE_SEC;
            else if(!received)
               rtt = FIVE_SEC;
            sumRTT += rtt;
            computeLimits(rtt);
            timingOutput += "PING " + i +": " + received + " RTT: " + rtt
                  + "\n";
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
   
   private void computeRTT()
   {
      long avg = sumRTT / (long)10;
      System.out.println("Minimum = " + minTime +"ms, Maximum = " + maxTime
         + "ms, Average = " + avg + "ms.");
   }
   
   /**
   Checks if current RTT is a min or max
   @param inNum 
   */
   private void computeLimits(long inNum)
   {
      if(inNum < minTime)
         minTime = inNum;
      if(inNum > maxTime)
         maxTime = inNum;
   }
}
