/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.main.v3;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.justbru00.epic.rename.commands.v3.AddLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.EpicRename;
import com.gmail.justbru00.epic.rename.commands.v3.Export;
import com.gmail.justbru00.epic.rename.commands.v3.Glow;
import com.gmail.justbru00.epic.rename.commands.v3.HideEnchantments;
import com.gmail.justbru00.epic.rename.commands.v3.Import;
import com.gmail.justbru00.epic.rename.commands.v3.InsertLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.Lore;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveGlow;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveLore;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveName;
import com.gmail.justbru00.epic.rename.commands.v3.Rename;
import com.gmail.justbru00.epic.rename.commands.v3.SetLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.UnHideEnchantments;
import com.gmail.justbru00.epic.rename.configuration.ConfigurationManager;
import com.gmail.justbru00.epic.rename.enums.v3.MCVersion;
import com.gmail.justbru00.epic.rename.exploit_prevention.ExploitPreventionListener;
import com.gmail.justbru00.epic.rename.listeners.v3.OnJoin;
import com.gmail.justbru00.epic.rename.main.v3.bstats.BStats;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.PluginFile;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	public static boolean debug = false;
	public static String PLUGIN_VERSION = null;
	private static int BSTATS_PLUGIN_ID = 548;
	
	/**
	 * Default to the method for getting items in hand for MC version 1.9.x+
	 */
	public static boolean USE_NEW_GET_HAND = true; 
	
	/**
	 *  Version is set in checkServerVerison()
	 */
	public static MCVersion MC_VERSION;
	public static Main plugin;
	public static PluginFile messages = null;
	public static PluginFile statsFile = null;
	
	/**
	 * Vault economy.
	 */
	public static Economy econ = null;
	public static boolean USE_ECO = false;
	public static boolean USE_XP_COST = false;

	public static ConsoleCommandSender clogger = Bukkit.getServer().getConsoleSender();
	public static Logger log = Bukkit.getLogger();
	
	public static String prefix = Messager.color("&8[&bEpic&fRename&8] &f");

	@Override
	public void onDisable() {
		Messager.msgConsole("&cPlugin Disabled.");
		plugin = null; // Fix memory leak - maybe?
	}

	@Override
	public void onEnable() {
		plugin = this;
		
		// Save default yaml files.
		this.saveDefaultConfig();
		messages = new PluginFile(this, "messages.yml", "messages.yml");
		statsFile = new PluginFile(this, "stats.yml"); // Issue #162
		
		PLUGIN_VERSION = Main.getInstance().getDescription().getVersion();
		
		checkServerVerison();

		Messager.msgConsole("&bVersion: &c" + PLUGIN_VERSION + " &bMC Version: &c" + MC_VERSION.toString());
		Messager.msgConsole("&cThis plugin is Copyright (c) 2022 Justin \"JustBru00\" Brubaker. This plugin is licensed under the MPL v2.0. "
				+ "You can view a copy of the MPL v2.0 license at: http://bit.ly/2eMknxx");

		Messager.msgConsole("&aStarting to enable plugin...");

		// ISSUE #113
		if (ConfigurationManager.doesConfigYmlNeedUpdated()) {
			Messager.msgConsole("&c[WARN] The config.yml file version is incorrect. EpicRename v" + PLUGIN_VERSION +
					" expects a config.yml version of " + ConfigurationManager.CONFIG_VERSION + 
					". Attempting to add missing values to the config file.");
			ConfigurationManager.updateConfigYml();
		}
		
		if (ConfigurationManager.doesMessagesYmlNeedUpdated()) {
			Messager.msgConsole("&c[WARN] The messages.yml file version is incorrect. EpicRename v" + PLUGIN_VERSION +
					" expects a messages.yml version of " + ConfigurationManager.MESSAGES_VERSION + 
					". Attempting to add missing values to the config file.");
			ConfigurationManager.updateMessagesYml();
		}		
		// END ISSUE #113

		if (Main.getInstance().getConfig().getBoolean("economy.use")) {
			USE_ECO = true;
			Messager.msgConsole("&aEconomy is enabled in the config.");
		}
		
		if (Main.getInstance().getConfig().getBoolean("xp.use")) {
			USE_XP_COST = true;
			Messager.msgConsole("&aExperience cost is enabled in the config.");
		}

		if (!setupEconomy()) {
			// Message now handled in setupEconomy()
			Messager.msgConsole("&cDisabling support for economy features.");
			USE_ECO = false;
		}

		// Register Listeners
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new OnJoin(), this);
		pm.registerEvents(new ExploitPreventionListener(), this);

		// Command Executors
		getCommand("rename").setExecutor(new Rename());
		getCommand("epicrename").setExecutor(new EpicRename());
		getCommand("lore").setExecutor(new Lore());		
		getCommand("setloreline").setExecutor(new SetLoreLine());
		getCommand("removeloreline").setExecutor(new RemoveLoreLine());
		getCommand("insertloreline").setExecutor(new InsertLoreLine());
		getCommand("glow").setExecutor(new Glow());
		getCommand("removeglow").setExecutor(new RemoveGlow());
		getCommand("import").setExecutor(new Import());;
		getCommand("export").setExecutor(new Export());
		getCommand("removename").setExecutor(new RemoveName());
		getCommand("removelore").setExecutor(new RemoveLore());
		getCommand("hideenchantments").setExecutor(new HideEnchantments());
		getCommand("unhideenchantments").setExecutor(new UnHideEnchantments());
		getCommand("addloreline").setExecutor(new AddLoreLine());
		
		// Start bstats
		BStats bstats = new BStats(this, BSTATS_PLUGIN_ID);
		
		bstats.addCustomChart(new BStats.SimplePie("economy_features", new Callable<String>() {
			
			@Override
	        public String call() throws Exception {
				return Boolean.toString(Main.USE_ECO);
	        }
			
		}));
		
		bstats.addCustomChart(new BStats.SimplePie("uses_epicrenameonline_features", new Callable<String>() {
			
			@Override
	        public String call() throws Exception {
				return Boolean.toString(Main.isEpicRenameOnlineFeaturesUsedBefore());
	        }
		
		}));	
		
		// ISSUE #152
		bstats.addCustomChart(new BStats.SimplePie("experience_cost_feature", new Callable<String>() {
			
			@Override
	        public String call() throws Exception {
				return Boolean.toString(Main.USE_XP_COST);
	        }
		
		}));
		
		// Prefix 
		if (Main.getInstance().getConfig().getString("prefix") != null) {
			prefix = Messager.color(Main.getInstance().getConfig().getString("prefix"));
		} else {
			Messager.msgConsole("&cThe prefix in the config is null. Keeping default instead.");
		}
		Messager.msgConsole("&aPrefix set to: '" + prefix + "&a'");

		Messager.msgConsole("&aPlugin Enabled!");
	}

	/**
	 * 
	 * @param path Path to the message in messages.yml
	 * @return The colored string from messages.yml
	 */
	public static String getMsgFromConfig(String path) {
		if (messages.getString(path) == null) {
			Debug.send("[Main#getMsgFromConfig()] A message from messages.yml was NULL. The path to the message is: " + path);
			return "[ERROR] Could not read value from messages.yml. Ask a server admin to enable /epicrename debug to find the broken value. [ERROR]";
		}
		return messages.getString(path); // Removed duplicate Messager.color(); Messager#msgXXXX colors the message.
	}
	
	public static boolean getBooleanFromConfig(String path) {
		return Main.getInstance().getConfig().getBoolean(path);
	}

	public static Main getInstance() {
		return plugin;
	}

	public static void reloadConfigs() {
		getInstance().reloadConfig();
		messages.reload();
		if (Main.getInstance().getConfig().getBoolean("economy.use")) {
			USE_ECO = true;
			Messager.msgConsole("&aEconomy is enabled in the config.");
		}
		
		if (Main.getInstance().getConfig().getBoolean("xp.use")) {
			USE_XP_COST = true;
			Messager.msgConsole("&aXp cost is enabled in the config.");
		}
		
		// ISSUE #125 - Prefix not correctly loaded by /epicrename reload
		if (Main.getInstance().getConfig().getString("prefix") != null) {
			prefix = Messager.color(Main.getInstance().getConfig().getString("prefix"));
			Messager.msgConsole("&aPrefix set to: '" + prefix + "&a'");	
		} else {
			Messager.msgConsole("&cThe prefix in the config is null. Keeping the prefix set as the default instead.");
		}			
		// END ISSUE #125
	}

	public static void checkServerVerison() {
		String version = Bukkit.getVersion();
		// Check Server Version
		if ((version.contains("1.7")) || (version.contains("1.8"))) {
			USE_NEW_GET_HAND = false;
			MC_VERSION = MCVersion.ONE_DOT_SEVEN_OR_NEWER;
			Debug.send("[Main#checkServerVersion()] Using methods for version 1.7 or 1.8");
			Messager.msgConsole("&c[CheckServerVersion] Server running 1.7 or 1.8. EpicRename will stop supporting these versions in the near future.");
		} else if ((version.contains("1.9")) || (version.contains("1.10"))
				|| (version.contains("1.11")) || version.contains("1.12")) {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.ONE_DOT_NINE_OR_NEWER;
			Messager.msgConsole("&c[CheckServerVersion] Server running 1.9-1.12. EpicRename will stop supporting these versions in the future.");
		} else if (version.contains("1.13") || version.contains("1.14") || version.contains("1.15")) {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.ONE_DOT_NINE_OR_NEWER;
			Messager.msgConsole("&c[CheckServerVersion] Server running 1.13-1.15. EpicRename may stop supporting these versions in the future.");
		} else {
			// Version 1.16 or newer
			// ISSUE #150 HEX COLOR CODES
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.ONE_DOT_SIXTEEN_OR_NEWER;
			Messager.msgConsole("&a[CheckServerVersion] Server running 1.16 or newer. Hex color code support has been enabled.");
		} // End of Server Version Check
	}

	/**
	 * Sets up vault economy.
	 * 
	 * @return
	 */
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			Messager.msgConsole("&cVault not found! If you would like to use economy features download Vault at: "
							+ "http://dev.bukkit.org/bukkit-plugins/vault/");
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			Messager.msgConsole("&cFailed to get the economy details from Vault. Is there a Vault compatible economy plugin installed?");
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	/**
	 * Gets the stack trace as a String from a Throwable
	 * @param aThrowable
	 * @return
	 */
	public static String getStackTrace(Throwable aThrowable) {
	    Writer result = new StringWriter();
	    PrintWriter printWriter = new PrintWriter(result);
	    aThrowable.printStackTrace(printWriter);
	    return result.toString();
	  }
	
	public static PluginFile getMessagesYmlFile() {
		return messages;
	}
	
	/**
	 * Issue #162
	 * Saves the given value to the stats.yml file.
	 * Will only actually save the value to the file if it is different than the value already set in the file.
	 * This is to prevent any extra IO calls.
	 * @param The boolean value of EpicRenameOnline feature use.
	 */
	public static void setEpicRenameOnlineFeaturesUsedBefore(boolean value) {
		if (statsFile != null) {
			if (statsFile.isBoolean("epicrenameonline_features_used_before")) {
				if (statsFile.getBoolean("epicrenameonline_features_used_before") != value) {
					statsFile.set("epicrenameonline_features_used_before", value);
					statsFile.save();
				}
			}			
		} else {
			Debug.send("[Main#setEpicRenameOnlineFeaturesUsedBefore] stats.yml file is null. Are you sure it was able to be saved during onEnable?");
		}
	}
	
	/**
	 * Issue #162
	 * Reads the current boolean value of the key epicrenameonline_features_used_before from stats.yml
	 * @return The boolean value.
	 */
	public static boolean isEpicRenameOnlineFeaturesUsedBefore() {
		if (statsFile == null) {
			Debug.send("[Main#isEpicRenameOnlineFeaturesUsedBefore] stats.yml file is null. Are you sure it was able to be saved during onEnable?");
			return false;
		}
		
		if (statsFile.isBoolean("epicrenameonline_features_used_before")) {
			return statsFile.getBoolean("epicrenameonline_features_used_before");
		} else {
			statsFile.set("epicrenameonline_features_used_before", false);
			statsFile.save();
			return false;
		}
	}
}
