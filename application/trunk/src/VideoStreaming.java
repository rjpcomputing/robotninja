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
import javax.media.*;
import java.io.*;

public class VideoStreaming extends Thread {
	private boolean readyToStream;
	private VideoTransmit vt;
	private String ipAddress;
	private String port;

	public VideoStreaming(String ipAddress, String port)
	{
		readyToStream = false;
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public void run()                       
	{
		String errors;
		vt = new VideoTransmit(new MediaLocator("vfw:Microsoft WDM Image Capture (Win32):0"), ipAddress, port);
		readyToStream = true;

		errors = vt.start();

		if (errors != null)
		{
			System.out.println("Error: " + errors);
			System.exit(0);
		}

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

	public void stopStreaming()
	{
		readyToStream = false;
	}
}
