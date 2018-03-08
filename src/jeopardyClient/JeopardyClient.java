package jeopardyClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

import jeopardyForms.AnswerForm;
import jeopardyForms.GuessForm;
import jeopardyForms.JeopardyForm;
import jeopardyForms.QuestionForm;

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
		try {
			startGame();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void startGame() throws ClassNotFoundException {
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
				
				playerID = datInputStream.readInt();
				System.out.println("You are player: " + playerID);
				
				while(!tempString.equals("Game Started")) {
					tempString = datInputStream.readUTF();
					if(!tempString.equals("Game Started")) {
						System.out.println(tempString);
					}
				}
				//For tracking player dollar amounts
				int tempInt = datInputStream.readInt();
				playerDollars = new int[tempInt];
				for(int i = 0; i < playerDollars.length; i++) {
					playerDollars[i] = 0;
				}
				//For questions storage and loop control
				String question;
				boolean gameOver = false;
				boolean questionOver = false;
				Scanner myScan = new Scanner(System.in);
				
				//Stage two
				while(!gameOver) {
					question = datInputStream.readUTF();
					questionOver = false;
					int dollarAmt = 0;
					while(!questionOver) {
						//get next question or guess or correct answer
						JeopardyForm newForm = (JeopardyForm) objInputStream.readObject();
						
						if(newForm.returnFormType() == 0) {
							QuestionForm myForm = (QuestionForm) newForm;
							System.out.println(newForm.toString());
							dollarAmt = myForm.dollarAmt;
						}
						//Get different clients guess and print it plus incorrect
						if(newForm.returnFormType() == 1)
						{
							System.out.println(newForm.toString());
						}
						if(newForm.returnFormType() == 2)
						{
							AnswerForm myAnswer = (AnswerForm) newForm;
							System.out.println(newForm.toString());
							playerDollars[myAnswer.playerID] += dollarAmt;
							break;
						}
						System.out.println("Write your guess: ");
						String myString = myScan.nextLine();
						GuessForm myForm = new GuessForm();
						myForm.playerID = playerID;
						myForm.theGuess = myString;
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
