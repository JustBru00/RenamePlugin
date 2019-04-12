/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */ 
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.Main;

/**
 * 
 * @author Justin Brubaker
 *
 */
public class Messager {	


	public static String color(String uncolored){
		return ChatColor.translateAlternateColorCodes('&', uncolored);		
	}
	
	public static void msgConsole(String msg) {		
		msg = VariableReplacer.replace(msg);
		
		if (Main.clogger != null) {
		Main.clogger.sendMessage(Main.prefix + Messager.color(msg));		
		} else {
			Main.log.info(ChatColor.stripColor(Messager.color(msg)));
		}
	}
	
	public static void msgPlayer(String msg, Player player) {
		msg = VariableReplacer.replace(msg);
		player.sendMessage(Main.prefix + Messager.color(msg));
	}	
	/**
	 * Sends a message from the provided messages.yml path to the provided sender.
	 * @param msgPath
	 * @param sender
	 */
	public static void msgSenderWithConfigMsg(String msgPath, CommandSender sender) {
		String msg = Main.getMsgFromConfig(msgPath);
		msg = VariableReplacer.replace(msg);
		sender.sendMessage(Main.prefix + Messager.color(msg));
	}
	
	public static void msgSender(String msg, CommandSender sender) {
		msg = VariableReplacer.replace(msg);
		sender.sendMessage(Main.prefix + Messager.color(msg));
	}	
}
