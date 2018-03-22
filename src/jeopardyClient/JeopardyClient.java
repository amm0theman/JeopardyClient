package jeopardyClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import jeopardyForms.JeopardyForm;

public class JeopardyClient {
	Socket socket = null;
	ObjectInputStream objInputStream = null;
	ObjectOutputStream objOutputStream = null;
	DataInputStream datInputStream = null;
	DataOutputStream datOutputStream = null;
	Integer playerID;
	boolean isConnected = false;
	
	int[] playerDollars;
	
	public JeopardyClient() {
		while(true) {
			try {
				startGame();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void printResults(int _numPlayers) {
		System.out.println("----------RESULTS----------");
		for (int i = 0; i < _numPlayers; i++) {
		System.out.println("Player " + (i+1) + ": " + playerDollars[i] + "$");
		}
		System.out.println("---------------------------\n\n");
		System.out.println("Enter to continue to the next game: ");
	}
	
	public void startGame() throws ClassNotFoundException {
			
		//STAGE I.I -------------------------------------------------------
		//Initialize connection and data streams
		try {
					socket = new Socket("localhost", 5557);
					isConnected = true;
					
					objOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objInputStream = new ObjectInputStream(socket.getInputStream());
					datInputStream = new DataInputStream(socket.getInputStream());
					datOutputStream = new DataOutputStream(socket.getOutputStream()); 
		} catch(IOException e) {
			e.printStackTrace();
		}
		//-----------------------------------------------------------------
				
		//STAGE I.II -------------------------------------
		//Tell the player who they are and start the game
		//
		String tempString = "game not started";
		try {
			playerID = datInputStream.readInt();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("You are player: " + playerID);
		
		while(!tempString.equals("Game Started")) {
			try {
				tempString = datInputStream.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!tempString.equals("Game Started")) {
				System.out.println(tempString);
			}
		}
		//------------------------------------------------
		
		//STAGE I.III ------------------------------------
		//For tracking player dollar amounts
		int tempInt = 0;
		try {
			tempInt = datInputStream.readInt();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		playerDollars = new int[tempInt];
		for(int i = 0; i < playerDollars.length; i++) {
			playerDollars[i] = 0;
		}
				
		//STAGE II
		boolean gameRunning = false;
		JeopardyForm newForm = null;
		JeopardyGuessListener guessListener = null;
		JeopardyAnswerListener answerListener = null;
		JeopardyGuesser jeopardyGuesser = new JeopardyGuesser(playerID, objOutputStream);
		Thread guesserThread = null;
		guessListener = new JeopardyGuessListener();
		answerListener = new JeopardyAnswerListener(playerDollars, playerID, objOutputStream);
		guesserThread = new Thread(jeopardyGuesser);
		
		while(!gameRunning) {

			try {
				newForm = (JeopardyForm) objInputStream.readObject();
			} catch (IOException e) {
				System.out.println("Game has ended\n");
				printResults(playerDollars.length);				
				try {
					objOutputStream.close();
					objInputStream.close();
					datInputStream.close();
					datOutputStream.close();
					socket.close();
					if(guesserThread.isAlive()) {
						try {
							guesserThread.join();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (IOException ee) {
					ee.printStackTrace();
				}
			}
			
			//If question print out question and how much its worth
			//Otherwise update listeners
			if(newForm.returnFormType() == 0) {
				System.out.println(newForm.toString());
				System.out.println("Write your guess:");
			}
			else {
				if(newForm.returnFormType() == 2)
					answerListener.update(newForm);
				if(newForm.returnFormType() == 1)
					guessListener.update(newForm);	
			}
			
			//Start guessing if we haven't
			if(!guesserThread.isAlive())
				guesserThread.start();
		}
	}
}
