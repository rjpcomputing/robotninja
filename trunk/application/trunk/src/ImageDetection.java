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
import javax.media.*;
import javax.media.bean.playerbean.MediaPlayer;
import javax.media.control.*;
import javax.media.protocol.*;
import javax.media.util.*;
import javax.media.format.*;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.*;
import java.awt.image.*;

import javax.imageio.*;

import java.awt.*;

public class ImageDetection extends Thread {
	private CannyEdgeDetector edgeDetector;
	private CircleDetection goalDetector;
	private CircleDetection ballDetector;
	private FrameGrabbingControl frameGrabber;
	private Player player;
	private CartesianCoordinates goal;
	private CartesianCoordinates[] balls;
	private boolean timeToStop;
	
	public ImageDetection(Player pPlayer)
	{
		player = pPlayer;
		goal = null;
		balls = new CartesianCoordinates[2];
		timeToStop = false;
	}
	
    public void run()                       
    {
    	// wait for web cam to warm up
		try 
		{
			Thread.currentThread().sleep(5000);
		} 
		catch (InterruptedException ie) { }
    	player.start();
    	
    	frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
    	
		while(true)
		{
			Buffer buf;
			BufferedImage buffImg;
			Image img = null;
			BufferedImage edges;
			
			try 
			{
				Thread.currentThread().sleep(1000);
			} 
			catch (InterruptedException ie) { }

			if (timeToStop)
			{
				break;
			}
			
			try
			{
				// pull in the image
				buf = frameGrabber.grabFrame();
				img = (new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
				buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				//Graphics2D g = buffImg.createGraphics();
				//g.drawImage(img, null, null);
				
				System.out.println("Image pulled in!");
				
				// use the canny detection algorithm
				edgeDetector.setSourceImage(buffImg);
				edgeDetector.process();
				edges = edgeDetector.getEdgesImage();
				
				System.out.println("Used canny detection algorithm!");
				
				// figure out the location of the goal
				goalDetector = new CircleDetection(edges, 20, 45, 1);
				goal = goalDetector.getAllCoords().get(0);
				System.out.println("Goal Location: (" + goal.getX() + ","+ goal.getY() + ")");
				
				System.out.println("Got goal location!");
				
				// figure out the location of the balls
				ballDetector = new CircleDetection(edges, 4, 8, 2);
				balls[0] = ballDetector.getAllCoords().get(0);
				balls[1] = ballDetector.getAllCoords().get(1);
				System.out.println("Ball 1 Location: (" + balls[0].getX() + ","+ balls[0].getY() + ")");
				System.out.println("Ball 2 Location: (" + balls[1].getX() + ","+ balls[1].getY() + ")");
				
				System.out.println("It worked!");
			}
			catch (Exception e)
			{  
				//System.out.println("Crap.");
			}
		}
    }
    
	public void stopRunning()
	{
		timeToStop = true;
	}
}
