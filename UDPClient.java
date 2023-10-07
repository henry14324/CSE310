import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class UDPClient extends PingClient{
    /** Host to ping */
    String remoteHost;
    
    /** Port number of remote host */
    int remotePort;

    /** How many pings to send */
    static final int NUM_PINGS = 10;
    
    /** How many reply pings have we received */
    int numReplies = 0;
    
    /** Crate an array for holding replies and RTTs */
    long [] RTTs = new long [10];

    /* Send our own pings at least once per second. If no replies received
     within 5 seconds, assume ping was lost. */
    /** 1 second timeout for waiting replies */
    static final int TIMEOUT = 1000;
    
    /** 5 second timeout for collecting pings at the end */
    static final int REPLY_TIMEOUT = 5000;

    /** constructor **/
    public UDPClient(String host, int port) {
        this.remoteHost = host;
        this.remotePort = port;
    }

/**
     * Main function. Read command line arguments and start the client.
     */
    public static void main(String args[]) {

        String host = null;
        int port = 0;
        /* Parse host and port number from command line */
        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
            } 
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need two arguments: remoteHost remotePort");
            System.exit(-1);
        } 
        catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        }
            
        System.out.println("Contacting host " + host + " at port " + port);
            
        //. . . . . 
        UDPClient client = new UDPClient(host, port);
        client.run();
    }

    public void run() {
    
        /* Create socket. We do not care which local port we use. */
		createSocket();
        try {
            socket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            System.out.println("Error setting timeout TIMEOUT: " + e);
        }

        for (int i = 0; i < NUM_PINGS; i++) {

            /* Message we want to send to server is just the current time. */
            long time = System.currentTimeMillis();
            Message ping = null;
 		    //. . . . . 
            
             try {
                String timeStr = String.valueOf(time);
                ping = new Message(InetAddress.getByName(remoteHost), remotePort, timeStr);
                sendPing(ping);

            } catch (UnknownHostException e) {
                System.out.println("Cannot find host: " + e);
            }
		 //. . . . . 
            /* Read the reply by getting the received ping message */
            try {
                Message reply = receivePing();
                handleReply(reply.getContents());
                 //. . . . .
            } catch (SocketTimeoutException e) {
                /* Reply did not arrive. Do nothing for now. Figure
                 * out lost pings later. */
            }
        }
        
        try {
            socket.setSoTimeout(REPLY_TIMEOUT);
        } catch (SocketException e) {
            System.out.println("Error setting timeout REPLY_TIMEOUT: " + e);
        }
        while (numReplies < NUM_PINGS) {
            try {
                Message reply = receivePing();
                handleReply(reply.getContents());
                 //. . . . .
            } catch (SocketTimeoutException e) {
                /* Nothing coming our way apparently. Exit loop. */
                break;
                 //. . . . . . .
            }
        }
        /* Print statistics */
        for (int i = 0; i < NUM_PINGS; i++) {
            System.out.println("RTT " + i + " " + RTTs[i]);
        }

    }
     
    private void handleReply(String reply) {
        long beforeTime = Long.valueOf(reply.trim());
        long currTime = System.currentTimeMillis();
        
        /* Calculate RTT and store it in the rtt-array. */
        long RTT = currTime - beforeTime;
        RTTs[numReplies] = RTT;
        System.out.println("PING " + numReplies + " " + reply);
        numReplies++;
    }    
 
}
