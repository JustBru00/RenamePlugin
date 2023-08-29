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
		toReplace = toReplace.replace("{version}", Main.PLUGIN_VERSION);		
		
		return toReplace;
	}
	
	/**
	 * Replaces {player}, {previous_name}, {new_name} and depreciated {name}
	 * @param toReplace
	 * @return
	 */
	public static String replaceRenameLogVariables(String toReplace, String playerUsername, String oldName, String completeArgs) {
		return replaceRenameSuccessVariables(toReplace.replace("{player}", playerUsername), oldName, completeArgs);
	}
	
	/**
	 * Replaces {previous_name}, {new_name} and depreciated {name}
	 * @param toReplace
	 * @param oldName
	 * @param completeArgs
	 * @return
	 */
	public static String replaceRenameSuccessVariables(String toReplace, String oldName, String completeArgs) {
		return toReplace.replace("{previous_name}", oldName).replace("{new_name}", completeArgs).replace("{name}", completeArgs);
	}
	
}
