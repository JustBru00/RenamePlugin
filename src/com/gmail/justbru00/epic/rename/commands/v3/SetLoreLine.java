package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.Messager;

public class SetLoreLine implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("setloreline")) {
			if (sender.hasPermission("epicrename.setloreline")) {
				if (sender instanceof Player) {

					Player player = (Player) sender;
					
					if (args.length > 1) {
						
					} else {
						Messager.msgPlayer(Main.getMsgFromConfig(""), player); //TODO
						return true;
					}
				} else {
					Messager.msgSender(Main.getMsgFromConfig("setloreline.wrong_sender"), sender);
					return true;
				}
			} else {
				Messager.msgSender(Main.getMsgFromConfig("setloreline.no_permission"), sender);
				return true;
			}
		}
		return false;
	}

}
