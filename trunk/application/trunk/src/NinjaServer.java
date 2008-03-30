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

	public NinjaServer()
	{

	}

	public static void main(String[] args)
	{
		if (args.length != 3 && args.length != 4)
		{
			System.out.println("Usage: java NinjaServer <tcpPort> <rtpPort> <robotmac> (videoConnectionString)");
			System.exit(1);
		}

		RobotInterface robot = null;
		ClientInterface client = null;
		VideoStreaming streaming = null;

		robot = new RobotInterface(args[2]);

		while (true)
		{
			client = new ClientInterface(args[0]);

			if (client.connected() && args.length == 3)
			{
				streaming = new VideoStreaming(client.getClientIP(), args[1]);
			}
			else if (client.connected() && args.length == 4)
			{
				streaming = new VideoStreaming(client.getClientIP(), args[3]);
			}
			//ImageDetection detection = new ImageDetection();

			//detection.start();
			streaming.start();

			while (true)
			{
				String command;

				command = client.receiveCommand();

				if (command.contains("X"))
				{
					break;
				}

				robot.sendCommand(command);

				if (robot.receiveStatus())
				{
					//client.sendAck();
				}
				else
				{
					//client.sendNak();
				}
			}
			streaming.stopStreaming();
			client.disconnect();
		}
	}
}
