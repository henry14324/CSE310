import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.net.DatagramPacket;
//import java.util.*;
//import java.lang.Integer;
public class UDPServer {
	
    public static void main(String args[]) throws Exception {	
           int port = 0;
        
        /** Parse port number from command line **/
        try {
            port = Integer.parseInt(args[0]) ;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need one argument: port number.");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        }

                try (/** Create a new datagram socket at the port **/
        DatagramSocket serverSocket = new DatagramSocket(port)) {
            /** Let the user know the server is running **/
            System.out.println("The UDP server is listening on port " + port);

            int count = 0;

            while (true) {
                        
                    /** Create a new datagram packet and let the socket receive it **/
                        DatagramPacket newPacket = new DatagramPacket(new byte[512], 512);
                        serverSocket.receive(newPacket);
                        /** Print the message received **/
                        byte [] packetData = newPacket.getData();
                        //………..
                        String message = new String(packetData, StandardCharsets.UTF_8);
                        //long test = Long.parseLong(message);
                        System.out.println("PING " + count + " " + message);
                        count++;
                        
                        /** Get the IP Address of the Sender **/
                        InetAddress ipAddr = newPacket.getAddress();
                        //……..
                        /** Get the port of the Sender **/
                        int senderPort = newPacket.getPort() ;
                        
                        /** Prepare the data to send back **/
                        //. . . . .
                        DatagramPacket sendPacket = new DatagramPacket(packetData, 512, ipAddr, senderPort);             
                        /** Send the packet **/
                        serverSocket.send(sendPacket);
                        //. . . . .
                        
            }
        }



    }
}
