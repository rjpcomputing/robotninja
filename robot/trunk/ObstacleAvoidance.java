/******************************************************************************
* Copyright (C) 2008 Ryan Pusztai All rights reserved.
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

import lejos.nxt.*;

/**
 * This class is used to impliment obstacle avoidance.
 * It is meant to be ran as a thread and uses the ultra-sonic sensor
 * for distance detection.
 * @author rpusztai
 */
public class ObstacleAvoidance extends Thread
{
	private UltrasonicSensor sonic = null;
	private int distance = 0;
	private int obstacleAvoidanceDistance = 0;
	
	/**
	 * Sets up the Ultrasonic Sensor on port 4.
	 * Constructor.
	 */
	public ObstacleAvoidance()
	{
		sonic = new UltrasonicSensor( SensorPort.S4 );
		SetObstacleAvoidanceDistance( 20 );
	}
	
	/**
	 * Returns the minimum distance that the robot sees as an obstacle.
	 * @return the obstacle avoidance distance.
	 */
	public int GetObstacleAvoidanceDistance()
	{
		return obstacleAvoidanceDistance;
	}

	/**
	 * Sets the minimum distance that the robot sees as an obstacle.
	 * @param obstacleAvoidanceDistance the obstacle avoidance distance to set.
	 */
	public void SetObstacleAvoidanceDistance( int obstacleAvoidanceDistance )
	{
		this.obstacleAvoidanceDistance = obstacleAvoidanceDistance;
	}

	/**
	 * Returns the distance read from the UltraSonic sensor.
	 * @return the distance
	 */
	public int GetDistance()
	{
		return sonic.getDistance();
	}
	
	/**
	 * Checks if there is an obstacle that is less than obstacleAvoidanceDistance
	 * away from the Ultra Sonic Sensor.
	 * @return True if there is an obstacle, else false.
	 */
	public boolean CheckForObstacle()
	{
		//Debug
		LCD.drawInt( GetDistance(), 0, 0, 3 );
		
		if ( GetDistance() < GetObstacleAvoidanceDistance() )
		{
			return true;
		}
		
		return false;
	}

	/**
	 * This is the main code that is ran as a thread.
	 * This makes the robot backup to get out of the way of the obstacle.
	 */
	public void run()
	{
		boolean isBackingUp = false;
		while( true )
		{
			// Check for obstacle.
			if ( CheckForObstacle() || isBackingUp )
			{
				// Backup to get out of way.
				NinjaBot.GetNavigation().StartMotors( Direction.BACKWARD, 40 );
				isBackingUp = true;

				//Check again for an obstacle and if there is not one stop.
				if ( !CheckForObstacle() )
				{
					NinjaBot.GetNavigation().StopAllMotors();
					isBackingUp = false;
				}
			}
		}
	}
}
