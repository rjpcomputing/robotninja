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


public class NinjaServer {
	
	public static boolean ROBOTON;
	public static boolean CLIENTON;
	
	public NinjaServer()
	{
		
	}
	
	public static void main(String[] args)
	{
		RobotInterface robot = null;
		ClientInterface client = null;
		VideoStreaming streaming = null;
		ROBOTON = false;
		CLIENTON = true;

		if (ROBOTON)
		{
			robot = new RobotInterface();
		}
		
		if (CLIENTON)
		{
			client = new ClientInterface();
		}

		if (client.connected())
		{
			//System.out.println("Connected to client.  Attempting to stream...");
			streaming = new VideoStreaming(client.getClientIP(), "9000");
		}
		//ImageDetection detection = new ImageDetection();

		//detection.run();
		streaming.run();
		
		System.out.println("We should be streaming now...");
		
		while (true)
		{
			String command;
			
			if (CLIENTON)
			{
				command = client.receiveCommand();
				System.out.println(command);
		
				if (ROBOTON)
				{
				   robot.sendCommand(command);
				}
				
				if (command.contains("X"))
				{
					break;
				}
				
				if (ROBOTON)
				{
					if (robot.receiveStatus())
					{
						//client.sendAck();
						System.out.println("Success!");
					}
					else
					{
						System.out.println("Failure.");
						//client.sendNak();
					}
				}
			}
		}
		streaming.stopStreaming();
		
	}
}
