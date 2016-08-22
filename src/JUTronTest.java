import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


public class JUTronTest {
	
	/**
	 * this tests if the main can create one local and one remote player
	 * depending on the player's playerNumber
	 */
	@Test
	public void spawnTwoPlayersTest()
	{
		TronMain main = new TronMain();
		main.players = new ArrayList<TronPlayer>();
		main.playerNumber = 1;
		main.spawnTwoPlayers();
		assertTrue( (main.players.get(0) instanceof LocalPlayer) && (main.players.get(1) instanceof RemotePlayer) );
	}
	
	/**
	 * this tests if the main can create one local player
	 * and a set amount of comp players
	 */
	@Test
	public void spawnOnePlayerTest()
	{
		TronMain main = new TronMain();
		main.players = new ArrayList<TronPlayer>();
		main.spawnOnePlayer(3);
		assertTrue( main.players.size() == 4 );
	}
	
	@Test
	public void spawnConnection()
	{
		TronMain main = new TronMain();
		main.remoteIPAddress = "127.0.0.1";
		main.localPort = 7000;
		main.remotePort = 7000;
		main.spawnConnection();
		assertEquals(main.localPort, main.reciever.localPort);
		assertEquals(main.remotePort, main.sender.remotePort);
	}
	
	//TronPlayer Tests
	
	/**
	 * tests if the instance variables are set up correctly
	 */
	@Test 
	public void TronPlayerTest()
	{
		TronPlayer player = new TronPlayer("name", 20, 30, 0, Color.BLUE);
		assertEquals("name", player.getName());
		assertEquals(20, player.getX());
		assertEquals(30, player.getY());
		assertEquals(0, player.getDirection());
		assertEquals(Color.BLUE, player.getColor());


	}

	
	/**
	 * tests if the player's outOfBounds returns true when 
	 * the player's position exits the "arena"
	 */
	@Test
	public void outOfBoundsTest()
	{
		int [] xy = {1201,1201};
		TronPlayer player = new TronPlayer("name", 20, 30, 0, Color.BLUE);
		player.addLocation(xy);
		player.setX(xy[0]);
		player.setY(xy[1]);
		assertTrue(player.outOfBounds());
	}
	
	/**
	 * tests if the player's hitWall returns true
	 * when the player's position occupies the same location 
	 * as a wall
	 */
	@Test
	public void hitWallTest()
	{
		TronMain main = new TronMain();
		
		int [] xy = {20, 20};
		TronPlayer player = new TronPlayer("name", 20, 30, 0, Color.BLUE);
		main.players = new ArrayList<TronPlayer>();
		main.players.add(player);
		player.addLocation(xy);
		player.addLocation(xy);
		player.setX(15);
		player.setY(15);
		
		assertTrue(player.hitWall());
	}
	
	
	/**
	 * tests if this method whipes out all the coordinates saved in the Player's
	 * tail arraylist
	 */
	@Test
	public void clearLocationsTest()
	{
		int [] xy = {2,3};
		TronPlayer player = new TronPlayer("name", 20, 30, 0, Color.BLUE);
		player.locations.add( xy );
		player.clearLocations();
		assertEquals(0, player.getLocations().size());
	}
	
	/**
	 * tests if the player's coordinates are reset to their starting position
	 * tests if the player's tail is reset to being empty
	 */
	@Test
	public void resetTest()
	{
		int [] xy = {2,3};
		TronPlayer player = new TronPlayer("name", 20, 30, 0, Color.BLUE);
		player.setX(50);
		player.setY(70);
		player.locations.add( xy );
		player.reset();
		assertEquals(20, player.getX());
		assertEquals(30, player.getY());
		assertEquals(0, player.getLocations().size());
	}
	
	/**
	 * tests if the score is set correctly
	 */
	@Test
	public void setScoreTest()
	{
		TronPlayer player = new TronPlayer("name", 20, 30, 0, Color.BLUE);
		player.setScore(4);
		assertEquals(4, player.getScore());
	}
	
	/**
	 * tests if the score will increase by one after calling incrementScore()
	 */
	@Test
	public void incrementScore()
	{
		TronPlayer player = new TronPlayer("name", 20, 30, 0, Color.BLUE);
		player.incrementScore();
		assertEquals(1, player.getScore());
	}
	
	//LocalPlayer Tests
	
	//everything has been tested in TronPlayer tests
	
	//RemotePlayer Tests
	
	/**
	 * tests if the remotePlayer class can be constructed
	 * tests if it can add a new location and if it updates its location
	 */
	@Test
	public void remoteLocationTest()
	{
		int [] xy = {10,10};
		RemotePlayer player = new RemotePlayer("name", 20, 30, 0, Color.BLUE);
		player.addLocation(xy);
		assertEquals(5, player.getX());
		assertEquals(5, player.getY());
	}

	
	//AITronPlayer Tests
	
	/**
	 * intentionally places AI near boundary and checks the nearBoundary flag
	 */
	@Test
	public void nearBoundaryTest()
	{
		AITronPlayer testAIPlayer = new AITronPlayer("testComp", 10, 10, 0, Color.RED);
		
		assertTrue(testAIPlayer.nearBoundary());
	}
		
	
	/**
	 * creates an testAIplayer then intentionally adds a location right in front of it
	 * in order to check nearWall()
	 */
	@Test
	public void nearWallTest()
	{
		TronMain main = new TronMain();
		main.players = new ArrayList<TronPlayer>();
		AITronPlayer testAIPlayer = new AITronPlayer("testComp", 10, 10, 180, Color.RED);
		testAIPlayer.setX(15);
		testAIPlayer.setY(15);
		main.players.add(testAIPlayer);
		
		//intentionally adds a wall in front of the testAIPlayer
		int[] temp = {20, 25};
		testAIPlayer.addLocation(temp);
		
		assertEquals(true, testAIPlayer.nearWall());
		
	}
	

	
	//PlayerDataReciever Tests
	
	/**
	 * first checks if the constructor is properly made
	 * then checks if the receiver is properly updating remote player locations
	 * by creating a sender and sending one value
	 */
	@Test
	public void playerDataRecieverTest()
	{

		PlayerDataReceiver reciever = new PlayerDataReceiver(3000);
		
		
		//checks if server is properly created
		assertNotNull(reciever.server);
		
		
		//checks if remotePlayer location is updated
		TronMain main = new TronMain();
		main.remotePlayer = new RemotePlayer("remote", 30, 30, 270, Color.RED);
		main.players.add(main.remotePlayer);
		
		PlayerDataSender sender = new PlayerDataSender(3000, "127.0.0.1");
		int[] temp = {10, 10};
		sender.send(temp);
		
		//Giving the network the adequate time to send the data
		try {
		    TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
		    //Handle exception
		}
		
		assertNotNull( main.remotePlayer.getLocations().get(0));
	}
	
	/**
	 * checks to see if the reciever closes
	 * the socket once killed
	 */
	@Test
	public void killTestReciever()
	{
		PlayerDataReceiver reciever = new PlayerDataReceiver(4000);
		PlayerDataSender sender = new PlayerDataSender(4000, "127.0.0.1");
		
		reciever.kill();
		
		assertTrue(reciever.server.isClosed());
	}
	
	//PlayerDataSender Tests
	
	/**
	 * creates a sender and a receiver and checks that remoteSocket
	 * is not null
	 */
	@Test
	public void PlayerDataSenderTest()
	{
		PlayerDataReceiver reciever = new PlayerDataReceiver(5000);
		PlayerDataSender sender = new PlayerDataSender(5000, "127.0.0.1");
		
		assertNotNull(sender.remoteSocket);
		
		sender.kill();
	}
	
	/**
	 * similar implementation as receiver tests
	 * because if the reciever is recieving proper input
	 * then the sender is sending proper output
	 */
	@Test
	public void sendTest()
	{
		PlayerDataReceiver reciever = new PlayerDataReceiver(6000);
		
		
		//checks if remotePlayer location is updated
		TronMain main = new TronMain();
		main.remotePlayer = new RemotePlayer("remote", 30, 30, 270, Color.RED);
		main.players.add(main.remotePlayer);
		
		PlayerDataSender sender = new PlayerDataSender(6000, "127.0.0.1");
		int[] temp = {10, 10};
		sender.send(temp);
		
		//Giving the network the adequate time to send the data
		try {
		    TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
		    //Handle exception
		}
		
		assertNotNull( main.remotePlayer.getLocations().get(0));


		
	}
		
}
