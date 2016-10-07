package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.Messager;

public class V3_EpicRename implements CommandExecutor {
	
	// VERSION 3

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// Start Command Handling | EpicRename
		if (command.getName().equalsIgnoreCase("epicrename")) {
			
			if (!sender.hasPermission("epicrename.epicrename")) { // Check Basic Permission
				Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.no_permission"), sender);
				return true;
			}
			
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("license")) { // Start /epicrename license
					Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.license"), sender);
					return true;
				} else if (args[0].equalsIgnoreCase("help")) { // End /epicrename license | Start /epicrename help
					Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.help"), sender);
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) { // End /epicrename license | Start /epicrename reload
					if (sender.hasPermission("epicrename.reload")) {
						V3_Main.reloadConfigs();
						Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.reload_success"), sender);
						return true;
					} else {
						Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.no_permission"), sender);
						return true;
					}					
				} else if (args[0].equalsIgnoreCase("debug")) { // End /epicrename reload | Start /epicrename debug
					if (sender.hasPermission("epicrename.epicrename.debug")) {
						if (V3_Main.debug) {
							V3_Main.debug = false;
							Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.debug_disable"), sender);
							return true;
						} else {
							V3_Main.debug = true;
							Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.debug_enable"), sender);
							return true;
						}
					} else {
						Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.no_permission"), sender);
						return true;
					}
				} else if (args[0].equalsIgnoreCase("version")) { // End /epicrename debug | Start /epicrename version
					Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.version"), sender); // TODO Replace {version} varible.
					return true;
				} else { // End /epicrename version | Start /epicrename invalid args
					Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.invalid_args"), sender);
					return true;
				}
			} else { // No Args				
				Messager.msgSender(V3_Main.getMsgFromConfig("epicrename.no_args"), sender);				
				return true;
			} //End No args
		} // End Command Handling | EpicRename
		
		return false;
	}

}
