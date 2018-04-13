/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.enums.v3;

public enum EpicRenameCommands {

	RENAME, LORE, EPICRENAME, SETLORELINE, 
	REMOVELORELINE, GLOW, REMOVEGLOW, ALIGN;
	
	
	public static String getStringName(EpicRenameCommands e) {
		switch (e) {
		case RENAME: {return "rename";}
		case LORE: {return "lore";}
		case EPICRENAME: {return "epicrename";}
		case SETLORELINE: {return "setloreline";}
		case REMOVELORELINE: {return "removeloreline";}
		case GLOW: {return "glow";}
		case REMOVEGLOW: {return "removeglow";}
		case ALIGN: {return "removeglow";}
		default:
			break;
		}
		return "Command not found";
	}
	
}
