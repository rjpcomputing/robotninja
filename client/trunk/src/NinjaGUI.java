/******************************************************************************
 * Copyright (C) 2008 Adam Parker. All rights reserved.
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
	private JLabel lblServer, lblPort, lblVidPort, lblPower, lblScore, lblTemp;
	private JPanel video;
	private JSlider slider;
	private JTextArea txtInstructions;
	private JTextField jtxServer, jtxPort, jtxVidPort, jtxScore;
	private MediaPlayer player;
	private String serverName, message;
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
					if(client.isClosed() == false)
					{
						try
						{
							client.sendString("X.........");
						}
						catch(Exception ex)
						{
							System.out.println(ex.toString());
						}
					}
				}
				if(player != null)
				{
					player.stop();
					player.close();
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

		lblScore = new JLabel("Score");
		lblScore.setBounds(250, 110, 100, 25);
		lblScore.setForeground(Color.WHITE);
		
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
		
		jtxScore = new JTextField("0");
		jtxScore.setBounds(325, 110, 150, 25);
		jtxScore.setForeground(Color.RED);
		jtxScore.setEditable(false);
		
		btnConnect = new JButton("Connect");
		btnConnect.setBounds(500, 5, 150, 25);
		btnConnect.addActionListener(this);
		btnConnect.setActionCommand("CONNECTION");
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBounds(500, 40, 150, 25);
		btnDisconnect.addActionListener(this);
		btnDisconnect.setActionCommand("X.........");	
		btnDisconnect.setEnabled(false);

		lblTemp = new JLabel("Not connected.");
		lblTemp.setBounds(25, 625, 200, 25);
		lblTemp.setForeground(Color.WHITE);

		video = new JPanel();
		
		content = getContentPane();
		content.setLayout(null);

		content.add(lblTemp);
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
		content.add(jtxScore);
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
	
	public void setMessage(String pMessage)
	{
		message = pMessage;
	}
	
	public String getMessage()
	{
		return message;
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
			client = new EchoClient(tempServer, portNum);
			try
			{
				client.sendString(action);
				message = client.receiveString();
			}
			catch(Exception e)
			{
				updateGUI(e.toString(), null);
			}

			// set actions for key depresses
			forward = new ActionSet("forward", slider, this, client);
			backward = new ActionSet("backward", slider, this, client);
			left = new ActionSet("left", slider, this, client);
			right = new ActionSet("right", slider, this, client);
			halt = new ActionSet("halt", slider, this, client);
			open = new ActionSet("open", slider, this, client);
			close = new ActionSet("close", slider, this, client);

			// maps the action map to keyboard input
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"),"w_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"),"s_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"),"a_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"),"d_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"),"e_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("O"),"o_pressed");
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"),"p_pressed");

			// maps actions to keyboard input
			video.getActionMap().put("w_pressed", forward);
			video.getActionMap().put("s_pressed", backward);
			video.getActionMap().put("a_pressed", left);
			video.getActionMap().put("d_pressed", right);
			video.getActionMap().put("e_pressed", halt);
			video.getActionMap().put("o_pressed", open);
			video.getActionMap().put("p_pressed", close);

			updateGUI("Connecting...", null);
			
			String tempVidPort = jtxVidPort.getText().trim();

			if(player != null)
			{
				content.remove(player);
			}
			media = new GetVideo(tempServer, tempVidPort);
			try
			{
	    		player = media.getGUI();
	    	}
	    	catch(Exception e)
    		{
				updateGUI(e.toString(), null);
			}
			player.setBounds(25, 150, 640, 480);			
			player.start();
			content.add(player);
			Graphics g = getGraphics();
			update(g);
			
			btnDisconnect.setEnabled(true);
			btnConnect.setEnabled(false);
			
			updateGUI("Connected!", player);

			slider.requestFocus();
		}
		
		if(action == "X.........")
		{
			try
			{
				client.sendString(action);
				message = client.receiveString();
			}
			catch(Exception e)
			{
				updateGUI(e.toString(), null);
			}
			player.stop();
			player.close();
			
			btnDisconnect.setEnabled(false);
			btnConnect.setEnabled(true);
		
			updateGUI("Disconnected.", null);

			video.getActionMap().clear();
			video.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
			forward = null;
			backward = null;
			left = null;
			right = null;
			halt = null;
			open = null;
			close = null;
			try
			{
				client.close();
			}
			catch(Exception e)
			{
				updateGUI(e.toString(), null);
			}
		}
	}
	
	public void updateGUI(String  pMessage, MediaPlayer pPlayer)
	{
		Graphics g = content.getGraphics();
		content.remove(lblTemp);
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
		content.remove(jtxScore);
		content.setBackground(null);
		/*if(pPlayer != null)
		{
			content.remove(pPlayer);
		}*/
		update(g);

		lblTemp = null;
		lblTemp = new JLabel(pMessage);
		lblTemp.setBounds(25, 625, 600, 25);
		lblTemp.setForeground(Color.WHITE);

		content.add(lblTemp);
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
		content.add(jtxScore);
		content.setBackground(Color.BLACK);
		/*if(pPlayer != null)
		{
			content.add(pPlayer);
		} */
		update(g);
	}
	
	// Main method to call the constructor
	public static void main(String[] args)
	{
		System.out.println("Go Ninja, Go Ninja, Go!");
		new NinjaGUI(args);
	}
}
