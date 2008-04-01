/******************************************************************************
 * Copyright (C) 2008 Adam Parker. All rights reserved.
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
