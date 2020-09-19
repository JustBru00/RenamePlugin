/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */ 
package com.gmail.justbru00.epic.rename.utils.v3;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.enums.v3.MCVersion;
import com.gmail.justbru00.epic.rename.main.v3.Main;

/**
 * 
 * @author Justin Brubaker
 *
 */
public class Messager {	
	
	private static final Pattern RGB_PATTERN = Pattern.compile("(&)?&#([0-9a-fA-F]{6})");
		
	public static String color(String uncolored) {
		if (Main.MC_VERSION == null) {
			return ChatColor.translateAlternateColorCodes('&', uncolored);	
		}
		
		if (Main.MC_VERSION.equals(MCVersion.ONE_DOT_SIXTEEN_OR_NEWER)) {
			return ChatColor.translateAlternateColorCodes('&', convertHexColorCodes(uncolored));		
		}
		
		return ChatColor.translateAlternateColorCodes('&', uncolored);		
	}
	
	/**
	 * ISSUE #150
	 * Converts hex color codes.
	 * @param uncolored
	 * @return
	 */
	public static String convertHexColorCodes(String uncolored) {
		StringBuffer builder = new StringBuffer();
		
		Matcher matcher = RGB_PATTERN.matcher(uncolored);
		
		while(matcher.find()) {
			boolean escaped = (matcher.group(1) != null);
			
			if (!escaped) {
				try {
					String hexColorCode = matcher.group(2);
					matcher.appendReplacement(builder, parseHexColor(hexColorCode));
					continue;
				} catch (NumberFormatException e) {
					//Ignore
				}				
			}
			matcher.appendReplacement(builder, "&#$2");
		}
		matcher.appendTail(builder);
		
		return builder.toString();
	}
	
    /**
     * @throws NumberFormatException If the provided hex color code is incorrect or if the version less than 1.16.
     */
    public static String parseHexColor(String hexColor) throws NumberFormatException {
        if (Main.MC_VERSION != MCVersion.ONE_DOT_SIXTEEN_OR_NEWER) {
            throw new NumberFormatException("RGB Colors only supported in 1.16 or newer");
        }

        if (hexColor.startsWith("#")) {
            hexColor = hexColor.substring(1);
        }
        
        if (hexColor.length() != 6) {
            throw new NumberFormatException("Invalid Length");
        }
        
        Color.decode("#" + hexColor);
        
        StringBuilder assembledColorCode = new StringBuilder();
        
        assembledColorCode.append("\u00a7x");
        
        for (char curChar : hexColor.toCharArray()) {
            assembledColorCode.append("\u00a7").append(curChar);
        }
        
        return assembledColorCode.toString();
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
