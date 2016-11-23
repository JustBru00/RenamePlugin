/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.Messager;

public class OnJoin implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (e.getPlayer().getUniqueId().toString().equals("28f9bb08-b33c-4a7d-b098-ebf271383966"))  { // UUID = the UUID of plugin author do stuff.
			Messager.msgPlayer("&a&lThis server uses &b&lEpic&f&lRename &a&lversion &c&l" + Main.PLUGIN_VERISON, e.getPlayer());
		}
	}
	
}
