/******************************************************************************
* Copyright (C) 2008 Ryan Pusztai, Adam Parker All rights reserved.
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
 * This class will hold details about the commands sent
 * over Bluetooth.
 * @author rpusztai
 * @author Adam Parker
 */
public class Command extends Exception
{
	private Motor motor;
	private int motorPower;
	private Direction motorDirection;

	/**
	 * Gets the commands motor.
	 * @return The current motor.
	 */
	public Motor GetMotor()
	{
		return motor;
	}

	/**
	 * Gets the commands motor power.
	 * @return The current motor power.
	 */
	public int GetPower()
	{
		return motorPower;
	}

	/**
	 * Gets the commands motor direction.
	 * @return The current motor direction.
	 */
	public Direction GetDirection()
	{
		return motorDirection;
	}

	public void SetMotor(char pMotor) throws Exception
	{
		if(pMotor == 'L')
		{
			motor = Motor.C;
		}
		else if (pMotor == 'R')
		{
			motor = Motor.B;
		}
		else if (pMotor == 'C')
		{
			motor = Motor.A;
		}
		else
		{
			throw new Exception( "Unsupported motor set" );
		}
	}

	public void SetPower(int pPower)
	{
		motorPower = pPower;
	}

	public void SetDirection(char pDirection)
	{
		if(pDirection == '+')
		{
			motorDirection = Direction.FORWARD;
		}
		else
		{
			motorDirection = Direction.BACKWARD;
		}
	}
}

