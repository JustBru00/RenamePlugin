package com.gmail.justbru00.epic.rename.main.v3;

import org.bukkit.Bukkit;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.enums.v3.MCVersion;
import com.gmail.justbru00.epic.rename.utils.Debug;

public class Main_NewRenameRewrite /** TODO Extend Java plugin**/{
	
	public static boolean debug = false;
	public static String PLUGIN_VERISON; // TODO Make final and get version from plugin.yml
	public static int RENAME_USES; // TODO Add one per successfull command usage. TODO Write to config. (Perferably in #onDisable())
	public static int LORE_USES; // TODO Add one per successfull command usage.	
	public static int RENAME_ENTITY_USES; // TODO Add one per successfull command usage. TODO Write to config. (Perferably in #onDisable())
	public static boolean USE_NEW_GET_HAND = true; // Default to the post 1.9.x get in hand item method.
	public static MCVersion MC_VERSION; // Version is set in #checkServerVerison()
	
	
	//@Override
	public static void onDisable() {
		
		
		// TODO Plugin Disabled Message.
	}
	
	/**
	 * 
	 * @param path Path to the message in messages.yml
	 * @return The colored string from messages.yml
	 */
	public static String getMsgFromConfig(String path) {
		// TODO Get string from config and color.
		return null;
	}
	
	public static void reloadConfigs() {
		// TODO Reload config.yml and messages.yml
	}
	
	public static void checkServerVerison() {
		// Check Server Version
				if ((Bukkit.getVersion().contains("1.7")) || (Bukkit.getVersion().contains("1.8"))) {
					USE_NEW_GET_HAND = false;
					MC_VERSION = MCVersion.OLDER_THAN_ONE_DOT_NINE;
					Debug.send("Using methods for version 1.7 or 1.8");
				} else if ((Bukkit.getVersion().contains("1.9")) || (Bukkit.getVersion().contains("1.10"))) {
					USE_NEW_GET_HAND = true;
					MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
					Debug.send("Using methods for version 1.9");
				} else {
					USE_NEW_GET_HAND = true;
					MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
					Debug.send("Using new get hand.");
				}	// End of Server Version Check
	}
	
	/**
	 * Add Command Stats
	 * @param erc The command that this method is coming from.
	 */
	public static void cmdAddStats(EpicRenameCommands erc) {
		
		try {	
			
			if (erc == EpicRenameCommands.RENAME) {
				RENAME_USES++;
				Debug.send("Added one to the total for /renamee");
				return;
			} else if (erc == EpicRenameCommands.LORE) {
				LORE_USES++;
				Debug.send("Added one to the total for /lore");
				return;
			} else if (erc == EpicRenameCommands.RENAMEENTITY) {
				RENAME_ENTITY_USES++;
				Debug.send("Added one to the total for /renameentity");
				return;
			} else {
				Debug.send("Incorrect command sent to #cmdAddStats()");
				return;
			}
			
		} catch(Exception e) {
			Debug.send("Number in per command stats is probably too large. Please check your config and see if that number is 2,147,483,648. If it is then you can reset it to 0."
					+ " Please email JustBru00 at justbru00@gmail.com when you reach this. I would love to know. :D");
		}
	}
	
}
