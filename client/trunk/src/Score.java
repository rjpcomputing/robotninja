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

 
 public class Score extends Thread
{
	NinjaGUI ninja;
	EchoClient client;
	String message;
	boolean status;
	
	public Score(NinjaGUI pGUI, EchoClient pClient)
	{
		ninja = pGUI;
		client = pClient;	
		message = "";
		status = false;
	}
	
	public boolean getStatus()
	{
		return status;
	}
	
	public void setStatus(boolean pStatus)
	{
		status = pStatus;
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				if(status == true)
				{
					return;
				}
				Thread.sleep(1000);
				client.sendString("score?");
				message = client.receiveString();
				ninja.setScore(message);
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}

		}
	}





}