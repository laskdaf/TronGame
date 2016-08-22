import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

/**
 * TronPlayer a super class that models a generic player 
 *
 */
public class TronPlayer
{
	/**the name of the player*/
	protected String name;
	/**the player x along with the start x of the player*/
	protected int x, startX;
	/**the player y along with the start y of the player*/
	protected int y, startY;
	/**the player direction*/
	protected int direction;
	/**if the player has crashed or not*/
	protected boolean crash = false;
	/**the maximum size of the ArrayList that holds the tail locations*/
	protected int tailMax = 1000;
	/**an arrayList containing all player tail locations*/
	protected ArrayList locations;
	/**the color of the player*/
	protected Color c;
	/**the velocity of the player*/
	protected int velocity;
	/**the score of the player*/
	protected int score;
	/**the boost length of a player*/
	protected int boostCounter;
	/**the number of boosts a player is allowed*/
	protected int boostNum;
	
	/**keeps track of the last hit player*/
	protected TronPlayer lastHitPlayer;
	

    /**
     * @param name is the name of the player (local)
     * @param x is the start x position
     * @param y is the start y position
     * @param direction is the starting direction
     * @param c is the color
     * 
     * sets up the player with needed metrics
     */
	public TronPlayer( String name, int x, int y, int direction, Color c)
	{
		this.name = name;
		this.x = x;
		startX = x;
		this.y = y;
		startY = y;
		this.direction = direction;
		this.c = c;
		locations = new ArrayList<int[]>();
		score = 0;
		velocity = 2;
		boostCounter = 0;
		boostNum = 5;
	}

	/**
	 * @param g is the graphics
	 * very little functionality here:
	 * simply draws the player and updates the ArrayList length if it's too long
	 */
	public void action(Graphics g)
	{
		/*
		if(boostCounter > 0)
		{
			boostCounter--;
		}
		else if (boostCounter == 0)
		{
			velocity = 2;
		}
		*/
		
		
		if (getLocations().size() > tailMax && tailMax > 0)
			removeLocation(0);
		
		for (int i = 0; i < getLocations().size() - 1; i++) {
			g.setColor(getColor().darker().darker());
			g.fillRect(getLocations().get(i)[0]-5, getLocations().get(i)[1]-5, 10, 10);
		}

		g.setColor( getColor() );
		g.fillRect(getX(), getY(), 10, 10);
		
	}
	
	   /**
     * @return returns true if the player is out of bounds
     * Runs an out of bounds check
     */
    public boolean outOfBounds()
    {
        return getX() < 0 || getX() > TronMain.fWidth - 10 || getY() < 0 || getY() > TronMain.fHeight - 10;
    }

    /**
     * @return returns true if the player hit a players wall
     * Runs an player hit check
     */
    public boolean hitWall()
    {
        
        
        int[] temp = {getX(), getY()};
        
        for (TronPlayer testPlayer: TronMain.players)
        {
        	if (testPlayer.getLocations() == null)
        	{
        		return false; 
        	}
        	
        	ArrayList<int[]> tempLocations = testPlayer.getLocations();
   
            try
            {
                for (int[] loc : tempLocations )
                {
                    if (temp[0] + 5 == loc[0] && temp[1] + 5 == loc[1])
                    {
                    	if (testPlayer != this)
                    	{
                        	lastHitPlayer = testPlayer;
                    	}

                        return true;
                    }
                }   
            }
            catch(Exception e)
            {
            }
        }

        return false;

    }
	
	/**
	 * gives the player a boost
	 * currently not implemented in order
	 * to not give the local player an unfair
	 * advantage
	 */
	public void boost()
	{
		if(boostCounter == 0 && boostNum > 0)
		{
			boostCounter = 30;
			velocity = velocity * 4;
			boostNum--;
		}

	}
		
	public int getBoost()
	{
		return boostNum;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public Color getColor()
	{
		return c;
	}
	
	public String getName()
	{
		return name;
	}

	public int getDirection()
	{
		return direction;
	}

	public ArrayList<int[]> getLocations()
	{
		return locations;
	}

	public void setX( int x )
	{
		this.x = x;
	}

	public void setY( int y )
	{
		this.y = y;
	}

	public int getVelocity()
	{
		return velocity;
	}

	public void setDirection( int d )
	{
		direction = d;
	}

	public  void addLocation( int[] xy )
	{
		//attempts a try catch to protect against comodification error
		try
		{
			locations.add( xy );
		}
		catch (Exception e)
		{
			
		}
	}

	public void removeLocation( int x )
	{
		try
		{
			locations.remove(x);	
		}
		catch(Exception e)
		{
			
		}

	}

	public void clearLocations()
	{
		try
		{
			locations.clear();	
		}
		catch(Exception e)
		{
			
		}

	}

	/**
	 * sets the crash flag to true
	 */
	public void crash()
	{
		crash = true;
	}

	/**
	 * sets the player back at the start point
	 * direction is not reset for strategic game play
	 */
	public void reset()
	{
		x = startX;
		y = startY;
		boostNum = 3;
		clearLocations();
	}
	
	public void setScore(int s)
	{
		score = s;
	}

	public int getScore()
	{
		return score;
	}
	
	/**
	 * increments the score
	 */
	public void incrementScore()
	{
		score++;
	}
}
