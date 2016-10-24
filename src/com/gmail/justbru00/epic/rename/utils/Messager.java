/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */ 
package com.gmail.justbru00.epic.rename.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.RenameRewrite;
import com.gmail.justbru00.epic.rename.utils.v3.V3_VaribleReplacer;


public class Messager {

	public static String color(String uncolored){
		return ChatColor.translateAlternateColorCodes('&', uncolored);		
	}
	
	public static void msgConsole(String msg) {		
		msg = V3_VaribleReplacer.replace(msg);
		
		if (RenameRewrite.clogger != null) {
		RenameRewrite.clogger.sendMessage(RenameRewrite.Prefix + Messager.color(msg));		
		} else {
			RenameRewrite.log.info(ChatColor.stripColor(Messager.color(msg)));
		}
	}
	
	public static void msgPlayer(String msg, Player player) {
		msg = V3_VaribleReplacer.replace(msg);
		player.sendMessage(RenameRewrite.Prefix + Messager.color(msg));
	}	
	
	public static void msgPlayer(Player player, String msg) {
		msg = V3_VaribleReplacer.replace(msg);
		player.sendMessage(RenameRewrite.Prefix + Messager.color(msg));
	}	
	
	public static void msgSender(String msg, CommandSender sender) {
		msg = V3_VaribleReplacer.replace(msg);
		sender.sendMessage(RenameRewrite.Prefix + Messager.color(msg));
	}	
}
