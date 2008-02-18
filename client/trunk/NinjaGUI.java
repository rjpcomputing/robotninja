/* 
 * We just wanted 4 buttons so far? It's not beautiful, but after all I'm just a javva-noob
 * 
 * ToDo:
 * layout and icons
 * TCP/IP-socket
 * maybe use inner classes for actionhandling
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.BorderLayout.*;
import javax.swing.*;
import javax.swing.BoxLayout.*;
import java.io.*;import java.net.*;

public class NinjaGUI implements ActionListener 
{
	private JButton moveForward, moveBack, moveLeft, moveRight;
	private String serverName;
	private Socket sktToServer;
	private int serverPort;
	private Container content;
	
	// Constructor
	NinjaGUI() 
	{
		init();
	}
	
	// Main initialization method
	protected void init() 
	{   
	   JFrame frame = new JFrame("NinjaBot"); // getName() ?
   	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
   	frame.setSize(800,600);
   	frame.setResizable(false);
   	content = frame.getContentPane();
   	content.setLayout(null);
    	frame.addWindowListener(new WindowAdapter() 
    	{
    		public void windowClosing(WindowEvent e) 
    		{
    			System.exit(0);
    		}
   	});
   	
   	try
   	{
			serverName = "";
			serverPort = 0;
			sktToServer = new Socket(serverName, serverPort);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
    
    	JPanel video = new JPanel();
    	video.setBounds(150, 100, 600, 480);
    	video.setBorder(BorderFactory.createMatteBorder(5,5,5,5,Color.BLACK));
    	JLabel videoTemp = new JLabel("VIDEO GOES HERE");
    	video.add(videoTemp);
    	content.add(video);
    	content.add(buildGUI());
    	//frame.pack();
    	frame.setVisible(true);
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
		JPanel panel = new JPanel(buttonsLayout);
		panel.add(moveForward, BorderLayout.NORTH);
		panel.add(moveBack, BorderLayout.SOUTH);
		panel.add(moveLeft, BorderLayout.WEST);
		panel.add(moveRight, BorderLayout.EAST);
		panel.setBounds(0,0,150,100);
		
		moveForward.addActionListener(this);
		moveForward.setActionCommand("L+100R+100");
		moveBack.addActionListener(this);
		moveBack.setActionCommand("L-100R-100");
		moveLeft.addActionListener(this);
		moveLeft.setActionCommand("L+000R+100");
		moveRight.addActionListener(this);
		moveRight.setActionCommand("L+100R+000");
		
		return panel;
	}

	//  When a button is pressed, send the appropriate command string to the server
	public void actionPerformed(ActionEvent ae) 
	{
		String action = ae.getActionCommand();
		try
		{
			OutputStream out = sktToServer.getOutputStream();
			byte b[]= action.getBytes();
			out.write(b);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		//System.out.println(action);
	}
	
	// Main method to call the constructor
	public static void main(String[] args)
	{
		System.out.println("Go Ninja, Go Ninja, Go!");
		new NinjaGUI();
	}
}