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
 * The main class to the NinjaServer project.  It sends video feed to a client
 * that sends it commands on where to send a robot.  It is designed to be cross
 * platform and persist when the client disconnects and reconnects.  Uses image
 * detection to calculate the score of a simple ball game that requires two
 * balls and one larger, circular goal.
 * 
 * @author Jason Pell
 *******************************************************************************/

public class NinjaServer {

	private RobotInterface robot;
	private ClientInterface client;
	private VideoStreaming streaming;
	private ImageDetection detection;

	/**************************************************************************
	 * Constructor that takes command line parameters, connects to the robot,
	 * then connects to the server.
	 * 
	 * @param tcpPort the port the client should connect to for commands
	 * @param rtpPort the beginning port (even-numbered) for video streaming
	 * @param robotMac the robot's MAC address
	 * @param videoConnectionString the video connection string
	 */
	public NinjaServer(String tcpPort, String rtpPort, String robotMac, String videoConnectionString)
	{
		robot = new RobotInterface(robotMac);

		while (true)
		{
			client = new ClientInterface(tcpPort);

			detection = new ImageDetection();
			
			streaming = new VideoStreaming(client.getClientIP(), rtpPort, videoConnectionString, detection);

			streaming.start();

			run();
		}
	}

	/**************************************************************************
	 * Handles main communication between the client and the robot.
	 */
	private void run()
	{
		while (true)
		{
			String command;

			command = client.receiveCommand();

			if (command.contains("X"))
			{
				break;
			}

			if (command.length() == 10)
			{
				robot.sendCommand(command);

				if (robot.receiveStatus())
				{
					client.sendAck();
				}
				else
				{
					client.sendNak();
				}
			}
			else if (command.length() < 10)
			{
				if (command.equals("score?"))
				{
					if (detection != null)
					{
						int score = detection.getScore();
						client.sendString(String.valueOf(score));
					}
					else
					{
						client.sendString("0");
					}
				}
			}
			else
			{
				System.out.println("Invalid command: " + command);
			}

		}
		streaming.stopStreaming();
		client.disconnect();
	}

	/**************************************************************************
	 * Main method creates a NinjaServer instance.
	 * @param args tcpPort rtpPort robotMAC videoConnectionString
	 */
	public static void main(String[] args)
	{
		NinjaServer server;

		if (args.length == 3)
		{
			server = new NinjaServer(args[0], args[1], args[2], "vfw:Microsoft WDM Image Capture (Win32):0");
		}
		else if (args.length == 4)
		{
			server = new NinjaServer(args[0], args[1], args[2], args[3]);
		}
		else
		{
			System.out.println("Usage: java NinjaServer <tcpPort> <rtpPort> <robotmac> (videoConnectionString)");
			System.exit(1);
		}
	}
}
