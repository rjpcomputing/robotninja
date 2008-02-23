
public class NinjaServer {
	public NinjaServer()
	{
		
	}
	
	public static void main(String[] args)
	{
		RobotInterface robot = new RobotInterface();
		ClientInterface client = new ClientInterface();
		//ImageDetection detection = new ImageDetection();
		//VideoStreaming streaming = new VideoStreaming();
		
		//robot.run();
		//client.run();
		//detection.run();
		//streaming.run();
		
		while (true)
		{
			String command;
			command = client.receiveCommand();
			System.out.println(command);
		
			robot.sendCommand(command);
			
			if (command.contains("x"))
			{
				break;
			}
			if (robot.receiveStatus())
			{
				//client.sendAck();
				System.out.println("Success!");
			}
			else
			{
				System.out.println("Failure.");
				//client.sendNak();
			}
		}
		
	}
}
