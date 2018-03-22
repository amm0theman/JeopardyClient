package jeopardyForms;

import java.io.Serializable;

public class AnswerForm implements JeopardyForm, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6923941578090068936L;
	public String theAnswer;
	public Integer playerID;
	public Integer dollarAmount;
	@Override
	public int returnFormType() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public String toString() {
		return "Player " + playerID + " was correct with the guess: " + theAnswer + ". Player wins " + dollarAmount + " dollars";
	}
	
	public int getDollarAmt() {
		return dollarAmount;
	}
}
