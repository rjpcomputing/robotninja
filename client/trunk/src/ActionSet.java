import java.util.*;import java.awt.*;import java.awt.event.*;import javax.swing.*;import java.io.*;import java.net.*;

class ActionSet extends AbstractAction 
{
	private String action = "";
	private EchoClient echo;
	private JSlider slider;
	private String temp = "";
	private NinjaGUI ninja;	private EchoClient client;

    public ActionSet(String pAction, JSlider pSlider, NinjaGUI pNinja, EchoClient pClient) 
    {
    	  super();
    	  action = pAction;
    	  client = pClient; 
    	  slider = pSlider;
    	  ninja = pNinja;
    }
    
    public void actionPerformed(ActionEvent e) 
    {
		if(action != ninja.getCurrentCom())
		{
		   String speed = "000000";
			speed = speed + slider.getValue();
			speed = speed.substring(speed.length() - 3, speed.length());
			String tcpCommand = "";
			
			if(action == "forward")
			{
				tcpCommand = "L+"+speed+"R+"+speed;
			}
			else if (action == "backward")
			{
				tcpCommand = "L-"+speed+"R-"+speed;
			}
			else if (action == "left")
			{
				tcpCommand = "L+000R+"+speed;
			}
			else if (action == "right")
			{
				tcpCommand = "L+"+speed+"R+000";
			}
			else if (action == "halt")
			{
				tcpCommand = "L+000R+000";
			}			else if (action == "open")			{				tcpCommand = "O.........";			}			else if (action == "close")			{				tcpCommand = "C.........";			}
			ninja.setCurrentCom(action);
			System.out.println(action);
			System.out.println(tcpCommand);
		    client.sendString(tcpCommand);
		}
    }
}