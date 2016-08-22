
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.PrintWriter;
import java.io.IOException;



/**
 * Thread class that deals with sending data from the program to a remote
 * host.  You may assume that the socket is already connected in the
 * constructor of this class.  From there, your thread should wait for
 * input from the main program, and send that input out over the socket.
 *
 * If the socket is closed, the thread should terminate.
 *
 * The thread should also have a method that causes it to terminate, so
 * the main program can kill the thread at any time.
 *
 * $Id: ChatSender.java
 */
public class PlayerDataSender extends Thread {

    /** Whether this sender should continue sending, or if it's done */
    protected boolean alive = false;

    /** Socket used to communicate with remote host */
    protected Socket remoteSocket;

    /** Writer to use when sending information over the socket */
    protected PrintWriter output;

    /** the port of the remote machine*/ 
    protected int remotePort;
    
    /**the IP of the remote machine*/
    protected String IPAddress;

    /**
     * Constructor. The given Socket is already connected and ready for use.
     * 
     * @param cd The ChatDisplay object that displays any error messages
     * @param n The name of the socket this thread goes with
     * @param sock The socket to send data out to
     */
    public PlayerDataSender(int remotePort, String IPAddress)
    {
    	


        this.remotePort = remotePort;
        this.IPAddress = IPAddress;
        System.out.print("Waiting for the other player to connect ...");

        //continues to run until a connection is made
        while (true)
        {
            try
            {
                remoteSocket = new Socket(IPAddress, remotePort);
            }
            catch (java.net.ConnectException e)
            {
                System.out.print(".");
            }
            catch ( UnknownHostException e )
            {
                e.printStackTrace();
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            } 
            catch(Exception e)
            {
            	e.printStackTrace();
            }

            if(remoteSocket!= null && remoteSocket.isConnected())
            {
                System.out.println("Connected!");
                
                break;
            }

            try
            {
                sleep(1000);
            }
            catch ( InterruptedException e )
            {
                e.printStackTrace();
            }

        }

        try
        {
            output = new PrintWriter( remoteSocket.getOutputStream(), true );
        }
        catch ( IOException e )
        {
             e.printStackTrace();
        }
        
        start();


    }

    /**
     * Send the given string out over the socket belonging to this thread.
     * 
     * @param s The data to send over the socket
     */
    public void send(int[] location)
    {
        if (remoteSocket.isConnected())
        {
            try
            {
                output.flush();
                output.write(location[0] + " " + location[1] + "\n");
                //System.out.println("Sent: [" + location[0] + "," + location[1] + "]");

            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }  
        }
    }

    /**
     * Stop waiting for input and terminate this thread.
     */
    public void kill()
    {
        alive = false;
    }

    
    /**
     * Main thread execution loop. This method should contain a tight loop that
     * calls sleep() and checks the state of the thread to see if it should
     * still be running. The argument to sleep() should be at least 1 second.
     */
    public void run()
    {

        //keeps the socket open until the kill method is called
        while ( alive )
        {

            try
            {
                sleep( 1000 );
            }
            catch ( InterruptedException ie )
            {
            }
            catch ( Exception e )
            {
                alive = false;

                e.printStackTrace();
            }
        }

        try
        {
            // clean up the sockets
            output.close();
            remoteSocket.close();
        }
        catch ( Exception e )
        {
        }
    }
}
