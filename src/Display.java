import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Display
 * Runs the entire app in the form of a JPanel
 * -Draws both players and updates their movement
 * -Draws the score
 * -Draws the boundary
 *
 */
public class Display extends JPanel{

	/** width and height of the frame*/
    static int fWidth, fHeight;
    /** holds the players in the game*/
    public ArrayList<TronPlayer> players;
	
    /**
     * constructor of the Display class
     * initializes fWidth, fHeight, and players
     * 
     * @param width frame width
     * @param height frame height
     * @param players game players
     */
    public Display (int width, int height, ArrayList<TronPlayer> players)
    {
    	fWidth = width;
    	fHeight = height;
    	this.players = players;
    }
    
    /**
     * repaints the JFrame
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.CYAN);
        g.drawRect(0, 0, fWidth, fHeight);
        //drawImages(g);
        updatePlayers(g);
        drawScoreBoard(g);

        repaint();
    }
    
    /**
     * @param g is the graphics
     * updatePlayers calls each of the players' action method
     */
    public void updatePlayers(Graphics g)
    {
    	//System.out.println(players);
        for(TronPlayer player: players) 
        {
        	player.action(g);
        }
    }
    
    /**
     * @param g is the graphics
     * draws and updates the scoreBoard based on both the player names and scores
     */
    public void drawScoreBoard(Graphics g)
    {
    	g.setColor(new Color(230, 230, 230));
    	
    	for(int i = 0; i < TronMain.players.size(); i++)
    	{
    		g.drawString(TronMain.players.get(i).name + ": " + TronMain.players.get(i).getScore(), 20 + 80 * i, fHeight - 10);
    	}

    }

    
    /**
     * @param g is the graphics
     * draws a TRON logo on the playing field
     * currently not implemented because of slower game speeds
     */
    public void drawImages(Graphics g)
    {
    	BufferedImage img = null;
    	
        try {
            img = ImageIO.read(this.getClass().getResource("tronLogo.jpg"));
        } catch (IOException e) {
        	e.printStackTrace();
        }
        
        g.drawImage(img,10, 10, this);
    }
   
    /**
     * resets the entire field if needed
     */
    public void reset()
    {
        for (TronPlayer player: players)
        {
            player.reset();
            
        }
    }      
}

