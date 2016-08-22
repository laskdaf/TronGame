import java.awt.Color;



/**
 * RemotePlayer
 * 
 * extends TronPlayer and slightly modifies functionality to fit the remote player actions
 *
 */
class RemotePlayer extends TronPlayer
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
    public RemotePlayer( String name, int x, int y, int direction, Color c )
    {
        super( name, x, y, direction, c );
    }
    
    /**
     * @param xy is the most recent location of the player sent
     * through the network
     * slightly modifies addLocation so that it properly sets the
     * x and y components of the remote player without keyboard input
     */
    public void addLocation( int[] xy )
    {

    	
        locations.add( xy );
        setX(xy[0] - 5);
        setY(xy[1] - 5);
        
		if (getLocations().size() > tailMax && tailMax > 0)
			removeLocation(0);
    }

    
}