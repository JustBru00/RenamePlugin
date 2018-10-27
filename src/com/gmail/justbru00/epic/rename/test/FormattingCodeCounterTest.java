package com.gmail.justbru00.epic.rename.test;

import com.gmail.justbru00.epic.rename.utils.v3.FormattingCodeCounter;

public class FormattingCodeCounterTest {

	public static void main(String[] args) {
		 if (test()) {
		System.out.println("TEST PASSED");
		 } else {
			 System.out.println("TEST FAILED");
		 }
	}
	/**
	 * 
	 * @return True if test is successful
	 */
	public static boolean test() {
		String test = "&b&lTesting the counter&. &k";
		int codes = 3;
		
		if (codes == FormattingCodeCounter.getAmountOfColorCodes(test, '&')) {
			return true;
		}
		
		return false;
	}
}
