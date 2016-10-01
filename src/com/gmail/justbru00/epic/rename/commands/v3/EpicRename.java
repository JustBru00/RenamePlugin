package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.justbru00.epic.rename.main.v3.Main_NewRenameRewrite;
import com.gmail.justbru00.epic.rename.utils.Messager;

public class EpicRename implements CommandExecutor {
	
	// VERSION 3

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// Start Command Handling | EpicRename
		if (command.getName().equalsIgnoreCase("epicrename")) {
			
			if (!sender.hasPermission("epicrename.epicrename")) { // Check Basic Permission
				Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.no_permission"), sender);
				return true;
			}
			
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("license")) { // Start /epicrename license
					Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.license"), sender);
					return true;
				} else if (args[0].equalsIgnoreCase("help")) { // End /epicrename license | Start /epicrename help
					Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.help"), sender);
					return true;
				} else if (args[0].equalsIgnoreCase("reload")) { // End /epicrename license | Start /epicrename reload
					if (sender.hasPermission("epicrename.reload")) {
						Main_NewRenameRewrite.reloadConfigs();
						Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.reload_success"), sender);
						return true;
					} else {
						Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.no_permission"), sender);
						return true;
					}					
				} else if (args[0].equalsIgnoreCase("debug")) { // End /epicrename reload | Start /epicrename debug
					if (sender.hasPermission("epicrename.epicrename.debug")) {
						if (Main_NewRenameRewrite.debug) {
							Main_NewRenameRewrite.debug = false;
							Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.debug_disable"), sender);
							return true;
						} else {
							Main_NewRenameRewrite.debug = true;
							Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.debug_enable"), sender);
							return true;
						}
					} else {
						Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.no_permission"), sender);
						return true;
					}
				} else if (args[0].equalsIgnoreCase("version")) { // End /epicrename debug | Start /epicrename version
					Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.version"), sender); // TODO Replace {version} varible.
					return true;
				} else { // End /epicrename version | Start /epicrename invalid args
					Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.invalid_args"), sender);
					return true;
				}
			} else { // No Args				
				Messager.msgSender(Main_NewRenameRewrite.getMsgFromConfig("epicrename.no_args"), sender);				
				return true;
			} //End No args
		} // End Command Handling | EpicRename
		
		return false;
	}

}
