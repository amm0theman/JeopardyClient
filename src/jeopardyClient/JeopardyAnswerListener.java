package jeopardyClient;

import java.io.IOException;
import java.io.ObjectOutputStream;

import jeopardyForms.AnswerForm;
import jeopardyForms.JeopardyForm;
import jeopardyForms.SkipForm;

public class JeopardyAnswerListener implements Runnable {
	
	AnswerForm newForm = new AnswerForm();
	int[] playerDollars = null;
	Integer dollarAmt = null;
	int playerID;
	ObjectOutputStream objOutputStream;
	
	JeopardyAnswerListener(int[] _playerDollars, int _playerID, ObjectOutputStream _objOutputStream) {
		playerDollars = _playerDollars;
		playerID = _playerID;
		objOutputStream = _objOutputStream;
	}
	
	public void update(JeopardyForm _newForm) {
		if(!newForm.equals(_newForm) && newForm.returnFormType() == 2) {
			newForm = (AnswerForm) _newForm;
			run();
		}
	}

	@Override
	public void run() {
			AnswerForm myAnswer = (AnswerForm) newForm;
			System.out.println(newForm.toString());
			playerDollars[myAnswer.playerID-1] += newForm.getDollarAmt();
			//if not the player who made the guess
			if(myAnswer.playerID != playerID) {
				//Tell your threads on the server to move on
				try {
					objOutputStream.writeObject(new SkipForm());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
	}

}
