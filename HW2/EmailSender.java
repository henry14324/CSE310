import java.io.*;
import java.net.*;

public class EmailSender
{
   public static void main(String[] args) throws Exception
   {
      // Establish a TCP connection with the mail server.
      String host = "sbmta1.cc.stonybrook.edu";
      int port = 25;
      Socket socket = new Socket(host, port);

      // Create a BufferedReader to read a line at a time.
      InputStream is = socket.getInputStream();
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);

      // Read greetings from the server.
      String response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("220")) {
         socket.close();
         throw new Exception("220 reply not received from server.");
      }

      // Get a reference to the socket's output stream.
      OutputStream os = socket.getOutputStream();

      // Send HELO command and get server response.
      String command = "HELO alice\r\n";
      System.out.print(command);
      os.write(command.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
        socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Send MAIL FROM command.
      String mailCmd = "MAIL FROM: <alice@crepes.fr>\r\n";
      System.out.print(mailCmd);
      os.write(mailCmd.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
        socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Send RCPT TO command.
      String RCPTCmd = "RCPT TO: henry.wang@stonybrook.edu\r\n";
      System.out.print(RCPTCmd);
      os.write(RCPTCmd.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
        socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Send DATA command.
      String dataCmd = "DATA\r\n";
      System.out.print(dataCmd);
      os.write(dataCmd.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("354")) {
        socket.close();
         throw new Exception("354 reply not received from server.");
      }

      // Send message data.
      String message = "SUBJECT: hello\nHi Henry, How's the weather? Alice.\r\n";
      System.out.print(message);
      os.write(message.getBytes("US-ASCII"));
      
      // End with line with a single period.
      String periodCmd = ".\r\n";
      System.out.print(periodCmd);
      os.write(periodCmd.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("250")) {
        socket.close();
         throw new Exception("250 reply not received from server.");
      }

      // Send QUIT command.
      String quitCmd = "QUIT\r\n";
      System.out.print(quitCmd);
      os.write(quitCmd.getBytes("US-ASCII"));
      response = br.readLine();
      System.out.println(response);
      if (!response.startsWith("221")) {
        socket.close();
         throw new Exception("221 reply not received from server.");
      }

      socket.close();
   }
}
