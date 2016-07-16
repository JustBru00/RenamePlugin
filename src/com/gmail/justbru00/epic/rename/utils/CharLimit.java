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

import com.gmail.justbru00.epic.rename.main.RenameRewrite;

public class CharLimit {	

	/**
	 * 
	 * @param checking String that we are checking.
	 * @param player Player who typed the command.
	 * @return False if ok. True if too many chars.
	 */
	public static boolean checkCharLimit(String checking, Player player) {
		
		if (!RenameRewrite.getInstance().getConfig().getBoolean(("charlimitenabled"))) {
			return false;
		}
		
		if (player.hasPermission("epicrename.bypass.charlimit")) {
			Messager.msgPlayer(player, RenameRewrite.getInstance().getConfig().getString("charlimitbypassmessage"));
			return false;
		}
		
		if (checking.length() > getCharLimit()) {
			return true;
		}
		
		return false;
	}
	
	public static int getCharLimit() {
		return RenameRewrite.getInstance().getConfig().getInt("charlimit");		
	}
}
