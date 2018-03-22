package jeopardyClient;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import jeopardyForms.GuessForm;

public class JeopardyGuesser implements Runnable {
	
	Scanner myScan;
	int playerID;
	ObjectOutputStream objOutputStream;
	
	JeopardyGuesser(int _playerID, ObjectOutputStream _objOutputStream) {
		System.out.println("Init client guesser");
		myScan = new Scanner(System.in);
		playerID = _playerID;
		objOutputStream = _objOutputStream;
	}
	
	@Override
	public void run() {
		boolean runIt = true;
		while(runIt) {
			String myString = myScan.nextLine();
			GuessForm myForm = new GuessForm();
			myForm.playerID = playerID;
			myForm.theGuess = myString;
				try {
					objOutputStream.writeObject(myForm);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					runIt = false;
					System.out.println("Next game starting.\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					break;
				}
		}
		return;
	}

}
