/* EchoServer.java */

import java.net.*;
import java.io.*;

public class EchoServer
{
/*	private static int port = 7;
	
	//Allows the user to specify a different port to listen on
	public EchoServer(int listenPort) {
		port = listenPort;
	}
	*/
  public static void main(String[] args) throws Exception 
  {
	  int port = 4444;
    int cnt = 0; //The number associated with the latest thread
    try {
      System.out.println("Waiting for connection on port: " + port);
      ServerSocket echod = new ServerSocket(port);
      while (true) {
        Socket socket = echod.accept();
        (new EchoClientThread(++cnt, socket)).start();
      }
    } catch (IOException e) {
      System.err.println(e.toString());
      System.exit(1);
    }
  }
}

class EchoClientThread
extends Thread
{
  private int    name;
  private Socket socket;

  public EchoClientThread(int name, Socket socket)
  {
    this.name   = name;
    this.socket = socket;
  }

  public void run()
  {
    String msg = "EchoServer: Connection " + name;
    System.out.println(msg + " created");
    try {
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();
      out.write((msg + "\r\n").getBytes());
      int c;
      //As long as we don't read end of line
      while ((c = in.read()) != -1) {
        out.write((char)c);
        System.out.print((char)c);
      }
      System.out.println("Connection " + name + " were closed");
      socket.close();
    } catch (IOException e) {
      System.err.println(e.toString());
    }
  }
}