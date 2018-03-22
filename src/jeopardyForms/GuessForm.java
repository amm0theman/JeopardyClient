package jeopardyForms;

import java.io.Serializable;

public class GuessForm implements JeopardyForm, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String theGuess;
	public Integer playerID;
	
	@Override
	public int returnFormType() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public String toString() {
		System.out.println("\n\n\n\n\n\n\n\n");
		return "Player" + playerID + ": " + theGuess + " Incorrect\nWrite your guess: ";
	}
}
