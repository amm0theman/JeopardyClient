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
	String playerID;
	boolean isConnected = false;
	
	public JeopardyClient() {
		startGame();
	}
	
	public void startGame() {
		while (!isConnected) {
			try {
				//Stage one
				socket = new Socket("localHost", 5557);
				System.out.println("Connected");
				isConnected = true;
				objOutputStream = new ObjectOutputStream(socket.getOutputStream());
				objInputStream = new ObjectInputStream(socket.getInputStream());
				datInputStream = new DataInputStream(socket.getInputStream());
				datOutputStream = new DataOutputStream(socket.getOutputStream());
				
				String tempString = "game not started";
				
				playerID = datInputStream.readUTF();
				System.out.println("You are player: " + playerID);
				
				while(!tempString.equals("Game Started")) {
					tempString = datInputStream.readUTF();
					if(!tempString.equals("Game Started")) {
						System.out.println(tempString);
					}
				}
				
				String question;
				boolean gameOver = false;
				boolean questionOver = false;
				//Stage two
				while(!gameOver) {
					question = datInputStream.readUTF();
					questionOver = false;
					while(!questionOver) {
						//getForm
						if(guessForm)
						{
							//pring guess + incorrect
						}
						if(answerForm)
						{
							//print ID guessed answer correct won 200 dollars
							break;
							//sendAnswerForm
						}
					}
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
