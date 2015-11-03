package com.gmail.justbru00.epic.rename.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.justbru00.epic.rename.main.RenameRewrite;

public class EpicRename implements CommandExecutor {

	public RenameRewrite main;

	public EpicRename(RenameRewrite main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {	
		
		if (command.getName().equalsIgnoreCase("epicrename")) {			
			if (args.length == 0) {
				sender.sendMessage(RenameRewrite.Prefix + "Please type /epicrename help");
				sender.sendMessage(RenameRewrite.Prefix + "Or type /epicrename license");
				return true;
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("license")) {
					sender.sendMessage(RenameRewrite.Prefix + "See License Information at: http://tinyurl.com/epicrename1");
					return true;
				}
				if (args[0].equalsIgnoreCase("help")){ 
					sender.sendMessage(RenameRewrite.Prefix + ChatColor.GRAY + "---------------------------------------");
					sender.sendMessage(RenameRewrite.Prefix + "/rename - Usage: /rename &b&lTest");
					sender.sendMessage(RenameRewrite.Prefix + "/renameany - Usage: /renamyany &b&lTest");
					sender.sendMessage(RenameRewrite.Prefix + "/lore - Usage: /lore &bHello");
					sender.sendMessage(RenameRewrite.Prefix + "/renameentity - Usage: /renameentity &bTest");
					sender.sendMessage(RenameRewrite.Prefix + ChatColor.GRAY + "---------------------------------------");					
					return true;
				} else {
					sender.sendMessage(RenameRewrite.Prefix + "Please type /epicrename help");
				    return true;
				}				
			}
		} // End of command EpicRename
		return false;
	}

}
