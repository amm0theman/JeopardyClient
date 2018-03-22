package jeopardyClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import jeopardyForms.JeopardyForm;

public class JeopardyClient {
	
	
	//*****************************************************************\\
	//							   MEMBERS 							   \\
	//_________________________________________________________________\\
	Socket socket = null;						//For connection	
	ObjectInputStream objInputStream = null;	//For receiving data from server	
	ObjectOutputStream objOutputStream = null;	//For writing to the server		
	DataInputStream datInputStream = null;		//For receiving data from server	
	DataOutputStream datOutputStream = null;	//For writing to the server	
	Integer playerID;							//To keep track of which player you are
	int[] playerDollars;						//Keep track of players money
	
	
	/*	Constructor
	 *	Receives: N/A
	 *	Returns: N/A
	 *	Simply calls the game loop
	 */	
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
	
	
	/*	Prints Results
	 *	Receives: N/A
	 *	Returns: N/A
	 *	Method prints the results of a finished Jeopardy game
	 */	
	public void printResults(int _numPlayers) {
		System.out.println("----------RESULTS----------");
		for (int i = 0; i < _numPlayers; i++) {
		System.out.println("Player " + (i+1) + ": " + playerDollars[i] + "$");
		}
		System.out.println("---------------------------\n\n");
		System.out.println("Enter to continue to the next game: ");
	}
	
	
	/*	Main game loop
	 *	Receives: N/A
	 *	Returns: N/A
	 *	This method controls everything
	 */	
	public void startGame() throws ClassNotFoundException {
			
		//*****************************************************************\\
		//							CONNECTION INIT 					   \\
		//_________________________________________________________________\\
		try {
			socket = new Socket("localhost", 5557);
			
			objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objInputStream = new ObjectInputStream(socket.getInputStream());
			datInputStream = new DataInputStream(socket.getInputStream());
			datOutputStream = new DataOutputStream(socket.getOutputStream()); 
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
		//-----------------------------------------------------------------
				
		//*****************************************************************\\
		//							PLAYER INIT 						   \\
		//_________________________________________________________________\\
		String tempString = "game not started";
		try {
			playerID = datInputStream.readInt();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("You are player: " + playerID);
		
		//GAME START POINT****************************************************
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
		
		//*****************************************************************\\
		//							SCORE INIT 							   \\
		//_________________________________________________________________\\
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
				
		//*****************************************************************\\
		//							THREAD INITS    					   \\
		//_________________________________________________________________\\
		boolean gameRunning = false;																//For loop control, checks to see if game is still going
		JeopardyForm newForm = null;																//Form to be grabbed from the server
		JeopardyGuessListener guessListener = null;													//Listens to other peoples guesses
		JeopardyAnswerListener answerListener = null;												//Listens to correct answers
		JeopardyGuesser jeopardyGuesser = new JeopardyGuesser(playerID, objOutputStream);			//Sends user guesses
		Thread guesserThread = null;																//Thread to send user guesses
		guessListener = new JeopardyGuessListener();												//init
		answerListener = new JeopardyAnswerListener(playerDollars, playerID, objOutputStream);		//init
		guesserThread = new Thread(jeopardyGuesser);												//init
		
		//*****************************************************************\\
		//							GAME LOOP		 					   \\
		//_________________________________________________________________\\
		while(!gameRunning) {

			//Get new form. If you can't, it means the game has ended
			try {
				newForm = (JeopardyForm) objInputStream.readObject();
			} 
			//If form wasn't caught, game has ended
			catch (IOException e) {
				System.out.println("Game has ended\n");
				printResults(playerDollars.length);	
				
				try {
					//Cleanup
					objOutputStream.close();
					objInputStream.close();
					datInputStream.close();
					datOutputStream.close();
					socket.close();
					
					//Join threads if they are alive
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
				//If correct answer update
				if(newForm.returnFormType() == 2)
					answerListener.update(newForm);
				//If incorrect guess update
				if(newForm.returnFormType() == 1)
					guessListener.update(newForm);	
			}
			
			//Start guessing thread if we haven't
			if(!guesserThread.isAlive())
				guesserThread.start();
		}
	}
}
