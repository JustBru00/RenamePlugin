/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.main.v3;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.justbru00.epic.rename.commands.v3.V3_EpicRename;
import com.gmail.justbru00.epic.rename.commands.v3.V3_Rename;
import com.gmail.justbru00.epic.rename.enums.v3.V3_EpicRenameCommands;
import com.gmail.justbru00.epic.rename.enums.v3.V3_MCVersion;
import com.gmail.justbru00.epic.rename.listeners.V3_OnJoin;
import com.gmail.justbru00.epic.rename.utils.Debug;
import com.gmail.justbru00.epic.rename.utils.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.V3_PluginFile;

public class V3_Main extends JavaPlugin{
	
	public static boolean debug = false;
	public static String PLUGIN_VERISON = null; 
	public static int RENAME_USES; // TODO Add one per successfull command usage. TODO Write to config. (Perferably in #onDisable())
	public static int LORE_USES; // TODO Add one per successfull command usage.	
	public static int RENAME_ENTITY_USES; // TODO Add one per successfull command usage. TODO Write to config. (Perferably in #onDisable())
	public static boolean USE_NEW_GET_HAND = true; // Default to the post 1.9.x get in hand item method.
	public static V3_MCVersion MC_VERSION; // Version is set in #checkServerVerison()
	public static V3_Main plugin;
	public static V3_PluginFile v3_messages = null;
	
	
	
	@Override
	public void onDisable() {
		
		
		Messager.msgConsole("&cPlugin Disabled.");
		plugin = null; // Fix memory leak.
	}
	
	@Override
	public void onEnable() {
		plugin = this;
		
		checkServerVerison();
		this.saveDefaultConfig();
		v3_messages = new V3_PluginFile(this, "v3_messages.yml", "v3_messages.yml");
		PLUGIN_VERISON = V3_Main.getInstance().getDescription().getVersion();
		
		Messager.msgConsole("&bVersion: &c" + PLUGIN_VERISON + " &bMC Version: &c" + MC_VERSION.toString());
		Messager.msgConsole("&cThis plugin is Copyright (c) " + Calendar.getInstance().get(Calendar.YEAR) + " Justin \"JustBru00\" Brubaker. This plugin is licensed under the MIT License. "
				+ "You can view a copy of it at: http://choosealicense.com/licenses/mit/"); 
				
		Messager.msgConsole("&aStarting plugin enable...");
		
		// TODO Check Economy in config.
		
		// Register Listeners
		Bukkit.getServer().getPluginManager().registerEvents(new V3_OnJoin(), this);
		
		// Command Executors
		getCommand("rename").setExecutor(new V3_Rename());
		getCommand("epicrename").setExecutor(new V3_EpicRename());	
		
		Messager.msgConsole("&aPlugin Enabled!");		
	}
	
	/**
	 * 
	 * @param path Path to the message in messages.yml
	 * @return The colored string from messages.yml
	 */
	public static String getMsgFromConfig(String path) {
		
		return Messager.color(v3_messages.getString(path));
	}
	
	 public static V3_Main getInstance() {
		 return plugin;
	 }
	
	public static void reloadConfigs() {
		getInstance().reloadConfig();
		v3_messages.reload();
	}	
	
	public static void checkServerVerison() {
		// Check Server Version
				if ((Bukkit.getVersion().contains("1.7")) || (Bukkit.getVersion().contains("1.8"))) {
					USE_NEW_GET_HAND = false;
					MC_VERSION = V3_MCVersion.OLDER_THAN_ONE_DOT_NINE;
					Debug.send("Using methods for version 1.7 or 1.8");
				} else if ((Bukkit.getVersion().contains("1.9")) || (Bukkit.getVersion().contains("1.10"))) {
					USE_NEW_GET_HAND = true;
					MC_VERSION = V3_MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
					Debug.send("Using methods for version 1.9+");
				} else {
					USE_NEW_GET_HAND = true;
					MC_VERSION = V3_MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
					Debug.send("Server running unknown version. Assuming newer than 1.10");
				}	// End of Server Version Check
	}
	
	/**
	 * Add Command Stats
	 * @param erc The command that this method is coming from.
	 */
	public static void cmdAddStats(V3_EpicRenameCommands erc) {
		
		try {	
			
			if (erc == V3_EpicRenameCommands.RENAME) {
				RENAME_USES++;
				Debug.send("Added one to the total for /renamee");
				return;
			} else if (erc == V3_EpicRenameCommands.LORE) {
				LORE_USES++;
				Debug.send("Added one to the total for /lore");
				return;
			} else if (erc == V3_EpicRenameCommands.RENAMEENTITY) {
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
