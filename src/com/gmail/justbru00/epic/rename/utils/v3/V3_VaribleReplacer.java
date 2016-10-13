package com.gmail.justbru00.epic.rename.utils.v3;

import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.CharLimit;

public class V3_VaribleReplacer {
	
	// Varibles: {char} {version} 

	public static String replace(String toReplace) {
		
		toReplace = toReplace.replace("{char}", "" + CharLimit.v3_getCharLimit()); // Make sure to use Version 3.0
		toReplace = toReplace.replace("{version}", V3_Main.PLUGIN_VERISON);
		
		return toReplace;
	}
	
}
