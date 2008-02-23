import lejos.nxt.*;

public class ObstacleAvoidance
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
		SetObstacleAvoidanceDistance( 30 );
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
		//LCD.drawInt( GetDistance(), 0, 0, 1 );
		
		if ( GetDistance() < GetObstacleAvoidanceDistance() )
		{
			return true;
		}
		
		return false;
	}
}