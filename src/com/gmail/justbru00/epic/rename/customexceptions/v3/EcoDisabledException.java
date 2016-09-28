package com.gmail.justbru00.epic.rename.customexceptions.v3;

public class EcoDisabledException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3066525035262548705L;

	
	//Parameterless Constructor
    public EcoDisabledException() {}

    //Constructor that accepts a message
    public EcoDisabledException(String message)
    {
       super(message);
    }
}
