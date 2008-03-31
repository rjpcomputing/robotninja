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
	private CircleDetection goalDetector;
	private CircleDetection ballDetector;
	private FrameGrabbingControl frameGrabber;
	private Player player;
	private CartesianCoordinates goal;
	private CartesianCoordinates[] balls;
	private boolean timeToStop;
	private int score;
	
	public ImageDetection()
	{
		player = null;
		goal = null;
		balls = new CartesianCoordinates[2];
		timeToStop = false;
		score = 0;
	}
	
	public void setPlayer(Player pPlayer)
	{
		player = pPlayer;
	}
	
    public void run()                       
    {
    	// wait for web cam to warm up
		try 
		{
			Thread.currentThread().sleep(5000);
		} 
		catch (InterruptedException ie) { }
    	    	
		while(true)
		{
			Buffer buf;
			Image img = null;
			
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
				CannyEdgeDetector detector = new CannyEdgeDetector();
				BufferedImage edges;
				FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
				buf = frameGrabber.grabFrame();
				
				img = (new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
				BufferedImage buffImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
				Graphics2D g = buffImg.createGraphics();
				g.drawImage(img, null, null);
								
				// use the canny detection algorithm
				detector.setSourceImage(buffImg);
				detector.process();
				edges = detector.getEdgesImage();
				
				// figure out the location of the goal
				goalDetector = new CircleDetection(edges, 20, 45, 1);
				goal = goalDetector.getAllCoords().get(0);
				
				// figure out the location of the balls
				ballDetector = new CircleDetection(edges, 4, 6, 2);
				balls[0] = ballDetector.getAllCoords().get(0);
				balls[1] = ballDetector.getAllCoords().get(1);
				
				score = 0;
				
				if (withinRange(getDistance(goal, balls[0]), goal.getR()))
				{
					score++;
				}
				
				if (withinRange(getDistance(goal, balls[1]), goal.getR()))
				{
					score++;
				}
				
				System.out.println("Score: " + score);
			}
			catch (Exception e)
			{  
				System.out.println("Image Detection Error: " + e.getMessage());
			}
		}
    }
    
    private double getDistance(CartesianCoordinates c1, CartesianCoordinates c2)
    {
    	return Math.sqrt(Math.pow(Math.abs(c1.getX() - c2.getX()), 2) + Math.pow(Math.abs(c1.getY() - c2.getY()), 2));
    }
    
    private boolean withinRange(double distance, int radius)
    {
    	if (distance > radius)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }
    
    public int getScore()
    {
    	return score;
    }
    
	public void stopRunning()
	{
		timeToStop = true;
	}
}
