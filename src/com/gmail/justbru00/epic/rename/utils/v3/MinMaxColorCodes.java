package com.gmail.justbru00.epic.rename.utils.v3;

public class MinMaxColorCodes {
	
	/**
	 * Checks if the given string has too many formatting codes.
	 * @return True if the max. is not reached. False if the max. has been reached.
	 */
	public static boolean checkMaxColorCodes(String valueToCheck) {
		
		return true;
	}
	/**
	 * Checks if the given string has too few formatting codes.
	 * @return True if the min. is reached. False if the min. has not been reached.
	 */
	public static boolean checkMinColorCodes(String valueToCheck) {
		return true;
	}
	
	/**
	 * Counts how many formatting codes are in the given String.
	 * @return The amount of formatting codes in the string.
	 */
	public static int getAmountOfColorCodes(String valueToCountCodesIn, char colorCodeChar) {
		int colorCodes = 0;
		char[] array = valueToCountCodesIn.toCharArray();
		
		for (int i = 0; i < array.length; i++) {
			
			if (array[i] == colorCodeChar) {
				// Might be a color code
				if (array.length != i + 1) { // Prevent error with color code character at end of string
					for(String s : FormattingPermManager.FORMAT_CODES) {
						if (String.valueOf(array[i+1]).equalsIgnoreCase(s)) {
							colorCodes++;
						}
					}
				}
			}			
		}
		
		return colorCodes;
	}
	
}
