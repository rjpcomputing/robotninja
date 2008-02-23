import lejos.nxt.*;

/**
 * Navigation controls the motor outputs of the robot. The drive motors are
 * assumed to be on ports B and C.
 */
public class Navigation
{
	/**
	 * Constructor that turns on regulated speed for the drive motors (B and C). 
	 */
	public Navigation()
	{
		Motor.B.regulateSpeed( true );
		Motor.C.regulateSpeed( true );
	}
	
	/**
	 * Starts a single motor.
	 * @param m The motor to start.
	 * @param d The direction to turn the motor.
	 * @param power The power of the motor.
	 */
	public void StartMotor( Motor m, Direction d, int power )
	{
	}
	
	/**
	 * Stops all the motors. Includes motors A, B, and C.
	 */
	public void StopAllMotors()
	{
	}
	
	/**
	 * Stops the specifed motor.
	 * @param m The motor to stop.
	 */
	public void StopMotor( Motor m )
	{
	}
	
	/**
	 * Starts both motors in the direction specified with the power specified.
	 * @param d The Direction to turn the motors.
	 * @param power The power to set the motors at. Valid range is 0 - 100.
	 */
	public void StartMotors( Direction d, int power )
	{
		// Right motor is C
		// Left motor is B
		Motor.B.setPower( power );
		Motor.C.setPower( power );
		
		if ( Direction.FORWARD == d )
		{
			Motor.B.forward();
			Motor.C.forward();
		}
		else
		{
			Motor.B.backward();
			Motor.C.backward();
		}
	}
	
	/**
	 * This is a high level function that takes a Command and carries out the
	 * details contained in the Command. This will set both drive motors in
	 * one single method call.
	 * @param cmd The Command to run on the robot.
	 */
	public void Navigate( Command cmd )
	{
	}
}
