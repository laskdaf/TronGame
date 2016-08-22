

import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;


/**
 * Thread class that deals with receiving data from a remote host and
 * relaying it back to the main chat program.  You may assume that the
 * socket is already connected in the constructor of this class.  From
 * there, your thread should wait for input to come in over the socket,
 * read that data, and send it back to the display.
 *
 * If the socket is closed, the thread should terminate.
 *
 * The thread should also have a method that causes it to terminate, so
 * the main program can kill the thread at any time.  It should attempt
 * to close the socket before exiting.
 *
 * $Id: ChatReceiver.java
 */
public class PlayerDataReceiver extends Thread{

	/** Socket used to communicate with remote host */
	protected Socket remoteSocket;

	/** Reader to grab input from over the wire */
	protected BufferedReader input;

	/**
	 * the local port saved when data reciever is initalized
	 */
	protected int localPort;

	protected ServerSocket server;

	/**
	 * Constructor. The given Socket is already connected and ready for use.
	 * 
	 * @param cd The ChatDisplay object that displays any error messages
	 * @param n The name of the socket this thread goes with
	 * @param sock The socket to receive data from
	 */
	public PlayerDataReceiver(int localPort)
	{

		this.localPort = localPort;

		try {
			server = new ServerSocket(localPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		start();
	}

	/**
	 * Stop waiting for input and terminate this thread.
	 * 
	 * You should also attempt to close the socket before exiting.
	 */
	public void kill()
	{
		try
		{
			if(server != null)
			{
				server.close();	
			}

		}
		catch ( IOException ioe )
		{
			ioe.printStackTrace();
		}
	}

	/**
	 * Main thread execution loop. This method should contain a tight loop that
	 * attempts to read data from the socket, and relays it to to main display
	 * class. When an exception occurs, the thread should close the socket,
	 * clean up, and exit its loop.
	 */
	public void run()
	{

		try
		{
			remoteSocket = server.accept(); 


			try
			{
				input = new BufferedReader( new InputStreamReader( remoteSocket.getInputStream() ) );

			}
			catch ( IOException e )
			{
				e.printStackTrace();
			}


			String locationString;

			while (true)
			{
				locationString = input.readLine();
				if (locationString != null)
				{
					String delims = " ";
					String[] locationStringArray = locationString.split(delims);
					int[] locations = {Integer.parseInt( locationStringArray[0] ), Integer.parseInt( locationStringArray[1])};

					//If the remote player crashes
					if(locations[0] == -1 && locations[1] == -1)
					{
						TronMain.remotePlayer.crash();
						TronMain.remotePlayer.reset();
						TronMain.localPlayer.incrementScore();

						continue;
					}

					TronMain.remotePlayer.addLocation(locations);
					//System.out.println("RemoteLoc: [" + locations[0] + "," + locations[1] + "]");
				}

			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

		try
		{
			// clean up the sockets
			input.close();
			server.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

		System.out.println( getName() + " terminating" );
	}



}

