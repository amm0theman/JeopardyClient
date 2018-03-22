package jeopardyForms;

import java.io.Serializable;

public class QuestionForm implements JeopardyForm, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1049592381584586834L;
	public String question;
	public int dollarAmt;
	
	@Override
	public int returnFormType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString( ) {
		return "Question: " + question + "\nWorth: " + dollarAmt;
	}
}
