/* EchoClient.java */

import java.net.*;
import java.io.*;

public class EchoClient
{
	Socket sock;
	InputStream in;
	OutputStream out;

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

	public void sendString(String pMessage) throws Exception
	{
		out.write(pMessage.getBytes());
		out.write('\n');
		out.flush();
		System.out.println(pMessage);
	}

	public void close() throws Exception
	{
		sock.close();
	}

	public boolean isClosed()
	{
		return sock.isClosed();
	}
}
