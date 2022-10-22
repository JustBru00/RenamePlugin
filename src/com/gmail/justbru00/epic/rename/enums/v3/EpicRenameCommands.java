/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.enums.v3;

public enum EpicRenameCommands {

	RENAME, LORE, EPICRENAME, SETLORELINE, REMOVELORELINE, INSERTLORELINE, GLOW, REMOVEGLOW, ALIGN, EXPORT, IMPORT, 
	REMOVENAME, REMOVELORE, HIDEENCHANTMENTS;
	
	
	public static String getStringName(EpicRenameCommands e) {
		switch (e) {
		case RENAME: {return "rename";}
		case LORE: {return "lore";}
		case EPICRENAME: {return "epicrename";}
		case SETLORELINE: {return "setloreline";}
		case REMOVELORELINE: {return "removeloreline";}
		case INSERTLORELINE: {return "insertloreline";}
		case GLOW: {return "glow";}
		case REMOVEGLOW: {return "removeglow";}
		case ALIGN: {return "align";}
		case EXPORT: {return "export";}
		case IMPORT: {return "import";}
		case REMOVENAME: {return "removename";}
		case REMOVELORE: {return "returnlore";}
		case HIDEENCHANTMENTS: {return "hideenchantments";}
		default:
			break;
		}
		return "Command not found";
	}
	
}
