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

/******************************************************************************
 * Thread that handles the video streaming to the client and sets up the image 
 * detection for keeping track of the score.
 * 
 * @author Jason Pell
 ******************************************************************************/

import javax.media.*;

public class VideoStreaming extends Thread {
	private boolean readyToStream;
	private VideoTransmit vt;
	private String ipAddress;
	private String port;
	private ImageDetection detection;
	private Player player;
	private String videoConnectionString;
	
	/**************************************************************************
	 * Constructor gets the appropriate values filled before the thread starts
	 * @param pIPAddress the IP address to stream to
	 * @param pPort the first (even-numbered) port to stream to
	 * @param pVideoConnectionString the video's connection string
	 * @param pImageDetection the image detection object
	 */
	public VideoStreaming(String pIPAddress, String pPort, String pVideoConnectionString, ImageDetection pImageDetection)
	{
		readyToStream = false;
		ipAddress = pIPAddress;
		port = pPort;
		detection = pImageDetection;
		videoConnectionString = pVideoConnectionString;
		detection = pImageDetection;
	}
	
	/**************************************************************************
	 * Sets up and starts the VideoTransmit class provided by Sun, then starts
	 * the image detection on another thread.
	 */
	public void run()                       
	{
		String errors;
		vt = new VideoTransmit(new MediaLocator(videoConnectionString), ipAddress, port);
		readyToStream = true;

		errors = vt.start();

		if (errors != null)
		{
			System.out.println("Error: " + errors);
			System.exit(0);
		}
		
		try
		{
			player = Manager.createPlayer(vt.getClonedDataSource());
			player.start();
			detection.setPriority(3); // setting image detection priority to 3 out of 10
			detection.setPlayer(player);
			detection.start();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		

		// continue streaming until we receive signal to stop
		while(true)
		{
			try 
			{
				Thread.currentThread().sleep(100);
			} 
			catch (InterruptedException ie) { }

			if(!readyToStream)
			{
				vt.stop();
				break;
			}
		}
	}

	/**************************************************************************
	 * Signals both the image detection and the video streaming threads to
	 * quit running by raising flags for the threads to detect.
	 */
	public void stopStreaming()
	{
		if (detection != null)
		{
			detection.stopRunning();
		}
		readyToStream = false;
	}
}
