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
	/**
	 * Reads a string from Bluetooth and parses it into a Command.
	 * @return A new Command that has the details of the Bluetooth
	 * 	command set from the server.
	 */
	public Command ReadCommand()
	{
		String receivedMessage = "";
		
		return ParseCommand( receivedMessage );
	}
	
	/**
	 * Parses a string recieved through Bluetooth into a Command class.
	 * @param receivedMsg The string recieved on the Bluetooth connection.
	 * @return A newly created Command with the details on the message
	 * 	received from the server.
	 */
	private Command ParseCommand( String receivedMsg )
	{
		return new Command();
	}
	
	/*public static void main(String [] args)  throws Exception 
	{
		String connected = "Connected";
        String waiting = "Waiting...";
        String closing = "Closing...";
        String fail	   = "Failure...";
        
		LCD.drawString(waiting,0,0);
		LCD.refresh();

		BTConnection btc = Bluetooth.waitForConnection();
		
		LCD.clear();
		LCD.drawString(connected,0,0);
		LCD.refresh();	

		DataInputStream dis = btc.openDataInputStream();
		DataOutputStream dos = btc.openDataOutputStream();

		// Loop counter.
		int iter = 0;
		while ( true )
		{
			//00:16:53:04:CE:24
			try
			{
				String msg = "";
				for( int i = 0; i < 10; i++ )
				{
					char c = dis.readChar();
					msg += c;
				}

				if ( msg.charAt( 0 ) == 'x' )
				{
					break;
				}

				// Display received message.
				LCD.drawString( msg, 0, 1, false );
				LCD.refresh();

				// Ack
				dos.writeBoolean( true );
				dos.flush();

				// Display Loop count.
				LCD.drawInt( iter, 0, 0, 3 );
				LCD.refresh();

				// Increment loop count.
				iterReceive++;
			}
			catch(IOException ex)
			{
				LCD.drawString( fail,0,0 );
				LCD.refresh();
				break;
			}

		}
		
		dis.close();
		dos.close();
		Thread.sleep(100); // wait for data to drain
		LCD.clear();
		LCD.drawString(closing,0,0);
		LCD.refresh();
		btc.close();
		LCD.clear();
	}
	*/
}

