import java.util.*;import java.awt.*;import java.awt.event.*;import javax.swing.*;import java.io.*;import java.net.*;

class ActionSet extends AbstractAction 
{
	private String action = "";
	private EchoClient echo;
	private JSlider slider;

    public ActionSet(String pAction, JSlider pSlider) 
    {
    	  super();
    	  action = pAction;
    	  //echo = new EchoClient("127.0.0.1", 8080);
    	  slider = pSlider;
    }
    
    public void actionPerformed(ActionEvent e) 
    {
     	String speed = "000000";
		speed = speed + slider.getValue();
		speed = speed.substring(speed.length() - 3, speed.length());
		
		if(action == "forward")
		{
			action = "L+"+speed+"R+"+speed;
		}
		else if (action == "backward")
		{
			action = "L-"+speed+"R-"+speed;
		}
		else if (action == "left")
		{
			action = "L+000R+"+speed;
		}
		else if (action == "right")
		{
			action = "L+"+speed+"R+000";
		}
		else if (action == "halt")
		{
			action = "L+000R+000";
		}
		System.out.println(action);
		//echo.sendString(action);
    }
}