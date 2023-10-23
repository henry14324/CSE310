
Code for proxy cache lab

This page contains the code for the proxy cache lab. You can copy the code into the correct files.
ProxyCache.java

/**
 * ProxyCache.java - Simple caching proxy
 *
 *
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {
    /** Port for the proxy */
    private static int port;
    /** Socket for client connections */
    private static ServerSocket socket;

    /** Create the ProxyCache object and the socket */
    public static void init(int p) {
	port = p;
	try {
	    socket = /* Fill in */;
	} catch (IOException e) {
	    System.out.println("Error creating socket: " + e);
	    System.exit(-1);
	}
    }

    public static void handle(Socket client) {
	Socket server = null;
	HttpRequest request = null;
	HttpResponse response = null;

	/* Process request. If there are any exceptions, then simply
	 * return and end this request. This unfortunately means the
	 * client will hang for a while, until it timeouts. */

	/* Read request */
	try {
	    BufferedReader fromClient = /* Fill in */;
	    request = /* Fill in */;
	} catch (IOException e) {
	    System.out.println("Error reading request from client: " + e);
	    return;
	}
	/* Send request to server */
	try {
	    /* Open socket and write request to socket */
	    server = /* Fill in */;
	    DataOutputStream toServer = /* Fill in */;
	    /* Fill in */
	} catch (UnknownHostException e) {
	    System.out.println("Unknown host: " + request.getHost());
	    System.out.println(e);
	    return;
	} catch (IOException e) {
	    System.out.println("Error writing request to server: " + e);
	    return;
	}
	/* Read response and forward it to client */
	try {
	    DataInputStream fromServer = /* Fill in */;
	    response = /* Fill in */;
	    DataOutputStream toClient = /* Fill in */;
	    /* Fill in */
	    /* Write response to client. First headers, then body */
	    client.close();
	    server.close();
	   
	} catch (IOException e) {
	    System.out.println("Error writing response to client: " + e);
	}
    }


    /** Read command line arguments and start proxy */
    public static void main(String args[]) {
	int myPort = 0;
	
	try {
	    myPort = Integer.parseInt(args[0]);
	} catch (ArrayIndexOutOfBoundsException e) {
	    System.out.println("Need port number as argument");
	    System.exit(-1);
	} catch (NumberFormatException e) {
	    System.out.println("Please give port number as integer.");
	    System.exit(-1);
	}
	
	init(myPort);

	/** Main loop. Listen for incoming connections and spawn a new
	 * thread for handling them */
	Socket client = null;
	
	while (true) {
	    try {
		client = /* Fill in */;
		handle(client);
	    } catch (IOException e) {
		System.out.println("Error reading request from client: " + e);
		/* Definitely cannot continue processing this request,
		 * so skip to next iteration of while loop. */
		continue;
	    }
	}

    }
}

HttpRequest.java

/**
 * HttpRequest - HTTP request container and parser
 *
 * 
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest {
    /** Help variables */
    final static String CRLF = "\r\n";
    final static int HTTP_PORT = 80;
    /** Store the request parameters */
    String method;
    String URI;
    String version;
    String headers = "";
    /** Server and port */
    private String host;
    private int port;

    /** Create HttpRequest by reading it from the client socket */
    public HttpRequest(BufferedReader from) {
	String firstLine = "";
	try {
	    firstLine = from.readLine();
	} catch (IOException e) {
	    System.out.println("Error reading request line: " + e);
	}

	String[] tmp = firstLine.split(" ");
	method = /* Fill in */;
	URI = /* Fill in */;
	version = /* Fill in */;

	System.out.println("URI is: " + URI);

	if (!method.equals("GET")) {
	    System.out.println("Error: Method not GET");
	}
	try {
	    String line = from.readLine();
	    while (line.length() != 0) {
		headers += line + CRLF;
		/* We need to find host header to know which server to
		 * contact in case the request URI is not complete. */
		if (line.startsWith("Host:")) {
		    tmp = line.split(" ");
		    if (tmp[1].indexOf(':') > 0) {
			String[] tmp2 = tmp[1].split(":");
			host = tmp2[0];
			port = Integer.parseInt(tmp2[1]);
		    } else {
			host = tmp[1];
			port = HTTP_PORT;
		    }
		}
		line = from.readLine();
	    }
	} catch (IOException e) {
	    System.out.println("Error reading from socket: " + e);
	    return;
	}
	System.out.println("Host to contact is: " + host + " at port " + port);
    }

    /** Return host for which this request is intended */
    public String getHost() {
	return host;
    }

    /** Return port for server */
    public int getPort() {
	return port;
    }

    /**
     * Convert request into a string for easy re-sending.
     */
    public String toString() {
	String req = "";

	req = method + " " + URI + " " + version + CRLF;
	req += headers;
	/* This proxy does not support persistent connections */
	req += "Connection: close" + CRLF;
	req += CRLF;
	
	return req;
    }
}

HttpResponse.java

/**
 * HttpResponse - Handle HTTP replies
 *
 * 
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpResponse {
    final static String CRLF = "\r\n";
    /** How big is the buffer used for reading the object */
    final static int BUF_SIZE = 8192;
    /** Maximum size of objects that this proxy can handle. For the
     * moment set to 100 KB. You can adjust this as needed. */
    final static int MAX_OBJECT_SIZE = 100000;
    /** Reply status and headers */
    String version;
    int status;
    String statusLine = "";
    String headers = "";
    /* Body of reply */
    byte[] body = new byte[MAX_OBJECT_SIZE];

    /** Read response from server. */
    public HttpResponse(DataInputStream fromServer) {
	/* Length of the object */
	int length = -1;
	boolean gotStatusLine = false;

	/* First read status line and response headers */
	try {
	    String line = /* Fill in */;
	    while (line.length() != 0) {
		if (!gotStatusLine) {
		    statusLine = line;
		    gotStatusLine = true;
		} else {
		    headers += line + CRLF;
		}

		/* Get length of content as indicated by
		 * Content-Length header. Unfortunately this is not
		 * present in every response. Some servers return the
		 * header "Content-Length", others return
		 * "Content-length". You need to check for both
		 * here. */
		if (line.startsWith(/* Fill in */) ||
		    line.startsWith(/* Fill in */)) {
		    String[] tmp = line.split(" ");
		    length = Integer.parseInt(tmp[1]);
		}
		line = fromServer.readLine();
	    }
	} catch (IOException e) {
	    System.out.println("Error reading headers from server: " + e);
	    return;
	}

	try {
	    int bytesRead = 0;
	    byte buf[] = new byte[BUF_SIZE];
	    boolean loop = false;

	    /* If we didn't get Content-Length header, just loop until
	     * the connection is closed. */
	    if (length == -1) {
		loop = true;
	    }
	    
	    /* Read the body in chunks of BUF_SIZE and copy the chunk
	     * into body. Usually replies come back in smaller chunks
	     * than BUF_SIZE. The while-loop ends when either we have
	     * read Content-Length bytes or when the connection is
	     * closed (when there is no Connection-Length in the
	     * response. */
	    while (bytesRead < length || loop) {
		/* Read it in as binary data */
		int res = /* Fill in */;
		if (res == -1) {
		    break;
		}
		/* Copy the bytes into body. Make sure we don't exceed
		 * the maximum object size. */
		for (int i = 0; 
		     i < res && (i + bytesRead) < MAX_OBJECT_SIZE; 
		     i++) {
		    /* Fill in */
		}
		bytesRead += res;
	    }
 	} catch (IOException e) {
 	    System.out.println("Error reading response body: " + e);
 	    return;
 	}


    }

    /**
     * Convert response into a string for easy re-sending. Only
     * converts the response headers, body is not converted to a
     * string.
     */
    public String toString() {
	String res = "";

	res = statusLine + CRLF;
	res += headers;
	res += CRLF;
	
	return res;
    }
}

