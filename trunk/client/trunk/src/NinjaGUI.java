
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
	private ActionSet forward, backward, left, right, halt, open, close;
	private Container content;
	private int serverPort;
	private GetVideo media;
	private JButton btnConnect, btnDisconnect;
	private JLabel lblServer, lblPort, lblVidPort, lblPower, lblScore;
	private JPanel video;
	private JSlider slider;
	private JTextArea txtInstructions;
	private JTextField jtxServer, jtxPort, jtxVidPort, jtxTeamOne, jtxTeamTwo;
	private MediaPlayer player;
	private String serverName;
	private Socket sktToServer;
	public String command;
	public EchoClient client;
	
	// Constructor
	NinjaGUI(String[] pArgs) 
	{
		super("NinjaBot");
		init(pArgs);
	}
	
	// Main initialization method
	protected void init(String[] pArgs) 
	{   
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(700,700);
		setResizable(false);
		
		addWindowListener(new WindowAdapter() 
    	{
    		public void windowClosing(WindowEvent e) 
    		{
				if(client != null)
				{
					client.sendString("X.........");
				}
				if(player != null)
				{
					player.stop();
				}
    			System.exit(0);
    		}
		});
		
		lblPower = new JLabel("<html>P<br/>o<br/>w<br/>e<br/>r<br/></html>");
		lblPower.setBounds(35,5,10,125);
		lblPower.setForeground(Color.WHITE);
		
		slider = new JSlider(JSlider.VERTICAL);
		slider.setBounds(50, 5, 50, 125);
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
		
		txtInstructions = new JTextArea("W - Forward\nA - Left\nS - Backward\nD - Right\nE - Stop\nO - Open Claw\nP - Close Claw");
		txtInstructions.setBounds(137, 5, 100, 125);
		txtInstructions.setForeground(Color.WHITE);
		txtInstructions.setBackground(Color.BLACK);
		txtInstructions.setEditable(false);		
		
		lblServer = new JLabel("Server:");
		lblServer.setBounds(250, 5, 75, 25);
		lblServer.setForeground(Color.WHITE);
		
		lblPort = new JLabel("Port:");
		lblPort.setBounds(250, 40, 75, 25);
		lblPort.setForeground(Color.WHITE);
		
		lblVidPort = new JLabel("Video Port:");
		lblVidPort.setBounds(250, 75, 75, 25);
		lblVidPort.setForeground(Color.WHITE);

		jtxServer = new JTextField();
		jtxServer.setBounds(325, 5, 150, 25);
		if(pArgs.length > 0)
		{
			jtxServer.setText(pArgs[0]);
		}
		
		jtxPort = new JTextField();
		jtxPort.setBounds(325, 40, 150, 25);
		if(pArgs.length > 1)
		{
			jtxPort.setText(pArgs[1]);
		}
		
		jtxVidPort = new JTextField();
		jtxVidPort.setBounds(325, 75, 150, 25);
		if(pArgs.length > 2)
		{
			jtxVidPort.setText(pArgs[2]);
		}
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(500, 5, 150, 25);
		btnConnect.addActionListener(this);
		btnConnect.setActionCommand("CONNECTION");
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBounds(500, 40, 150, 25);
		btnDisconnect.addActionListener(this);
		btnDisconnect.setActionCommand("X.........");	
		btnDisconnect.setEnabled(false);
		
		lblScore = new JLabel("Score");
		lblScore.setBounds(550, 75, 100, 25);
		lblScore.setForeground(Color.WHITE);
		
		jtxTeamOne = new JTextField("0");
		jtxTeamOne.setBounds(500, 100, 50, 25);
		jtxTeamOne.setForeground(Color.RED);
		jtxTeamOne.setEditable(false);
		
		jtxTeamTwo = new JTextField("0");
		jtxTeamTwo.setBounds(575, 100, 50, 25);
		jtxTeamTwo.setForeground(Color.BLUE);
		jtxTeamTwo.setEditable(false);

		video = new JPanel();
		
		content = getContentPane();
		content.setLayout(null);
		
		content.add(video);
		content.add(lblPower);
		content.add(slider);
		content.add(txtInstructions);
		content.add(lblServer);
		content.add(lblPort);
		content.add(lblVidPort);
		content.add(jtxServer);
		content.add(jtxPort);
		content.add(jtxVidPort);
		content.add(btnConnect);
		content.add(btnDisconnect);
		content.add(lblScore);
		content.add(jtxTeamOne);
		content.add(jtxTeamTwo);
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
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("O"),"o_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"),"p_pressed");

			video.getActionMap().put("w_pressed", forward);
			video.getActionMap().put("s_pressed", backward);
			video.getActionMap().put("a_pressed", left);
			video.getActionMap().put("d_pressed", right);
			video.getActionMap().put("e_pressed", halt);
			video.getActionMap().put("o_pressed", open);
			video.getActionMap().put("p_pressed", close);
			
			JLabel temp = new JLabel("Connecting...");
			temp.setBounds(25, 670, 200, 25);
			content.add(temp);
			updateGUI();
			
			try
			{
				Thread.sleep(5000);	
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
			String tempVidPort = jtxVidPort.getText().trim();
			media = new GetVideo(tempServer, tempVidPort);
	    	player = media.getGUI();
			player.setBounds(25, 150, 640, 480);
			player.start();
			
			btnDisconnect.setEnabled(true);
			btnConnect.setEnabled(false);
			
			Graphics g = content.getGraphics();
			content.remove(temp);
			content.remove(lblPower);
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
			content.remove(txtInstructions);
			content.remove(lblScore);
			content.remove(jtxTeamOne);
			content.remove(jtxTeamTwo);
			content.setBackground(null);
			update(g);
			
			temp = null;
			temp = new JLabel("Connected!");
			temp.setBounds(25, 670, 200, 25);
		
			content.add(temp);
			content.add(lblPower);
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
			content.add(txtInstructions);
			content.add(lblScore);
			content.add(jtxTeamOne);
			content.add(jtxTeamTwo);
			content.setBackground(Color.BLACK);
			content.add(player);
			update(g);
		}
		//System.out.println(action);
		if(action == "X.........")
		{
			player.stop();
			
			btnDisconnect.setEnabled(false);
			btnConnect.setEnabled(true);
			
			JLabel temp = new JLabel("Disconnected.");
			temp.setBounds(25, 670, 200, 25);
			
			Graphics g = content.getGraphics();
			content.remove(lblPower);
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
			content.remove(txtInstructions);
			content.remove(player);
			content.remove(lblScore);
			content.remove(jtxTeamOne);
			content.remove(jtxTeamTwo);
			content.setBackground(null);
			update(g);
			
			media = null;
			player = null;
			client = null;
			forward = null;
			backward = null;
			left = null;
			right = null;
			halt = null;
			open = null;
			close = null;
			
			content.add(temp);
			content.add(lblPower);
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
			content.add(txtInstructions);
			content.add(lblScore);
			content.add(jtxTeamOne);
			content.add(jtxTeamTwo);
			content.setBackground(Color.BLACK);
			update(g);
		}
		client.sendString(action);
	}
	
	public void updateGUI()
	{
		Graphics g = content.getGraphics();
		content.remove(lblPower);
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
		content.remove(txtInstructions);
		content.remove(player);
		content.remove(lblScore);
		content.remove(jtxTeamOne);
		content.remove(jtxTeamTwo);
		content.setBackground(null);
		update(g);
		
		content.add(lblPower);
		content.add(slider);
	   content.add(video);
	   content.add(btnDisconnect);
		ontent.add(btnConnect);
		content.add(jtxServer);
		content.add(jtxPort);
		content.add(jtxVidPort);
		content.add(lblServer);
		content.add(lblPort);
		content.add(lblVidPort);
		content.add(txtInstructions);
		content.add(player);
		content.add(lblScore);
		content.add(jtxTeamOne);
		content.add(jtxTeamTwo);
		content.setBackground(Color.BLACK);
		update(g);
	}
	// Main method to call the constructor
	public static void main(String[] args)
	{
		System.out.println("Go Ninja, Go Ninja, Go!");
		new NinjaGUI(args);
	}
}