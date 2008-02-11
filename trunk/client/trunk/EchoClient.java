/* EchoClient.java 
 * 
 * Usage: java EchoClient 192.168.0.3 4444
 *
 * */

import java.net.*;
import java.io.*;

public class EchoClient
{
  public static void main(String[] args)
  {
    //The client must have a host and port as arguments
	if (args.length != 2) {
      System.err.println("Usage: java EchoClient <host> <port>");
      System.exit(1);
    }
    try {
      Socket sock = new Socket(args[0], Integer.valueOf(args[1]).intValue());
      InputStream in = sock.getInputStream();
      OutputStream out = sock.getOutputStream();

      //Set timeout
      sock.setSoTimeout(300); 

      //Make a new thread
      OutputThread th = new OutputThread(in);
      th.start();

      //Loop for user inputs
      BufferedReader conin = new BufferedReader(
                             new InputStreamReader(System.in));
      String line = "";
      while (true) { 
        //Read input line
        line = conin.readLine();
        if (line.equalsIgnoreCase("QUIT")) {
          break;
        }
        //Send input line in return
        out.write(line.getBytes());
        out.write('\r');
        out.write('\n');
        //Wait for expidenture
        th.yield(); 
      }
      //Terminate program
      System.out.println("terminating output thread...");
      th.requestStop();
      th.yield();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
      in.close(); 
      out.close();
      sock.close();
    } catch (IOException e) {
      System.err.println(e.toString());
      System.exit(1);
    }
  }
}

class OutputThread
extends Thread
{
  InputStream in;
  boolean     stoprequested;

  public OutputThread(InputStream in)
  {
    super();
    this.in = in;
    stoprequested = false;
  }

  public synchronized void requestStop()
  {
    stoprequested = true;
  }

  public void run()
  {
    int len;
    byte[] b = new byte[100];
    try {
      while (!stoprequested) { 
        try {
          if ((len = in.read(b)) == -1) { 
            break;
          }
          System.out.write(b, 0, len);
        } catch (InterruptedIOException e) { 
          //Try again
        }
      }
    } catch (IOException e) {
      System.err.println("OutputThread: " + e.toString());
    }
  }
}