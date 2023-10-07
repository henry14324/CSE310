import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
public class PingClient {
    /** Socket which we use. */
    DatagramSocket socket;

    /** Set maximum length of a ping message to 512. */
    int MaxLength = 512;

    /** Create a datagram socket with random port for sending UDP messages */
    public void createSocket() {
        try {
            socket =  new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Error creating socket: " + e);
        }
    }

    /** Create a datagram socket for receiving UDP messages. 
     * This socket must be bound to the given port. */
    public void createSocket(int port) {
	try {
	    socket = new DatagramSocket(port) ;
	} catch (SocketException e) {
	    System.out.println("Error creating socket: " + e);
	}
    }

    /** Send a UDP ping message which is given as the argument. */
    public void sendPing(Message ping) {
        
	  //.. . . . . .
      byte [] message = ping.getContents().getBytes(StandardCharsets.UTF_8);
      InetAddress host = ping.getIP();
      int port = ping.getPort(); 

      try {
            /* Create a datagram packet addressed to the recipient */
            DatagramPacket sendPacket = new DatagramPacket(message, message.length, host, port);
            
            /* Send the packet */
            socket.send(sendPacket);
            System.out.println("Sent message to " + host + ":" + port);
        } catch (IOException e) {
            System.out.println("Error sending packet: " + e);
      }
    }


   /** Receive a UDP ping message and return the received message. 
     * We throw an exception to indicate that the socket timed out. 
     * This can happen when a message is lost in the network. */
    

    public Message receivePing() throws SocketTimeoutException {
        
        /* Create packet for receiving the reply */
    //. . . . . .
        DatagramPacket receivePacket = new DatagramPacket(new byte[MaxLength], MaxLength);
        Message reply = null;

        /* Read message from socket. */
        try {
            //. . . . . 
            socket.receive(receivePacket);
            System.out.println("Received message from " +receivePacket.getAddress() +":" + receivePacket.getPort());
            reply = new Message(receivePacket.getAddress(), receivePacket.getPort(), new String(receivePacket.getData(), StandardCharsets.UTF_8));
            
        } catch (SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            System.out.println("Error reading from socket: " + e);
        }

        return reply;
    }



}
