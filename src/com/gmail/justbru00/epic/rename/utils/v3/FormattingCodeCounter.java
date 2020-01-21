package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class FormattingCodeCounter {
	
	/**
	 * Checks if the given string has too many formatting codes.
	 * @return True if the max. is not reached. False if the max. has been reached.
	 */
	public static boolean checkMaxColorCodes(Player p, String valueToCheck, EpicRenameCommands cmd, boolean sendBypassMsg) {
		
		if (!Main.getInstance().getConfig().getBoolean("formatting_code_limit.enabled")) {
			Debug.send("[FormattingCodeCounter#checkMaxColorCodes] Formatting code limits are disabled.");
			return true;
		}
		
		if (p.hasPermission("epicrename.bypass.formattingcodemax")) {
			if (sendBypassMsg) {				
				if (!Main.getBooleanFromConfig("disable_bypass_messages")) { // Issue #107
					Messager.msgPlayer(Main.getMsgFromConfig("format_code_limit.bypass_max"), p);
				} else {
					Debug.send("Bypass messages are disabled.");
				} // End Issue #107
			}
			
			Debug.send("[FormattingCodeCounter#checkMaxColorCodes] Formatting code limit bypassed");			
			return true;
		}
		
		int numOfCodes = getAmountOfColorCodes(valueToCheck, '&');
		Debug.send("[FormattingCodeCounter#checkMaxColorCodes] Number of formatting codes: " + numOfCodes);
		
		if (numOfCodes > Main.getInstance().getConfig().getInt("formatting_code_limit." + EpicRenameCommands.getStringName(cmd) + ".max")) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Checks if the given string has too few formatting codes.
	 * @return True if the min. is reached. False if the min. has not been reached.
	 */
	public static boolean checkMinColorCodes(Player p, String valueToCheck, EpicRenameCommands cmd, boolean sendBypassMsg) {
		
		if (!Main.getInstance().getConfig().getBoolean("formatting_code_limit.enabled")) {
			Debug.send("[FormattingCodeCounter#checkMinColorCodes] Formatting code limits are disabled.");
			return true;
		}
		
		if (p.hasPermission("epicrename.bypass.formattingcodemin")) {
			if (sendBypassMsg) {				
				if (!Main.getBooleanFromConfig("disable_bypass_messages")) { // Issue #107
					Messager.msgPlayer(Main.getMsgFromConfig("format_code_limit.bypass_min"), p);
				} else {
					Debug.send("Bypass messages are disabled.");
				} // End Issue #107
			}
			Debug.send("[FormattingCodeCounter#checkMinColorCodes] Formatting code limit bypassed.");
			return true;
		}
		
		int numOfCodes = getAmountOfColorCodes(valueToCheck, '&');
		Debug.send("[FormattingCodeCounter#checkMinColorCodes] Number of formatting codes: " + numOfCodes);
		
		if (numOfCodes < Main.getInstance().getConfig().getInt("formatting_code_limit." + EpicRenameCommands.getStringName(cmd) + ".min")) {
			return false;
		}
		return true;
	}	
	
	/**
	 * Counts how many formatting codes are in the given String.
	 * @return The amount of formatting codes in the string.
	 */
	public static int getAmountOfColorCodes(String valueToCountCodesIn, char colorCodeChar) {
		int colorCodes = 0;
		char[] array = valueToCountCodesIn.toCharArray();
		
		for (int i = 0; i < array.length; i++) {
			
			if (array[i] == colorCodeChar) {
				// Might be a color code
				if (array.length != i + 1) { // Prevent error with color code character at end of string
					for(String s : FormattingPermManager.FORMAT_CODES) {
						if (String.valueOf(array[i+1]).equalsIgnoreCase(s)) {
							colorCodes++;
						}
					}
				}
			}			
		}
		
		return colorCodes;
	}
	
	/**
	 * Sends the minimum not reached message for the specified command.
	 * @param p
	 * @param erc
	 */
	public static void sendMinNotReachedMsg(Player p, EpicRenameCommands erc) {
		if (erc.equals(EpicRenameCommands.RENAME) || erc.equals(EpicRenameCommands.LORE)
				|| erc.equals(EpicRenameCommands.SETLORELINE) || erc.equals(EpicRenameCommands.INSERTLORELINE)) {
			String msgFromConfig = Main.getMsgFromConfig("format_code_limit.min_not_reached");
			FileConfiguration config = Main.getInstance().getConfig();
			int minimum = config.getInt("formatting_code_limit." + EpicRenameCommands.getStringName(erc) + ".min");
		
			msgFromConfig = msgFromConfig.replace("{min}", String.valueOf(minimum));
			Messager.msgPlayer(msgFromConfig, p);
		} else {
			Debug.send("[FormattingCodeCounter#sendMinNotReachedMsg] This method can only handle messages for RENAME, LORE, SETLORELINE, INSERTLORELINE."
					+ " config.yml version 8 only supports those commands.");
		}
	}
	
	/**
	 * Sends the maximum reached message for the specified command.
	 * @param p
	 * @param erc
	 */
	public static void sendMaxReachedMsg(Player p, EpicRenameCommands erc) {
		if (erc.equals(EpicRenameCommands.RENAME) || erc.equals(EpicRenameCommands.LORE)
				|| erc.equals(EpicRenameCommands.SETLORELINE) || erc.equals(EpicRenameCommands.INSERTLORELINE)) {
			String msgFromConfig = Main.getMsgFromConfig("format_code_limit.max_reached");
			FileConfiguration config = Main.getInstance().getConfig();
			int minimum = config.getInt("formatting_code_limit." + EpicRenameCommands.getStringName(erc) + ".max");
			
			msgFromConfig = msgFromConfig.replace("{max}", String.valueOf(minimum));
			Messager.msgPlayer(msgFromConfig, p);
		} else {
			Debug.send("[FormattingCodeCounter#sendMaxReachedMsg] This method can only handle messages for RENAME, LORE, SETLORELINE, INSERTLORELINE."
					+ " config.yml version 8 only supports those commands.");
		}
	}
	
}
