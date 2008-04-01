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

/******************************************************************************
 * Handles interfacing with the lego NXT robot by using bluetooth.            
 * 
 * @author Jason Pell
 ******************************************************************************/
import java.io.*;
import lejos.pc.comm.*;

public class RobotInterface {
	
	NXTComm nxtComm;
	NXTInfo[] nxtInfo;
	boolean opened;
	InputStream is;
	OutputStream os;
	DataOutputStream dos;
	DataInputStream dis;
	
	/**************************************************************************
	 * Constructor creates the connection to the robot and opens the
	 * appropriate streams.
	 * @param nxtMAC the MAC address of the robot
	 */
	public RobotInterface(String nxtMAC)
	{
		opened = false;
		
		nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		
		nxtInfo = new NXTInfo[1];
			
		nxtInfo[0] = new NXTInfo("NXT", nxtMAC);
		
		System.out.println("Connecting to " + nxtInfo[0].btResourceString);
		
		try {
			opened = nxtComm.open(nxtInfo[0]); 
		} catch (NXTCommException e) {
			System.out.println("Exception from open");
		}
		
		if (!opened) {
			System.out.println("Failed to open " + nxtInfo[0].name);
			System.exit(1);
		}
		
		System.out.println("Connected to " + nxtInfo[0].btResourceString);
		
		is = nxtComm.getInputStream();
		os = nxtComm.getOutputStream();
		
		dos = new DataOutputStream(os);
		dis = new DataInputStream(is);
	}
	    
	/**************************************************************************
	 * Sends the specified command to the robot.
	 * @param comm the command to send
	 */
    public void sendCommand(String comm)
    {
    	try
    	{
    		dos.writeChars(comm);
    		dos.flush();
    		
    	}
    	catch (IOException ioe)
    	{
    		System.out.println("sendCommand() error: " + ioe.getMessage());
    	}
    }
    
    /**************************************************************************
     * Waits for the robot to send its status.
     * @return the status that was received
     */
    public boolean receiveStatus()
    {
    	boolean status;
    	
		try
		{
			status = dis.readBoolean();
		}
		catch(IOException ioe)
		{
			System.out.println("receiveStatus() error: " + ioe.getMessage());
			status = false;
		}
    	
    	return status;
    }
    
    /**************************************************************************
     * Closes the connection with the robot.
     */
    public void close()
    {
		try 
		{
			dis.close();
			dos.close();
			nxtComm.close();
		} 
		catch (IOException ioe) 
		{
			System.out.println("close() error: " + ioe.getMessage());
		}
    }
}
