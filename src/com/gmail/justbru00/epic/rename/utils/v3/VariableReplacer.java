/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import com.gmail.justbru00.epic.rename.main.v3.Main;

public class VariableReplacer {
	
	// Variables: {char} {version} 

	public static String replace(String toReplace) {
		
		toReplace = toReplace.replace("{char}", "" + CharLimit.getCharLimit()); 
		toReplace = toReplace.replace("{version}", Main.PLUGIN_VERISON);
		
		return toReplace;
	}
	
}
