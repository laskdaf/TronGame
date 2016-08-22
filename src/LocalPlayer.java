import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


/**
 * LocalPlayer
 * Inherits TronPlayer and adds functionality unique to
 * the local player only
 *
 */
public class LocalPlayer extends TronPlayer
{

    /**
     * @param name is the name of the player (local)
     * @param x is the start x position
     * @param y is the start y position
     * @param direction is the starting direction
     * @param c is the color
     * 
     * constructor
     * simply calls the super class constructor, no added functionality here
     */
    public LocalPlayer( String name, int x, int y, int direction, Color c )
    {
        super( name, x, y, direction, c );
    }
    
    /**
     * overrides the super class action method
     * added functionality:
     * sets movement based on direction
     * tests out of bounds or hitWall
     * self updates locations
     * sends its most recent location to sender
     */
    public void action(Graphics g)
    {
        switch(getDirection())
        {
        case(0):
            setY(getY()-getVelocity());
        break;
        case(90):
            setX(getX()-getVelocity());
        break;
        case(180):
            setY(getY()+getVelocity());
        break;
        case(270):
            setX(getX()+getVelocity());
        break;
        }
        
        if (outOfBounds() || hitWall()) 
        {
            crash();
            reset();
            if(lastHitPlayer instanceof RemotePlayer)
            {
                int[] crashFlag = {-1, -1};
                TronMain.sender.send(crashFlag);
                lastHitPlayer.incrementScore();
            }
            else if(lastHitPlayer != null)
            {
                lastHitPlayer.incrementScore();
            }
            


            return;
        }
  
        super.action(g);
        
        int[] temp  = {getX() + 5, getY() + 5};
        
        addLocation(temp);
        if (getLocations().size() > tailMax && tailMax > 0)
            removeLocation(0);
        
        if(TronMain.remotePlayer != null)
        {
            TronMain.sender.send(temp);
        }
        
    }
    
 
}