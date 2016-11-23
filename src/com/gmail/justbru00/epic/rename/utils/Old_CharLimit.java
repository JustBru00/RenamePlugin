/**
 *******************************************
 * @author Justin Brubaker Plugin name: EpicRename
 *
 *         Copyright (C) 2016 Justin Brubaker
 *
 *         This program is free software; you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation; either version 2 of the License, or (at
 *         your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with this program; if not, write to the Free Software
 *         Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *         02110-1301 USA.
 * 
 *         You can contact the author @ justbru00@gmail.com
 */
package com.gmail.justbru00.epic.rename.utils;

import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.Old_RenameRewrite;
import com.gmail.justbru00.epic.rename.main.v3.Main;

import net.md_5.bungee.api.ChatColor;

public class Old_CharLimit {	
	


	/**
	 * 
	 * @param checking String that we are checking.
	 * @param player Player who typed the command.
	 * @return False if ok. True if too many chars.
	 */
	public static boolean checkCharLimit(String checking, Player player) {
		
		if (!Old_RenameRewrite.getInstance().getConfig().getBoolean(("charlimitenabled"))) {
			return false;
		}
		
		if (player.hasPermission("epicrename.bypass.charlimit")) {
			Messager.msgPlayer(player, Old_RenameRewrite.getInstance().getConfig().getString("charlimitbypassmessage"));
			return false;
		}
		
		if (checking.length() > getCharLimit()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param checking {@link String[]} that we are checking.
	 * @param player {@link Player} who typed the command.
	 * @return TRUE if ok. FALSE if too many chars.
	 */
	public static boolean v3_checkCharLimit(String[] checking, Player player) { // VERISON 3.0
		StringBuilder builder = new StringBuilder("");		
		String completeArgs = "";		
		
		for (String item : checking) {
			builder.append(item + " ");
		}
		
		completeArgs = builder.toString();
		completeArgs = ChatColor.stripColor(Messager.color(completeArgs));
		
		if (!Main.getInstance().getConfig().getBoolean("character_limit.enabled")) {
			Debug.send("Char Limit is disabled.");
			return true;
		}
		
		if (player.hasPermission("epicrename.bypass.charlimit")) {
			Debug.send("Player bypassed char limit");
			Messager.msgPlayer(Main.getMsgFromConfig("character_limit.bypass_msg"), player);
			return true;
		}
		
		if (completeArgs.length() > v3_getCharLimit()) {
			Debug.send("Player failed char limit.");
			return false;
		}
		
		return true;
	}
	
	public static int v3_getCharLimit() {
		return Main.getInstance().getConfig().getInt("character_limit.limit");
	}
	
	public static int getCharLimit() {
		return Old_RenameRewrite.getInstance().getConfig().getInt("charlimit");		
	}
}
