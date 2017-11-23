/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class FormattingPermManager {

	public static final String FORMAT_PERM = "epicrename.{CMD}.format.{CODE}";
	public static final String[] FORMAT_CODES = {"a","b","c","d","e","f","0","1","2","3","4","5","6","7","8","9","m","n","l","k","o"};

	/**
	 * Checks the provided players permissions for the color codes in their proposed text.
	 * @param erc The command this text will be used for.
	 * @param args The uncolored/unformatted arguments. (Still has '&' color codes)
	 * @param p The player to check permissions of.
	 * @return True if the player has permission. False if the player doesn't have permission.
	 */
	public static boolean checkPerms(EpicRenameCommands erc, String[] args, Player p) {
		StringBuilder builder = new StringBuilder("");				
		for (String item : args) {
			builder.append(item + " ");
		}
		return checkPerms(erc, builder.toString().trim(), p);
	}
	
	/**
	 * Checks the provided players permissions for the color codes in their proposed text.
	 * @param erc The command this text will be used for.
	 * @param unformattedString The uncolored/unformatted string. (Still has '&' color codes)
	 * @param p The player to check permissions of.
	 * @return True if the player has permission. False if the player doesn't have permission.
	 */
	public static boolean checkPerms(EpicRenameCommands erc, String unformattedString, Player p) {
		
		String allPerms = FORMAT_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc)).replace("{CODE}", "*");
		if (p.hasPermission(allPerms)) {
			Debug.send("[FormattingPermManager] The player has the permission: " + allPerms + " Bypassing individual code checks.");
			return true;
		}
		
		for (String code : FORMAT_CODES) {
			String perm = FORMAT_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc)).replace("{CODE}", code);
			if (unformattedString.toLowerCase().contains("&" + code)) {
				Debug.send("[FormattingPermManager] The string has the formatting code: " + code);
				if (!p.hasPermission(perm)) {
					Debug.send("[FormattingPermManager] The player doesn't have the permission: " + perm);
					Messager.msgPlayer(Main.getMsgFromConfig("format_code_permission.no_permission").replace("{code}", code), p);
					return false;
				}
			}
		}
		
		return true;
	}
}
