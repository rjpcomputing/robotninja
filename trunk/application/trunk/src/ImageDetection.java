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

/*****************************************************************************
 Grabs frames from webcam output and uses canny edge detection along with
 an implementation of a houghs transform for circles to find the locations
 of a circular goal and two balls.
 
 @author Jason Pell
 *****************************************************************************/

import javax.media.*;
import javax.media.control.*;
import javax.media.util.*;
import javax.media.format.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.*;
import java.util.ArrayList;

public class ImageDetection extends Thread {
	private CircleDetection goalDetector;
	private CircleDetection ballDetector;
	private Player player;
	private CartesianCoordinates goal; // stores goal coordinates
	private CartesianCoordinates[] balls; // stores ball coordinates
	private boolean timeToStop; // 
	private ArrayList<Integer> scoreHistory; // normalizes score
	
	public ImageDetection()
	{
		player = null;
		goal = null;
		balls = new CartesianCoordinates[2];
		timeToStop = false;
		scoreHistory = new ArrayList<Integer>();
	}


	/**************************************************************************
	* Constructor that requires a cloned Player.
	* @param pPlayer The video stream of the web cam.
	***************************************************************************/
	public void setPlayer(Player pPlayer)
	{
		player = pPlayer;
	}
	
	/**************************************************************************
	 * Method is called when thread is started and begins image detection.    
	 **************************************************************************/
    public void run()                       
    {
    	// let the web cam warm up for 5 seconds
		try 
		{
			Thread.currentThread().sleep(5000);
		} 
		catch (InterruptedException ie) { }
    	    	
		while(true)
		{
			Buffer buf;
			Image img = null;
			
			if (timeToStop)
			{
				break;
			}
			
			// give the CPU a breather
			try 
			{
				Thread.currentThread().sleep(1000);
			} 
			catch (InterruptedException ie) { }
			
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
				goalDetector = new CircleDetection(edges, 20, 30, 1);
				goal = goalDetector.getAllCoords().get(0);
				
				// figure out the location of the balls
				ballDetector = new CircleDetection(edges, 4, 6, 2);
				balls[0] = ballDetector.getAllCoords().get(0);
				balls[1] = ballDetector.getAllCoords().get(1);
				
				int score = 0;
				
				if (withinRange(getDistance(goal, balls[0]), goal.getR()))
				{
					score++;
				}
				
				if (withinRange(getDistance(goal, balls[1]), goal.getR()))
				{
					score++;
				}
				
				addToScoreHistory(score);
				
				System.out.println("Score: " + getScore());
			}
			catch (Exception e)
			{  
				System.out.println("Image Detection Error: " + e.getMessage());
			}
		}
    }
    
    /**************************************************************************
     * Uses the pythagorean theroem to get the distance between two coords.   
     * @param c1 The first set of cartesian coordinates.
     * @param c2 The second set of cartesian coordinates.
     * @return The resulting distance between the two coordinates.
     **************************************************************************/
    private double getDistance(CartesianCoordinates c1, CartesianCoordinates c2)
    {
    	return Math.sqrt(Math.pow(Math.abs(c1.getX() - c2.getX()), 2) + Math.pow(Math.abs(c1.getY() - c2.getY()), 2));
    }
    
    /**************************************************************************
     * Simply checks if the distance is within the radius.
     * @param distance The distance.
     * @param radius The radius.
     * @return if within range, returns true, otherwise returns false
     **************************************************************************/
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
    
    /**************************************************************************
     * Returns the score which is the mode of the score history.
     * @return the score
     **************************************************************************/
    public int getScore()
    {
    	int[] scoreFrequency = new int[getMaxValueFromArrayList(scoreHistory)+1];
    	int score = 0;
    	int highestFrequency = 0;
    	
    	for (int i = 0; i < scoreHistory.size(); i++)
    	{
    		int val = scoreHistory.get(i);
    		scoreFrequency[val]++;
    	}
    	
    	for (int i = 0; i < scoreFrequency.length; i++)
    	{
    		if (scoreFrequency[i] > highestFrequency)
    		{
    			score = i;
    			highestFrequency = scoreFrequency[i];
    		}
    	}
    	
    	return score;
    }
    
    /**************************************************************************
     * This flag is set outside of the thread to signal to shut down.
     **************************************************************************/
	public void stopRunning()
	{
		timeToStop = true;
	}
	
	/**************************************************************************
	 * Appends the latest score to the score history and bumps off the
	 * earliest record if required.
	 * @param score the latest score to be added.
	 **************************************************************************/
	private void addToScoreHistory(int score)
	{
		Integer newScore = new Integer(score);
		if (scoreHistory.size() <= 3)
		{
			scoreHistory.add(newScore);
		}
		else
		{
			scoreHistory.remove(0);
			scoreHistory.add(newScore);
		}
	}
	
	/**************************************************************************
	 * Goes through an arraylist of integers are returns the maximum value.
	 * @param ar the arraylist of integers
	 * @return the maximum value
	 **************************************************************************/
	private int getMaxValueFromArrayList(ArrayList<Integer> ar)
	{
		int max = 0;
		for (int i = 0; i < ar.size(); i++)
		{
			int val = ar.get(i);
			if (val > max)
			{
				max = val;
			}
		}
		return max;
	}
}
