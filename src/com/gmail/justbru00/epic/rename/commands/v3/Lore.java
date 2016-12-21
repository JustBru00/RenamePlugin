/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.LoreUtil;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;

public class Lore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("lore")) { // Start /lore

			if (sender instanceof Player) {

				Player player = (Player) sender;
				
				if (player.hasPermission("epicrename.lore")) {
					
					if (args.length >= 1) {
						
						LoreUtil.loreHandle(args, player);
						
						return true;
					} else {
						Messager.msgPlayer(Main.getMsgFromConfig("lore.no_args"), player);
						return true;
					}
					
				} else {
					Messager.msgPlayer(Main.getMsgFromConfig("lore.no_permission"), player);
					return true;
				}
				
			} else {
				Messager.msgSender(Main.getMsgFromConfig("lore.wrong_sender"), sender);
				return true;
			}			
		} // End /lore
		
		return false;
	}
}
