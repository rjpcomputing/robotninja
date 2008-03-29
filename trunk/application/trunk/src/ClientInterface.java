 /******************************************************************************
* Copyright (C) 2008 Jason Pell. All rights reserved.
*
* Permission is hereby granted, free of charge, to any person obtaining
* a copy of this software and associated documentation files (the
* "Software"), to deal in the Software without restriction, including
* without limitation the rights to use, copy, modify, merge, publish,
* distribute, sublicense, and/or sell copies of the Software, and to
* permit persons to whom the Software is furnished to do so, subject to
* the following conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
******************************************************************************/
import java.lang.*;
import java.net.*;
import java.io.*;

public class ClientInterface
{
	ServerSocket servSock;
	Socket socket;
	InputStream in;
	OutputStream out;
	BufferedReader reader;
	
	public ClientInterface(String port)
	{
		try
		{
			servSock = new ServerSocket(Integer.parseInt(port));
			socket = servSock.accept();
		}
		catch (IOException e)
		{
			System.out.println("Error establishing connection: " + e.getMessage());
		}
		
		try
		{
			in = socket.getInputStream();
	    	out = socket.getOutputStream();
	    	reader = new BufferedReader(new InputStreamReader(in));
		}
		catch (IOException e)
		{
			System.out.println("Error creating streams: " + e.getMessage());
		}
	}
	
	public String getClientIP()
	{
		InetAddress clientIP;
		clientIP = socket.getInetAddress();
		System.out.println("Connected to: " + clientIP.getHostAddress());
		return clientIP.getHostAddress();
	}
	
    public boolean connected()
    {
    	return socket.isConnected();
    }
    
    public String receiveCommand()
    {
    	String command;
    	
    	try
    	{
    		command = reader.readLine();
    	}
    	catch (IOException e)
    	{
    		System.out.println("receiveCommand() error: " + e.getMessage());
    		command = "";
    	}
    	
    	return command;
    }
    
    public void sendAck()
    {
    	try
    	{
    		out.write(1);
    	}
    	catch (IOException e)
    	{
    		System.out.println("sendAck() error: " + e.getMessage());
    	}
    }
    
    public void sendNak()
    {
    	try
    	{
    		out.write(0);
    	}
    	catch (IOException e)
    	{
    		System.out.println("sendNak() error: " + e.getMessage());
    	}

    }
}
