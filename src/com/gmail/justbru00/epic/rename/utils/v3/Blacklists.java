/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;

public class Blacklists {
	
	// VERSION 3
	/**
	 * 
	 * @param m The Material from the {@link CommandExecutor}
	 * @return True if NO blacklisted material found. False if a blacklisted material is FOUND.
	 */
	public static boolean checkMaterialBlacklist(Material m) {
		
		// TODO Return true if check is okay | return false is blacklisted material is found.
		
		return true;
	}

	/**
	 * 
	 * @param args The arguments from the {@link CommandExecutor}
	 * @return True if NO blacklisted word found. False if a blacklisted word is FOUND.
	 */
	public static boolean checkTextBlacklist(String[] args) {
		
		// TODO Return true if check is okay | return false is blacklisted text is found.
		
		return true;
	}
	
}
