import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * A computer player that extends TronPlayer.
 * It is created during one player mode.
 * It does something that he's working on but hasn't finished yet
 *
 */
public class AITronPlayer extends TronPlayer {

	/**
	 * constructor for the AITronPlayer class
	 * 
	 * @param name name of player
	 * @param x x value
	 * @param y y value
	 * @param direction player direction
	 * @param c color
	 */
	public AITronPlayer(String name, int x, int y, int direction, Color c) {
		super(name, x, y, direction, c);
	}
	
	/**
	 * moves the AITronPlayer and adds points to its tail
	 */
	public void action(Graphics g)
	{
		double randomTurn = Math.random();

		
		if (randomTurn < 0.004)
		{
			smartTurn();
		}
		
		if (nearWall() || nearBoundary())
		{
			smartTurn();
		}
		
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
            if(lastHitPlayer != null)
            {
                lastHitPlayer.incrementScore();
            }
			return;
		}

		int[] temp  = {getX() + 5, getY() + 5};

		addLocation(temp);

		super.action(g);
	}

	/**
	 * determines if the AITronPlayer
	 * 
	 * @return true if the AITronPlayer is near the edges
	 */
	public boolean nearBoundary()
	{
		int alertDistance = 15;

		//checks if close to relevant boundary based on direction
		switch(direction)
		{
		case 0:
			return getY() < alertDistance;
		case 90:
			return getX() < alertDistance;
		case 180:
			return getY() > TronMain.fHeight - alertDistance;
		case 270:
			return getX() > TronMain.fWidth - alertDistance;
		}
		return false;
	}

	/**
	 * checks if this player is near another player wall
	 * @return true if the player is alertDistance away from a player wall
	 */
	public boolean nearWall()
	{
		int alertDistance = 15;
		for (TronPlayer testPlayer: TronMain.players)
		{
			for (int[] loc : testPlayer.getLocations())
			{
				switch(direction)
				{
				case 0:	
					if (getX() + 5 == loc[0] && getY() + 5 - loc[1] < alertDistance && getY() + 5 - loc[1] > velocity)
					{
						return true;
					}
					break;
				case 90:
					if (getX()  + 5 -  loc[0] < alertDistance && getY() + 5 == loc[1] && getX() + 5 - loc[0] > velocity)
					{
						return true;
					}
					break;
				case 180:
					if (getX() + 5 == loc[0] && loc[1] - (getY() + 5) < alertDistance && loc[1] - (getY() + 5) > velocity)
					{
						return true;
					}
					break;
				case 270:
					if (loc[0] - (getX() + 5) < alertDistance && loc[0] - (getX() + 5) > velocity && getY() + 5 == loc[1])
					{
						return true;
					}
					break;
				}
			}
		}
		return false;
	}
	
	/**
	 * attempts to turn into a direction closest to the local player's direction
	 * currently not implemented, due to being a weaker algorithm than smartTurn
	 * but may be implemented for a more strategic AI
	 */
	public void turnTowardsPlayer()
	{
		if (getDirection() == TronMain.localPlayer.getDirection())
		{
			return;
		}
		else if((getDirection() + 90) % 360 == TronMain.localPlayer.getDirection())
		{
			setDirection(getDirection() + 90);
		}
		else if((getDirection() + 270) % 360 == TronMain.localPlayer.getDirection())
		{
			setDirection(getDirection() + 270);
		}
		else
		{
			smartTurn();
		}
	}
	
	/**
	 * chooses left or right turn
	 * based on the moving direction and quadrant on the playing field
	 * essentially attempts to turn towards the center when prompted to turn
	 * this algorithm is not perfect giving way for the local player windows
	 * of opportunity to beat the AI
	 */
	public void smartTurn()
	{

		
		if (getX() > TronMain.fWidth / 2)
		{
			//quadrant 4
			if(getY() > TronMain.fWidth / 2)
			{

				if(getDirection() == 180 || getDirection() == 0)
				{
					setDirection( ( getDirection() + 270) % 360 );
				}
				else
				{
					setDirection( ( getDirection() + 90) % 360 );
				}
			}
			//quadrant 1
			else
			{
				if(getDirection() == 270 || getDirection() == 180)
				{
					setDirection( ( getDirection() + 270) % 360 );
				}
				else
				{
					setDirection( ( getDirection() + 90) % 360 );
				}

				
			}

						
		}
		else
		{
			//quadrant 3
			if(getY() > TronMain.fHeight / 2)
			{
				if(getDirection() == 180 || getDirection() == 0)
				{
					setDirection( ( getDirection() + 90) % 360 );
				}
				else
				{
					setDirection( ( getDirection() + 270) % 360 );
				}
			}
			//quadrant 2
			else
			{

				
				if(getDirection() == 180 || getDirection() == 90)
				{
					setDirection( ( getDirection() + 90) % 360 );
				}
				else
				{
					setDirection( ( getDirection() + 270) % 360 );
				}
			}

			
		}	
	}
}
