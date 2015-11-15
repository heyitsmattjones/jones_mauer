
import java.net.InetAddress;

/**
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
*/

/**
 
 @author Matt
 */
public class Tester 
{
   public static void main(String[] args)
   {
      try
      {
         InetAddress addr = InetAddress.getByName("0.0.0.0");
         PingMessage pm = new PingMessage(addr, 5721, "tester1");
         for (int i = 0; i < 10; i++)
            System.out.println(pm.getPayload());
      }
      catch (Exception e)
      {
         
      }
   }
}
