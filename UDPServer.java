public class UDPServer {
	
    public static void main(String args[]) throws Exception {	
           int port = 0;
        
        /** Parse port number from command line **/
        try {
            port = args[0] ;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need one argument: port number.");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        }

                /** Create a new datagram socket at the port **/
        DatagramSocket serverSocket = new DatagramSocket();


        /** Let the user know the server is running **/
        System.out.println("The UDP server is listening on port " + port);

        while (true) {
                    
                /** Create a new datagram packet and let the socket receive it **/
                    ……..
                    
                    /** Print the message received **/
                    ………..
                    
                    /** Get the IP Address of the Sender **/
                    
                    ……..
                    /** Get the port of the Sender **/
                    int senderPort = . . . ;
                    
                    /** Prepare the data to send back **/
                    . . . . .             
                    /** Send the packet **/
                    . . . . .
                    
        }



    }
}
