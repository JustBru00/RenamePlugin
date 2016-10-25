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

import com.gmail.justbru00.epic.rename.enums.v3.V3_EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.V3_RenameUtil;

public class V3_Rename implements CommandExecutor {
	
	public static final V3_EpicRenameCommands COMMAND = V3_EpicRenameCommands.RENAME;

	// VERSION 3
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if (command.getName().equalsIgnoreCase("rename")) { // Start /rename code
			if (sender instanceof Player) {
				
				Player player = (Player) sender; // Player is sender so do stuff :D
				
				if (player.hasPermission("epicrename.rename")) {				
					
					if (args.length >= 1) {					
						
							V3_RenameUtil.renameHandle(player, args, COMMAND);
							
							return true;								
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
