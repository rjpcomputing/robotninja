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

/*******************************************************************************
 * Handles the TCP/IP communication with the client.                           
 * 
 * @author Jason Pell
 *******************************************************************************/
import java.net.*;
import java.io.*;

public class ClientInterface
{
	private ServerSocket servSock;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private BufferedReader reader;
	private BufferedWriter writer;

	/**************************************************************************
	 * Constructor waits on the specified port for an incoming connection
	 * and sets up client-server connectivity.
	 * @param port the port to accept connections
	 */
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
			writer = new BufferedWriter(new OutputStreamWriter(out));
		}
		catch (IOException e)
		{
			System.out.println("Error creating streams: " + e.getMessage());
		}
	}

	/**************************************************************************
	 * Returns the clients IP address.
	 * @return the client's IP address
	 */
	public String getClientIP()
	{
		InetAddress clientIP;
		clientIP = socket.getInetAddress();
		return clientIP.getHostAddress();
	}

	/**************************************************************************
	 * is the client connected?
	 * @return boolean value that says whether or not the client is connected
	 */
	public boolean connected()
	{
		return socket.isConnected();
	}

	/**************************************************************************
	 * Waits for the client to send the server a command.
	 * @return the command that was recieved by the client
	 */
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
	
	/**************************************************************************
	 * Sends the specified string to the client.
	 * @param a the string to send
	 */
	public void sendString(String a)
	{
		try
		{
			out.write(a.getBytes());
			out.write('\n');
			out.flush();
		}
		catch (IOException e)
		{
			System.out.println("sendString() error" + e.getMessage());
		}
	}

	/**************************************************************************
	 * Sends an acknowledgement to the client.
	 */
	public void sendAck()
	{
		String ack = "ACK";
		try
		{
			out.write(ack.getBytes());
			out.write('\n');
			out.flush();
		}
		catch (IOException e)
		{
			System.out.println("sendAck() error: " + e.getMessage());
		}
	}

	/**************************************************************************
	 * Sends a negative acknowledgement to the client.
	 */
	public void sendNak()
	{
		String nak = "NAK";
		try
		{
			out.write(nak.getBytes());
			out.write('\n');
			out.flush();
		}
		catch (IOException e)
		{
			System.out.println("sendNak() error: " + e.getMessage());
		}

	}

	/**************************************************************************
	 * Disconnects from the client.
	 */
	public void disconnect()
	{
		try 
		{
			socket.close();
			servSock.close();
		}
		catch (IOException ex) { }
	}
}
