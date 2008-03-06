/* EchoClient.java */

import java.net.*;
import java.io.*;

public class EchoClient
{
	Socket sock = null;
	InputStream in = null;
	OutputStream out = null;

  public EchoClient(String pServerName, int pPort)
  {
  		try
  		{
  		   sock = new Socket(pServerName, pPort);
      	in = sock.getInputStream();
      	out = sock.getOutputStream();
  		}
  		catch (Exception e)
  		{
  			System.out.println(e.toString());
  		}
  }
  
  public void sendString(String pMessage)
  {
  		try
  		{  			
        out.write(pMessage.getBytes());
        out.write('\n');
  		}
  		catch(Exception e)
  		{
  			System.out.println(e.toString());
  		}
  }
}