/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.main.v3;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.justbru00.epic.rename.commands.v3.V3_EpicRename;
import com.gmail.justbru00.epic.rename.commands.v3.V3_Rename;
import com.gmail.justbru00.epic.rename.enums.v3.V3_MCVersion;
import com.gmail.justbru00.epic.rename.listeners.V3_OnJoin;
import com.gmail.justbru00.epic.rename.utils.Debug;
import com.gmail.justbru00.epic.rename.utils.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.V3_PluginFile;

import net.milkbowl.vault.economy.Economy;

public class V3_Main extends JavaPlugin{
	
	public static boolean debug = false;
	public static String PLUGIN_VERISON = null; 
	public static boolean USE_NEW_GET_HAND = true; // Default to the post 1.9.x get in hand item method.
	public static V3_MCVersion MC_VERSION; // Version is set in #checkServerVerison()
	public static V3_Main plugin;
	public static V3_PluginFile v3_messages = null;
	public static Economy econ = null; // Vault economy.
	public static boolean USE_ECO = false;
	public static boolean AUTO_UPDATE = true; // For the SpigetUpdater (Issue #45)
	
	
	
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
		Messager.msgConsole("&cThis plugin is Copyright (c) " + Calendar.getInstance().get(Calendar.YEAR) + " Justin \"JustBru00\" Brubaker. This plugin is licensed under the MPL v2.0 license. "
				+ "You can view a copy of it at: http://bit.ly/2eMknxx"); 
				
		Messager.msgConsole("&aStarting plugin enable...");
		
		if (V3_Main.getInstance().getConfig().getBoolean("economy.use")) {
			USE_ECO = true;
			Messager.msgConsole("&aEconomy is enabled in the config.");
		}
		
		if (!setupEconomy()) {
			Messager.msgConsole("&cVault not found! Disabling support for economy features. If you would like to use economy features download Vault at: "
							+ "http://dev.bukkit.org/bukkit-plugins/vault/");
			USE_ECO = false;
		}
		
		// Register Listeners
		Bukkit.getServer().getPluginManager().registerEvents(new V3_OnJoin(), this);
		
		// Command Executors
		getCommand("rename").setExecutor(new V3_Rename());
		getCommand("epicrename").setExecutor(new V3_EpicRename());	
		// TODO /lore
		// TODO /saveitem
		// TODO /getitem
		// TODO /renameentity
		
		Messager.msgConsole("&aPlugin Enabled!");		
	}
	
	/**
	 * 
	 * @param path Path to the message in messages.yml
	 * @return The colored string from messages.yml
	 */
	public static String getMsgFromConfig(String path) {
		if (Messager.color(v3_messages.getString(path)) == null) Debug.send("Message in V3_Main.getMsgFromConfig() is NULL.");
		return Messager.color(v3_messages.getString(path));
	}
	
	 public static V3_Main getInstance() {
		 return plugin;
	 }
	
	public static void reloadConfigs() {
		getInstance().reloadConfig();
		v3_messages.reload();
		if (V3_Main.getInstance().getConfig().getBoolean("economy.use")) {
			USE_ECO = true;
			Messager.msgConsole("&aEconomy is enabled in the config.");
		}
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
	 * Sets up vault economy.
	 * @return
	 */
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
}
