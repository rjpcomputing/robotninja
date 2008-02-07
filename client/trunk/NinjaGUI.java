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

import javax.swing.*;


public class NinjaGUI implements ActionListener {

	private JButton moveForward, moveBack, moveLeft, moveRight;
	
	NinjaGUI() {
		init();
	}
	
	protected void init() {   
    JFrame frame = new JFrame("NinjaBot"); // getName() ?
    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
    	public void windowClosing(WindowEvent e) {System.exit(0);}
    });
    
    frame.add(buildGUI());
    frame.pack();
    frame.setVisible(true);
	}
	
	private JPanel buildGUI() {
		moveForward = new JButton("Forward");
		moveBack = new JButton("Back");
		moveLeft = new JButton("Left");
		moveRight = new JButton("Right");
		
		JPanel panel = new JPanel();
		panel.add(moveForward);
		panel.add(moveBack);
		panel.add(moveLeft);
		panel.add(moveRight);
		
		moveForward.addActionListener(this);
		moveForward.setActionCommand("forward");
		moveBack.addActionListener(this);
		moveBack.setActionCommand("back");
		moveLeft.addActionListener(this);
		moveLeft.setActionCommand("left");
		moveRight.addActionListener(this);
		moveRight.setActionCommand("right");
		
		return panel;
	}

	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();
		//call a move-method here, something like move(action)?
		System.out.println(action);

	}
	
	
	public static void main(String[] args) {
		System.out.println("Go Ninja Go!");
		new NinjaGUI();
	}
}