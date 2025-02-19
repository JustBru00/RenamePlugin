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

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 
 * @author Justin Brubaker
 *
 */
public class Messager {	
	
	private static final Pattern RGB_PATTERN = Pattern.compile("(&)?&#([0-9a-fA-F]{6})");
	private static final Pattern X_PATTERN = Pattern.compile("&[xX]&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])&([A-Fa-f0-9])");
		
	public static String color(String uncolored) {
		if (Main.MC_VERSION == null) {
			return ChatColor.translateAlternateColorCodes('&', uncolored);	
		}
		
		if (Main.MC_VERSION.equals(MCVersion.ONE_DOT_SIXTEEN_OR_NEWER) || Main.MC_VERSION.equals(MCVersion.ONE_DOT_TWENTY_DOT_FIVE_OR_NEWER)) {
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
        if (Main.MC_VERSION != MCVersion.ONE_DOT_SIXTEEN_OR_NEWER && Main.MC_VERSION != MCVersion.ONE_DOT_TWENTY_DOT_FIVE_OR_NEWER) {
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
	
	public static void msgPlayerPlain(String msg, Player player) {
		msg = VariableReplacer.replace(msg);
		player.sendMessage(Main.prefix + msg);
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
	
	/**
	 * 
	 * @param uncoloredChatMessage The chat message to send.
	 * @param suggestedCommand The command to suggest with / included
	 * @param player The player to send the suggestion to.
	 */
	public static void sendCommandSuggestionToPlayer(String uncoloredChatMessage, String suggestedCommand, Player player) {
		String colored = Main.prefix + Messager.color(uncoloredChatMessage);
		TextComponent component = new TextComponent(TextComponent.fromLegacyText(colored));
		
		component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestedCommand));
		
		player.spigot().sendMessage(component);		
	}
	
	/**
	 * Code from: https://www.spigotmc.org/threads/reverse-translatealternatecolorcodes.453480/#post-3888824
	 * @author Schottky
	 * @param textToReverse
	 * @param altChar
	 * @return
	 */
	public static String reverseSectionSignTo(String textToReverse, char altChar) {
	    char[] chars = textToReverse.toCharArray();

	    for (int i = 0; i < chars.length; i++) {
	        if (chars[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(chars[i + 1]) > -1) {
	            chars[i] = altChar;
	            chars[i + 1] = Character.toLowerCase(chars[i + 1]);
	        }
	    }

	    return new String(chars);
	}
	
	/**
	 * Reverses text with &x&1&2&3&4&5&6 color codes back to &#123456
	 * Thanks to Elementeral for the code inspiration: https://www.spigotmc.org/threads/hex-color-code-translate.449748/#post-3867804
	 * @param textToReverse
	 * @return
	 */
	public static String reverseFromXToHex(String textToReverse) {
		
		Matcher matcher = X_PATTERN.matcher(textToReverse);
		StringBuffer buffer = new StringBuffer(textToReverse.length() + 4 * 8);
		while (matcher.find()) {
			String group1 = matcher.group(1);
			String group2 = matcher.group(2);
			String group3 = matcher.group(3);
			String group4 = matcher.group(4);
			String group5 = matcher.group(5);
			String group6 = matcher.group(6);
			matcher.appendReplacement(buffer, "&#" + 
			group1.charAt(0) + 
			group2.charAt(0) + 
			group3.charAt(0) + 
			group4.charAt(0) + 
			group5.charAt(0) + 
			group6.charAt(0));
		}
		return matcher.appendTail(buffer).toString();
	}
}
