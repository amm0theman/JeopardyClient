package jeopardyClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class JeopardyClient {
	Socket socket = null;
	ObjectInputStream objInputStream = null;
	ObjectOutputStream objOutputStream = null;
	boolean isConnected = false;
	
	public JeopardyClient() {
		
	}
	
	public void startGame() {
		while (!isConnected) {
			try {
				socket = new Socket("localHost", 5555);
				System.out.println("Connected");
				isConnected = true;
				objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			}
			catch (SocketException se ) {
				se.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
