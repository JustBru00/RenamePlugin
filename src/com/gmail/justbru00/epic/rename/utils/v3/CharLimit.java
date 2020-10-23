/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.Main;

/**
 * This is currently only used by /rename
 */
public class CharLimit {

	
	/**
	 * @param checking The {@link String[]} that we are checking.
	 * @param player The {@link Player} who sent the command.
	 * @return TRUE if ok. FALSE if too many chars.
	 */
	public static boolean checkCharLimit(String[] checking, Player player) { // VERISON 3.0
		StringBuilder builder = new StringBuilder("");		
		String completeArgs = "";		
		
		for (String item : checking) {
			builder.append(item + " ");
		}
		
		completeArgs = builder.toString();
		completeArgs = ChatColor.stripColor(Messager.color(completeArgs));
		
		if (!Main.getInstance().getConfig().getBoolean("rename_character_limit.enabled")) {
			Debug.send("Character Limit is disabled.");
			return true;
		}
		
		if (player.hasPermission("epicrename.bypass.charlimit")) {
			Debug.send("Player bypassed char limit");			
			if (!Main.getBooleanFromConfig("disable_bypass_messages")) { // Issue #107
				Messager.msgPlayer(Main.getMsgFromConfig("rename_character_limit.bypass_msg"), player);
			} else {
				Debug.send("Bypass messages are disabled.");
			} // End Issue #107
			return true;
		}
		
		if (completeArgs.length() > getCharLimit()) {
			Debug.send("Player failed char limit.");
			return false;
		}
		
		return true;
	}
	
	public static int getCharLimit() {
		return Main.getInstance().getConfig().getInt("rename_character_limit.limit");
	}
}
