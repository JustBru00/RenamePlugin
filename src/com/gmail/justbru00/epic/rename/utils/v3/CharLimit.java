/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.Main;


public class CharLimit {

	
	/**
	 * 
	 * @param checking {@link String[]} that we are checking.
	 * @param player {@link Player} who typed the command.
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
			Debug.send("Char Limit is disabled.");
			return true;
		}
		
		if (player.hasPermission("epicrename.bypass.charlimit")) {
			Debug.send("Player bypassed char limit");
			Messager.msgPlayer(Main.getMsgFromConfig("rename_character_limit.bypass_msg"), player);
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
