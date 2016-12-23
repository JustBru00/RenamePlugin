/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;

public class EpicRename implements CommandExecutor {
	
	// VERSION 3

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// Start Command Handling | EpicRename
		if (command.getName().equalsIgnoreCase("epicrename")) {
			
			if (!sender.hasPermission("epicrename.epicrename")) { // Check Basic Permission
				Messager.msgSender(Main.getMsgFromConfig("epicrename.no_permission"), sender);
				return true;
			}
			
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("license")) { // Start /epicrename license
					Messager.msgSender(Main.getMsgFromConfig("epicrename.license"), sender);
					return true;
				} else if (args[0].equalsIgnoreCase("help")) { // End /epicrename license | Start /epicrename help
					for (String s : Main.messages.getStringList("epicrename.help")) {
						Messager.msgSender(s, sender);
					}
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) { // End /epicrename license | Start /epicrename reload
					if (sender.hasPermission("epicrename.epicrename.reload")) {
						Main.reloadConfigs();
						Messager.msgSender(Main.getMsgFromConfig("epicrename.reload_success"), sender);
						return true;
					} else {
						Messager.msgSender(Main.getMsgFromConfig("epicrename.no_permission"), sender);
						return true;
					}					
				} else if (args[0].equalsIgnoreCase("debug")) { // End /epicrename reload | Start /epicrename debug
					if (sender.hasPermission("epicrename.epicrename.debug")) {
						if (Main.debug) {
							Main.debug = false;
							Messager.msgSender(Main.getMsgFromConfig("epicrename.debug_disable"), sender);
							return true;
						} else {
							Main.debug = true;
							Messager.msgSender(Main.getMsgFromConfig("epicrename.debug_enable"), sender);
							return true;
						}
					} else {
						Messager.msgSender(Main.getMsgFromConfig("epicrename.no_permission"), sender);
						return true;
					}
				} else if (args[0].equalsIgnoreCase("version")) { // End /epicrename debug | Start /epicrename version
					Messager.msgSender(Main.getMsgFromConfig("epicrename.version"), sender); 
					return true;
				} else { // End /epicrename version | Start /epicrename invalid args
					Messager.msgSender(Main.getMsgFromConfig("epicrename.invalid_args"), sender);
					return true;
				}
			} else { // No Args				
				Messager.msgSender(Main.getMsgFromConfig("epicrename.no_args"), sender);				
				return true;
			} //End No args
		} // End Command Handling | EpicRename
		
		return false;
	}

}
