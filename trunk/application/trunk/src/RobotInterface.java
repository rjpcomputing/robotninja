import java.lang.*;
import javax.bluetooth.*;
import javax.microedition.io.*;
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
	
	public RobotInterface()
	{
		opened = false;
		
		nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		
		nxtInfo = new NXTInfo[1];
			
		nxtInfo[0] = new NXTInfo("NXT","00:16:53:04:CE:24");
		
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
	
    public void run()                       
    {              


    }
    
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
