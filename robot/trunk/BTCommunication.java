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
import lejos.nxt.comm.*;
import java.io.*;

/**
 * Receive data from another NXT, a PC, a phone, 
 * or another bluetooth device.
 * 
 * Waits for a connection, receives an int and returns
 * its negative as a reply, 100 times, and then closes
 * the connection, and waits for a new one.
 * 
 * @author Lawrie Griffiths
 *
 */
public class BTCommunication
{
	private BTConnection m_btConnection = null;
	
	public BTCommunication()
	{
		LCD.drawString( "Waiting...", 0, 0 );
		LCD.refresh();

		m_btConnection = Bluetooth.waitForConnection();

		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		LCD.refresh();	
	}
	
	/**
	 * Reads a string from Bluetooth and parses it into a Command.
	 * @return A new Command that has the details of the Bluetooth
	 * 	command set from the server.
	 */
	public Command[] ReadCommand()
	{
		String receivedMessage = "";
		ObstacleAvoidance objAvoid = new ObstacleAvoidance();

		//00:16:53:04:CE:24
		try
		{
			DataInputStream dis = m_btConnection.openDataInputStream();
			DataOutputStream dos = m_btConnection.openDataOutputStream();
			
			for( int i = 0; i < 10; i++ )
			{
				char c = dis.readChar();
				receivedMessage += c;
			}

			// Display received message.
			LCD.drawString(receivedMessage, 0, 1, false );
			LCD.refresh();

			// Ack
			dos.writeBoolean( !objAvoid.CheckForObstacle() );
			dos.flush();
		}
		catch(IOException ex)
		{
			LCD.drawString("fail",0,0);
			LCD.refresh();
		}
		
		return ParseCommand( receivedMessage );
	}
	
	/**
	 * Parses a string recieved through Bluetooth into a Command class.
	 * @param receivedMsg The string recieved on the Bluetooth connection.
	 * @return A newly created Command with the details on the message
	 * 	received from the server.
	 */
	private Command[] ParseCommand( String receivedMsg )
	{
		char[] temp = receivedMsg.toCharArray();
		//"L+100R+100"
		Command cmdSegmentOne = new Command();
		Command cmdSegmentTwo = new Command();

		// Check for command type.
		if ( temp[0] == 'X' || temp[0] == 'x' )						// Server exit command
		{
			System.exit(0);
		}
		else if ( temp[0] == 'C' || temp[0] == 'c' )				// Claw close command
		{
			try
			{
				
				cmdSegmentOne.SetMotor( 'C' );
				cmdSegmentOne.SetPower( 30 );
				cmdSegmentOne.SetDirection( '-' );

				cmdSegmentTwo.SetMotor( 'C' );
				cmdSegmentTwo.SetPower( 30 );
				cmdSegmentTwo.SetDirection( '-' );
			}
			catch( Exception e )
			{
				LCD.drawString( e.getMessage(), 0, 3, false );
				LCD.refresh();
			}
		}
		else if ( temp[0] == 'O' || temp[0] == 'o' )				// Claw open command
		{
			try
			{
				cmdSegmentOne.SetMotor( 'C' );
				cmdSegmentOne.SetPower( 50 );
				cmdSegmentOne.SetDirection( '+' );

				cmdSegmentTwo.SetMotor( 'C' );
				cmdSegmentTwo.SetPower( 50 );
				cmdSegmentTwo.SetDirection( '+' );
			}
			catch( Exception e )
			{
				LCD.drawString( e.getMessage(), 0, 3, false );
				LCD.refresh();
			}
		}
		else														// All other commands
		{
			try
			{
				cmdSegmentOne.SetMotor( temp[0] );
				cmdSegmentOne.SetPower( charToInt( temp[2], temp[3], temp[4] ) );
				cmdSegmentOne.SetDirection( temp[1] );

				cmdSegmentTwo.SetMotor( temp[5] );
				cmdSegmentTwo.SetPower( charToInt( temp[7], temp[8], temp[9] ) );
				cmdSegmentTwo.SetDirection( temp[6] );
			}
			catch( Exception e )
			{
				LCD.drawString( e.getMessage(), 0, 3, false );
				LCD.refresh();
			}
		}

		Command[] commands = new Command[2];
		commands[0] = cmdSegmentOne;
		commands[1] = cmdSegmentTwo;

		return commands;
	}

	public int charToInt(char pHundreds, char pTens, char pOnes)
	{
		int hundreds = (pHundreds - 48) * 100;
		int tens = (pTens - 48) * 10;
		int ones = pOnes - 48;
		int sum = hundreds + tens + ones;
		
		return sum;
	}
}

