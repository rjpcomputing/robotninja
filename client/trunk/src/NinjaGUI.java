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
import java.awt.Graphics.*;
import java.awt.BorderLayout.*;
import javax.swing.*;
import javax.swing.BoxLayout.*;
import java.io.*;
import java.net.*;
import javax.media.bean.playerbean.*;

public class NinjaGUI extends JFrame implements ActionListener
{
	private JButton btnConnect, btnDisconnect;
	private JTextField jtxServer, jtxPort, jtxVidPort;
	private JSlider slider;
	private String serverName;
	private Socket sktToServer;
	private int serverPort;
	private Container content;
	public String command;
	public EchoClient client;
	private MediaPlayer player;
	private ActionSet forward, backward, left, right, halt, open, close;
	private JPanel video;
	private JTextArea txtInstructions;
	private JLabel lblServer, lblPort, lblVidPort;
	
	// Constructor
	NinjaGUI() 
	{
		super("NinjaBot");
		init();
	}
	
	// Main initialization method
	protected void init() 
	{   
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(700,750);
		setResizable(false);
		
		addWindowListener(new WindowAdapter() 
    	{
    		public void windowClosing(WindowEvent e) 
    		{
				//player.stop();
    			System.exit(0);
    		}
		});
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBounds(525, 80, 100, 25);
		btnDisconnect.addActionListener(this);
		btnDisconnect.setActionCommand("X.........");
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(525, 30, 100, 25);
		btnConnect.addActionListener(this);
		btnConnect.setActionCommand("CONNECTION");
		
		lblServer = new JLabel("Server:");
		lblServer.setBounds(300, 30, 50, 25);
		lblServer.setForeground(Color.WHITE);
		jtxServer = new JTextField();
		jtxServer.setBounds(350, 30, 150, 25);
		
		txtInstructions = new JTextArea("W - Forward\nA - Left\nS - Backward\nD - Right\nE - Stop\nUp - Open Claw\nDown - Close Claw");
		lblInstructions.setBounds(160, 25, 125, 125);
		lblInstructions.setForeground(Color.WHITE);
		lblInstructions.setBackground(Color.BLACK);
		lblInstructions.setEditable(false);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(300, 80, 50, 25);
		lblPort.setForeground(Color.WHITE);
		jtxPort = new JTextField();
		jtxPort.setBounds(350, 80, 150, 25);
		
		lblVidPort = new JLabel("Video Port:");
		lblVidPort.setBounds(300, 120, 50, 25);
		lblVidPort.setForeground(Color.WHITE);
		jtxVidPort = new JTextField();
		jtxVidPort.setBounds(350, 120, 150, 25);
		
		video = new JPanel();
    	
    	slider = new JSlider(JSlider.VERTICAL);
		slider.setBounds(75, 25, 50, 125);
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
		
		content = getContentPane();
		content.setLayout(null);
		content.add(slider);
    	content.add(video);
    	content.add(btnDisconnect);
		content.add(btnConnect);
		content.add(jtxServer);
		content.add(jtxPort);
		content.add(jtxVidPort);
		content.add(lblServer);
		content.add(lblPort);
		content.add(lblVidPort);
		content.add(lblInstructions);
		content.setBackground(Color.BLACK);
    	setVisible(true);
	}
	
	public void setCurrentCom(String pCurrentCom)
	{
			command = pCurrentCom;
	}
	
	public String getCurrentCom()
	{
			return command;
	}


	//  When a button is pressed, send the appropriate command string to the server
	public void actionPerformed(ActionEvent ae) 
	{
		String action = ae.getActionCommand();
		if (action == "CONNECTION")
		{
			String tempServer = jtxServer.getText().trim();
			String tempPort = jtxPort.getText().trim();
			int portNum = Integer.parseInt(tempPort);
			System.out.println(tempServer);
			System.out.println(portNum);
			client = new EchoClient(tempServer, portNum);
			
			forward = new ActionSet("forward", slider, this, client);
			backward = new ActionSet("backward", slider, this, client);
			left = new ActionSet("left", slider, this, client);
			right = new ActionSet("right", slider, this, client);
			halt = new ActionSet("halt", slider, this, client);
			open = new ActionSet("open", slider, this, client);
			close = new ActionSet("close", slider, this, client);
			
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"),"w_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"),"s_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"),"a_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"),"d_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"),"e_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"),"up_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"),"down_pressed");

			video.getActionMap().put("w_pressed", forward);
			video.getActionMap().put("s_pressed", backward);
			video.getActionMap().put("a_pressed", left);
			video.getActionMap().put("d_pressed", right);
			video.getActionMap().put("e_pressed", halt);
			video.getActionMap().put("up_pressed", open);
			video.getActionMap().put("down_pressed", close);
			
			try
			{
				Thread.sleep(5000);	
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
			String tempVidPort = jtxVidPort.getText().trim();
			GetVideo media = new GetVideo(tempServer, tempVidPort);
	    	MediaPlayer player = media.getGUI();
			player.setBounds(25, 175, 640, 480);
			player.start();
			
			Graphics g = content.getGraphics();
			content.remove(slider);
	    	content.remove(video);
	    	content.remove(btnDisconnect);
			content.remove(btnConnect);
			content.remove(jtxServer);
			content.remove(jtxPort);
			content.remove(jtxVidPort);
			content.remove(lblServer);
			content.remove(lblPort);
			content.remove(lblVidPort);
			content.remove(lblInstructions);
			content.setBackground(null);
			update(g);
			content.add(slider);
	    	content.add(video);
	    	content.add(btnDisconnect);
			content.add(btnConnect);
			content.add(jtxServer);
			content.add(jtxPort);
			content.add(jtxVidPort);
			content.add(lblServer);
			content.add(lblPort);
			content.add(lblVidPort);
			content.add(lblInstructions);
			content.setBackground(Color.BLACK);
			content.add(player);
			update(g);
		}
		System.out.println(action);
		client.sendString(action);
	}
	
	// Main method to call the constructor
	public static void main(String[] args)
	{
		System.out.println("Go Ninja, Go Ninja, Go!");
		new NinjaGUI();
	}
}