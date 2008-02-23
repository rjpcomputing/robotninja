import java.lang.*;
import java.net.*;
import java.io.*;

public class ClientInterface
{
	ServerSocket servSock;
	Socket socket;
	InputStream in;
	OutputStream out;
	BufferedReader reader;
	
	public ClientInterface()
	{
		try
		{
			servSock = new ServerSocket(8080);
			socket = servSock.accept();
		}
		catch (IOException e)
		{
			System.out.println("Error establishing connection: " + e.getMessage());
		}
		
		try
		{
			in = socket.getInputStream();
	    	out = socket.getOutputStream();
	    	reader = new BufferedReader(new InputStreamReader(in));
		}
		catch (IOException e)
		{
			System.out.println("Error creating streams: " + e.getMessage());
		}
	}
	
    public void run()                       
    {              

    }
    
    public String receiveCommand()
    {
    	String command;
    	
    	try
    	{
    		command = reader.readLine();
    	}
    	catch (IOException e)
    	{
    		System.out.println("receiveCommand() error: " + e.getMessage());
    		command = "";
    	}
    	
    	return command;
    }
    
    public void sendAck()
    {
    	try
    	{
    		out.write(1);
    	}
    	catch (IOException e)
    	{
    		System.out.println("sendAck() error: " + e.getMessage());
    	}
    }
    
    public void sendNak()
    {
    	try
    	{
    		out.write(0);
    	}
    	catch (IOException e)
    	{
    		System.out.println("sendNak() error: " + e.getMessage());
    	}

    }
}
