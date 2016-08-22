import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.io.FileInputStream;
import java.net.*;

import javax.swing.*;

/**
 * @authors Abhiram Kothapalli, Kevin Chang, Rishabh Swarnkar
 * 
 * TRON
 * A Java Applet application of the the retro arcade game TRON.
 * Several functionalities from the original game have not been implemented
 * (speedboost, sound effects)
 * The main purpose of the code is to design and implement a solid networking structure
 * 
 * The game currently supports two modes:
 * -An AI Mode mainly designed to test the front end aspect of the code
 * -A remote mode broken down into advanced setup 
 * (where the player is prompted to fill in all networking metrics)
 * and a default mode(networking metrics are already filled into code)
 * 
 */
public class TronMain extends JPanel{

	/** width and height of the game window */
	static int fWidth = 1200, fHeight = 675;
	/** the local player which inherits from TronPlayer */
	static LocalPlayer localPlayer;
	/**the AI player which inherits from TronPlayer */
	//static AITronPlayer comp; 
	/**the remote player controlled through the network also inheriting from TronPlayer  */
	static RemotePlayer remotePlayer;
	/**the ArrayList of players, to be transversed by various functions*/
	public static ArrayList<TronPlayer> players;

	//DEFAULTS, DESIGNED TO BE MODIFIED
	/**the player ID either 1 or 2*/
	public static int playerNumber = 1;
	/**the local port to recieve remote player input*/
	public static int localPort = 3000;
	/**the remote port to send local player output*/
	public static int remotePort = 3001;
	/**the IP address of the remote player*/
	public static String remoteIPAddress = "10.18.78.37";

	/**sends the data to remote player*/
	public static PlayerDataSender sender;
	/**recieves the data from remote player*/
	public static PlayerDataReceiver reciever;
	
	/**this is the display that draws everything*/
	public static Display display;


	/**
	 * @param args is not used in this code
	 * 
	 * main:
	 * sets up the players, based on input
	 * sets up the network, based on input or defaults
	 * calls setDisplay
	 */
	public static void main (String[] args)
	{

		players = new ArrayList<TronPlayer>();
		int playerOption = 0;
		Scanner scan = new Scanner(System.in);
		
		System.out.print("local play(1) or advanced(2) or defaults(3): ");

		try
		{
			playerOption = scan.nextInt();
		}
		catch(Exception e)
		{

		}

		switch(playerOption)
		{

		case 1:
			System.out.print("how many AI's (0 - 3):");
			
			int numAI = 1;
			
			try
			{
				numAI = scan.nextInt();	
			}
			catch(Exception e)
			{
				System.out.println("Type error going to defaults:");
				printDefaults();
			}
			
			if(numAI > 3)
			{
				numAI = 3;
			}

			spawnOnePlayer(numAI);
			break;
		case 2:
			try
			{
				System.out.print("Player Number (1) (2): ");
				playerNumber = scan.nextInt();
				System.out.print("localPort: ");
				localPort = scan.nextInt();
				System.out.print("remoteIPAddress: ");
				remoteIPAddress = scan.next();
				System.out.print("remotePort: ");
				remotePort = scan.nextInt();
				System.out.println();
			}
			catch(Exception e)
			{
				System.out.println("Type error going to defaults:");
				printDefaults();
			}

			spawnTwoPlayers();
			spawnConnection();
			break;
		case 3:
			printDefaults();
			spawnTwoPlayers();
			spawnConnection();
			break;
		default:
			System.out.println("Type error going to defaults:");
			printDefaults();
			spawnTwoPlayers();
			spawnConnection();

		}


		setDisplay();


	}
	
	/**
	 * creates a JFrame that will hold the players
	 * adds a key listner to the JFrame to move your player
	 */
	public static void setDisplay()
	{
		int widthOffset = 2;
		int lengthOffset = 24;
		
		JFrame f = new JFrame("TRON");
		f.setBounds(0, 0, fWidth + widthOffset, fHeight + lengthOffset);
		f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		display = new Display( fWidth, fHeight, players);
		display.setBackground( Color.BLACK );
		Container c = f.getContentPane();
		c.add(display);
		f.setResizable(true);
		f.setVisible(true);
		f.setFocusable(true);
		f.requestFocusInWindow();

		f.addKeyListener( new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode())
				{
				case(KeyEvent.VK_UP):
					if (localPlayer.getDirection() != 180) {
						localPlayer.setDirection(0);
					}           
				break;
				case(KeyEvent.VK_LEFT):
					if (localPlayer.getDirection() != 270) {
						localPlayer.setDirection(90);
					}           
				break;
				case(KeyEvent.VK_DOWN):
					if (localPlayer.getDirection() != 0) {
						localPlayer.setDirection(180);
					}
				break;
				case(KeyEvent.VK_RIGHT):
					if (localPlayer.getDirection() != 90) {
						localPlayer.setDirection(270);
					}           
				break;
				/*
				case(KeyEvent.VK_SPACE):
					localPlayer.boost();
				*/
				}
			}

			/**
			 * not implemented
			 */
			@Override
			public void keyReleased(KeyEvent arg0) 
			{

			}

			/**
			 * not implemented
			 */
			@Override
			public void keyTyped(KeyEvent arg0) 
			{

			}

		});
		
	}
	
	/**
	 * prints each player with his/her 
	 * number
	 * local port
	 * ip address
	 * remote port
	 */
	public static void printDefaults()
	{
		System.out.println("Defaults:");
		System.out.println("\tplayerNumber: " + playerNumber);
		System.out.println("\tlocalPort: " + localPort);
		System.out.println("\tremoteIPAddress: " + remoteIPAddress);
		System.out.println("\tremotePort: " + remotePort);

	}

	/**
	 * ends game
	 * resets players
	 * kills the sender and receiver
	 * prints "kill game" message
	 * currently not implemented
	 */
	public static void killGame()
	{
		for(TronPlayer p : players)
		{
			p.reset();
		}

		sender.kill();
		reciever.kill();
		System.out.println("killed game");
	}

	/**
	 * sets up the local and remote player
	 */
	public static void spawnTwoPlayers()
	{

		//Sets up player's and their locations based on playerNumber
		switch (playerNumber)
		{
		case 1:
			localPlayer = new LocalPlayer("local", fWidth*3/4, fHeight/2, 90, Color.GREEN);
			remotePlayer = new RemotePlayer("remote", fWidth/4, fHeight /2, 270, Color.RED);
			break;
		case 2:
			localPlayer = new LocalPlayer("local", fWidth/4, fHeight /2, 270, Color.RED);
			remotePlayer = new RemotePlayer("remote", fWidth*3/4, fHeight/2, 90, Color.GREEN);
			break;

		}

		players.add(localPlayer);
		players.add(remotePlayer);

	}

	/**
	 * sets up a computer player and human player
	 */
	public static void spawnOnePlayer(int numAI)
	{
		
		localPlayer = new LocalPlayer("local", fWidth*3/4, fHeight/2, 90, Color.GREEN);

		players.add(localPlayer);
		
		for(int i = 1; i <= numAI; i++)
		{
			players.add(new AITronPlayer("comp" + i, fWidth/4, fHeight /2 + 50 * i, 270, new Color((int)(64 * Math.random()) + 192, (int)(128 * Math.random()), (int)(128 * Math.random()))));
		}





	}

	/**
	 * initializes the reciever and sender
	 */
	public static void spawnConnection()
	{
		reciever = new PlayerDataReceiver(localPort);
		sender = new PlayerDataSender(remotePort, remoteIPAddress);
	}
}
