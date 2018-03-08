package jeopardyClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class JeopardyClient {
	Socket socket = null;
	ObjectInputStream objInputStream = null;
	ObjectOutputStream objOutputStream = null;
	DataInputStream datInputStream = null;
	DataOutputStream datOutputStream = null;
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
				objInputStream = new ObjectInputStream(socket.getInputStream());
				datInputStream = new DataInputStream(socket.getInputStream());
				datOutputStream = new DataOutputStream(socket.getOutputStream());
				
				String tempString = null;
				
				while(!tempString.equals("Game Started")) {
					tempString = datInputStream.readUTF();
					System.out.println("Waiting on player: " + tempString);
				}
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
