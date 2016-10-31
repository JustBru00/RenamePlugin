package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.V3_LoreUtil;

public class V3_Lore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("lore")) { // Start /lore

			if (sender instanceof Player) {

				Player player = (Player) sender;
				
				if (player.hasPermission("epicrename.lore")) {
					
					if (args.length >= 1) {
						
						V3_LoreUtil.loreHandle(args, player);
						
						return true;
					} else {
						Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.no_args"), player);
						return true;
					}
					
				} else {
					Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.no_permission"), player);
					return true;
				}
				
			} else {
				Messager.msgSender(V3_Main.getMsgFromConfig("lore.wrong_sender"), sender);
				return true;
			}			
		} // End /lore
		
		return false;
	}
}
