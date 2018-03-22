package jeopardyClient;

import jeopardyForms.*;

public class JeopardyGuessListener implements Runnable {

	JeopardyForm newForm;
	
	JeopardyGuessListener() {
		newForm = new GuessForm();
	}
	
	public void update(JeopardyForm _newForm) {
		if(!_newForm.equals(newForm)) {
			newForm = _newForm;
			run();
		}
	}
	
	@Override
	public void run() {
		System.out.println(newForm.toString());
	}
}
