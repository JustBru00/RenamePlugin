/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.Old_CharLimit;

public class VaribleReplacer {
	
	// Varibles: {char} {version} 

	public static String replace(String toReplace) {
		
		toReplace = toReplace.replace("{char}", "" + Old_CharLimit.v3_getCharLimit()); // Make sure to use Version 3.0
		toReplace = toReplace.replace("{version}", Main.PLUGIN_VERISON);
		
		return toReplace;
	}
	
}
