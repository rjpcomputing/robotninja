/* 
 * We just wanted 4 buttons so far? It's not beautiful, but after all I'm just a javva-noob
 * 
 * ToDo:
 * layout and icons
 * TCP/IP-socket
 * maybe use inner classes for actionhandling
 */

import java.util.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics.*;
import java.awt.BorderLayout.*;
import javax.swing.*;
import javax.swing.BoxLayout.*;
import java.io.*;
import java.net.*;

public class NinjaGUI extends JFrame implements ActionListener 
{
	private JButton moveForward, moveBack, moveLeft, moveRight, exit, closeClaw, openClaw;
	private JSlider slider;
	private String serverName;
	private Socket sktToServer;
	private int serverPort;
	private Container content;
	private EchoClient echo;
	private JPanel panel, temp;
	
	// Constructor
	NinjaGUI() 
	{
		super("NinjaBot");
		init();
	}
	
	// Main initialization method
	protected void init() 
	{   
	   //super("NinjaBot"); // getName() ?
   	   setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	   setSize(600,650);
	   setResizable(false);
	   content = getContentPane();
	   content.setLayout(null);
    	addWindowListener(new WindowAdapter() 
    	{
    		public void windowClosing(WindowEvent e) 
    		{
    			System.exit(0);
    		}
   	});

		echo = new EchoClient("127.0.0.1", 8080);

		exit = new JButton("Disconnect");
		exit.setBounds(500, 10, 100, 25);
		exit.addActionListener(this);
		exit.setActionCommand("X.........");
		closeClaw = new JButton("Close Claw");
		openClaw = new JButton("Open Claw");
		openClaw.addActionListener(this);
		openClaw.setActionCommand("o.........");
		closeClaw.addActionListener(this);
		closeClaw.setActionCommand("c.........");
		openClaw.setBounds(500, 60, 100, 25);
		closeClaw.setBounds(500, 110, 100, 25);
    	JPanel video = new JPanel();
    	video.setBounds(0, 150, 600, 480);
		
		slider = new JSlider(JSlider.VERTICAL);
		slider.setBounds(25, 25, 50, 100);
		slider.setMajorTickSpacing(25);
		slider.setPaintTicks(true);
		
		//Create the label table
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer(0), new JLabel("000") );
		labelTable.put( new Integer(25), new JLabel("025") );
		labelTable.put( new Integer(50), new JLabel("050") );
		labelTable.put( new Integer(75), new JLabel("075") );
		labelTable.put( new Integer(100), new JLabel("100") );
		slider.setLabelTable( labelTable );

		slider.setPaintLabels(true);
		
    	//video.setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.BLACK));
    	JLabel videoTemp = new JLabel("VIDEO GOES HERE");
    	video.add(videoTemp);
		content.add(slider);
    	content.add(video);
		content.add(openClaw);
		content.add(closeClaw);
    	content.add(exit);
		temp = buildGUI();
    	content.add(temp);
		content.setBackground(Color.BLACK);
    	//frame.pack();
    	setVisible(true);
	}
	
	// Panel to hold the buttons using a BorderLayout
	private JPanel buildGUI() 
	{
		moveForward = new JButton("Forward");
		moveBack = new JButton("Back");
		moveLeft = new JButton("Left");
		moveRight = new JButton("Right");
		
		BorderLayout buttonsLayout = new BorderLayout();
		buttonsLayout.setHgap(10);
		buttonsLayout.setVgap(10);
		
		panel = new JPanel(buttonsLayout);
		panel.setBackground(null);
		panel.setBackground(Color.BLACK);
		panel.add(moveForward, BorderLayout.NORTH);
		panel.add(moveBack, BorderLayout.SOUTH);
		panel.add(moveLeft, BorderLayout.WEST);
		panel.add(moveRight, BorderLayout.EAST);
		panel.setBounds(200,25,150,100);
		
		moveForward.addActionListener(this);
		moveForward.setActionCommand("forward");
		moveBack.addActionListener(this);
		moveBack.setActionCommand("backward");
		moveLeft.addActionListener(this);
		moveLeft.setActionCommand("left");
		moveRight.addActionListener(this);
		moveRight.setActionCommand("right");
		
		return panel;
	}

	//  When a button is pressed, send the appropriate command string to the server
	public void actionPerformed(ActionEvent ae) 
	{
		String speed = "000000";
		speed = speed + slider.getValue();
		speed = speed.substring(speed.length() - 3, speed.length());
		
		String action = ae.getActionCommand();
		
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
		//System.out.println(action);
		echo.sendString(action);
	}
	
	// Main method to call the constructor
	public static void main(String[] args)
	{
		System.out.println("Go Ninja, Go Ninja, Go!");
		new NinjaGUI();
	}
}