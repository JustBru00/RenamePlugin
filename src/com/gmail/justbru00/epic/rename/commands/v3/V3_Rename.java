package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.V3_RenameUtil;

public class V3_Rename implements CommandExecutor {

	// VERSION 3
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (command.getName().equalsIgnoreCase("rename")) { // Start /rename code
			if (sender instanceof Player) {
				
				Player player = (Player) sender; // Player is sender so do stuff :D
				
				if (player.hasPermission("epicrename.rename")) {				
					
					if (args.length == 1) {
						// TODO Rename Item
						
					} else { // No Args
						Messager.msgPlayer(player, V3_Main.getMsgFromConfig("rename.no_args"));
						return true;						
					}
				} else { // No basic permission.
					Messager.msgPlayer(player, V3_Main.getMsgFromConfig("rename.no_permission"));
					return true;
				}
			} else { // Wrong sender
				Messager.msgSender(V3_Main.getMsgFromConfig("rename.wrong_sender"), sender);
				return true;
			}
		} // End /rename code
		
		return false;
	}

}
