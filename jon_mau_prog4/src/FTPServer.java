/**

@author Pauer
*/
public class FTPServer
{
   private final int LISTEN_PORT = 5721;
   
   public static void main(String[] args)
   {
      try
      {
         FTPServer fileServ = new FTPServer();
         fileServ.run();
      }
      catch(Exception e)
      {
         e.toString();
      }
   }
   
   public void run()
   {
      
   }
}
