package com.gmail.justbru00.epic.rename.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Watcher implements Listener{

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
	 if (e.getPlayer().getName().equalsIgnoreCase("JustBru00")) {
		 e.getPlayer().sendMessage(RenameRewrite.Prefix + RenameRewrite.color("&bThis Server uses EpicRename."));		 
	 }
	}	
}
