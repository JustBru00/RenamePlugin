/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.Main;

public class Blacklists {

	// VERSION 3
	
	/**
	 * Issue #81
	 * Checks if the name of the item is one of the ones from the config.
	 * Useful to keep players from renaming important names.
	 * @param p The Player that we are checking for.
	 * @return True if the item can be edited. False if the item contains a blacklisted name.
	 */
	public static boolean checkExistingName(Player p) {
		Debug.send("[Blacklists#checkExistingName(Player)] Method called");
		
			if (RenameUtil.getInHand(p).getType() == Material.AIR || RenameUtil.getInHand(p) == null) {
				Debug.send("[Blacklists#checkExistingName(Player)] Item was AIR or NULL");
				return true;
			}
			
			String itemName = RenameUtil.getInHand(p).getItemMeta().getDisplayName();
			itemName = ChatColor.stripColor(itemName);
			
			if (itemName == null) {
				Debug.send("[Blacklists#checkExistingName(Player)] Item existing name was NULL");
				return true;
			}
			
			for (String blacklistedString : Main.getInstance().getConfig().getStringList("blacklists.existingname")) {
				if (blacklistedString != null) {
					
					blacklistedString = ChatColor.stripColor(Messager.color(blacklistedString));
					
					if (itemName.toLowerCase().contains(blacklistedString.toLowerCase())) {			
						Debug.send("[Blacklists#checkExistingName(Player)] Name contained '" + blacklistedString + "'");
						
						if (p.hasPermission("epicrename.bypass.existingname")) {
							// Player has bypass permission
							Debug.send("[Blacklists#checkExistingName(Player)] Player had the epicrename.bypass.existingname permission.");
							if (!Main.getBooleanFromConfig("disable_bypass_messages")) { // Issue #107
								Messager.msgPlayer(Main.getMsgFromConfig("blacklists.existingname.bypass"), p);
							} else {
								Debug.send("Bypass messages are disabled.");
							}
							return true;	
						} 							
						
						return false;						
					} 
				}				
			}
			
		return true;
	}
	
	/**
	 * Issue #81
	 * Checks if the lore of the item is one of the ones from the config file.
	 * Useful to keep players from changing important lore.
	 * @param p The Player that we are checking for.
	 * @return True if the item can be edited. False if the item contains a blacklisted lore string.
	 */
	public static boolean checkExistingLore(Player p) {
		Debug.send("[Blacklists#checkExistingLore(Player)] Method called");
		
		if (RenameUtil.getInHand(p).getType() == Material.AIR || RenameUtil.getInHand(p) == null) {
			Debug.send("[Blacklists#checkExistingLore(Player)] Item was AIR or NULL");
			return true;
		}
		
			List<String> loreLines = RenameUtil.getInHand(p).getItemMeta().getLore();
			
			if (loreLines == null) {
				Debug.send("[Blacklists#checkExistingLore(Player)] Lore from existing item was NULL");
				return true;
			}
			
			for (String loreLine : loreLines) {
				loreLine = ChatColor.stripColor(loreLine);
				
				for (String blacklistedString : Main.getInstance().getConfig().getStringList("blacklists.existingloreline")) {
					if (blacklistedString != null) {
					
						blacklistedString = ChatColor.stripColor(Messager.color(blacklistedString));
					
						if (loreLine.toLowerCase().contains(blacklistedString.toLowerCase())) {
							Debug.send("[Blacklists#checkExistingLore(Player)] Lore Line: '"+ loreLine +  "' contained '" + blacklistedString + "'");
							if (p.hasPermission("epicrename.bypass.existinglore")) {
								// Player has bypass permission
								Debug.send("[Blacklists#checkExistingLore(Player)] Player had the epicrename.bypass.existinglore permission.");
								if (!Main.getBooleanFromConfig("disable_bypass_messages")) { // Issue #107
									Messager.msgPlayer(Main.getMsgFromConfig("blacklists.existinglore.bypass"), p);
								} else {
									Debug.send("Bypass messages are disabled.");
								}
								return true;
							} 								
								
							return false;
						}
					}				
				}
			}	
		
		return true;
	}
	
	/**
	 * 
	 * @param m The Material from the {@link CommandExecutor}. This will also check is the player has the bypass permission. It will message the player.
	 * @return True if NO blacklisted material found. False if a blacklisted material is FOUND.
	 */
	public static boolean checkMaterialBlacklist(Material m, Player p) {
		
		// Issue #74
		if (p.hasPermission("epicrename.bypass.materialblacklist")) {
			Debug.send("Player just bypassed the material blacklist.");			
			if (!Main.getBooleanFromConfig("disable_bypass_messages")) { // Issue #107
				Messager.msgPlayer(Main.getMsgFromConfig("blacklists.material.bypass"), p);
			} else {
				Debug.send("Bypass messages are disabled.");
			}
			return true;
		}
		// End issue #74
		
		for (String s : Main.getInstance().getConfig().getStringList("blacklists.material")) {
			if (s != null) {
				if (m == Material.getMaterial(s)) {
					Debug.send("Material blacklist detected blacklisted material. (" + m.toString() + ")");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * @param args The arguments from the {@link CommandExecutor}. This will also check is the player has the bypass permission. It will message the player.
	 * @return True if NO blacklisted word found. False if a blacklisted word is FOUND.
	 */
	public static boolean checkTextBlacklist(String[] args, Player p) {
		
		// Issue #74
		if (p.hasPermission("epicrename.bypass.textblacklist")) {
			Debug.send("Player just bypassed the text blacklist.");
			if (!Main.getBooleanFromConfig("disable_bypass_messages")) { // Issue #107
				Messager.msgPlayer(Main.getMsgFromConfig("blacklists.text.bypass"), p);
			} else {
				Debug.send("Bypass messages are disabled.");
			}
			
			return true;
		}
		// End issue #74
		
		StringBuilder builder = new StringBuilder("");		
		String completeArgs = "";		
		
		for (String item : args) {
			builder.append(item + " ");
		}
		
		completeArgs = builder.toString().trim();
		if (Main.getInstance().getConfig().getBoolean("replace_underscores")) {
			completeArgs = completeArgs.replace("_", " ");
			Debug.send("Replaced the underscores.");
		}
		
		completeArgs = ChatColor.stripColor(Messager.color(completeArgs));
		
		for (String s : Main.getInstance().getConfig().getStringList("blacklists.text")) {
			if (s != null) {
				if (completeArgs.toLowerCase().contains(s.toLowerCase())) {
					Debug.send("Text blacklist has found blacklisted word. (" + s + ")");
					return false;
				}
			}
		}

		return true;
	}

}
