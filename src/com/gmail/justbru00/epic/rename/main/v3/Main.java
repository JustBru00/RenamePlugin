/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.main.v3;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.justbru00.epic.rename.commands.v3.EpicRename;
import com.gmail.justbru00.epic.rename.commands.v3.Export;
import com.gmail.justbru00.epic.rename.commands.v3.Glow;
import com.gmail.justbru00.epic.rename.commands.v3.Import;
import com.gmail.justbru00.epic.rename.commands.v3.InsertLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.Lore;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveGlow;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.Rename;
import com.gmail.justbru00.epic.rename.commands.v3.SetLoreLine;
import com.gmail.justbru00.epic.rename.configuration.ConfigurationManager;
import com.gmail.justbru00.epic.rename.enums.v3.MCVersion;
import com.gmail.justbru00.epic.rename.exploit_prevention.ExploitPreventionListener;
import com.gmail.justbru00.epic.rename.listeners.v3.OnJoin;
import com.gmail.justbru00.epic.rename.main.v3.Metrics.Graph;
import com.gmail.justbru00.epic.rename.main.v3.bstats.BStats;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.PluginFile;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	public static boolean debug = false;
	public static String PLUGIN_VERISON = null;
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
	
	/**
	 * Vault economy.
	 */
	public static Economy econ = null;
	public static boolean USE_ECO = false;
	public static boolean USE_XP_COST = false;
	
	public static boolean usesEpicRenameOnlineFeatures = false;

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
		
		this.saveDefaultConfig();
		messages = new PluginFile(this, "messages.yml", "messages.yml");
		PLUGIN_VERISON = Main.getInstance().getDescription().getVersion();
		
		checkServerVerison();

		Messager.msgConsole("&bVersion: &c" + PLUGIN_VERISON + " &bMC Version: &c" + MC_VERSION.toString());
		Messager.msgConsole("&cThis plugin is Copyright (c) " + Calendar.getInstance().get(Calendar.YEAR)
				+ " Justin \"JustBru00\" Brubaker. This plugin is licensed under the MPL v2.0. "
				+ "You can view a copy of it at: http://bit.ly/2eMknxx");

		Messager.msgConsole("&aStarting to enable plugin...");

		// ISSUE #113
		if (ConfigurationManager.doesConfigYmlNeedUpdated()) {
			Messager.msgConsole("&c[WARN] The config.yml file version is incorrect. EpicRename v" + PLUGIN_VERISON +
					" expects a config.yml version of " + ConfigurationManager.CONFIG_VERSION + 
					". Attempting to add missing values to the config file.");
			ConfigurationManager.updateConfigYml();
		}
		
		if (ConfigurationManager.doesMessagesYmlNeedUpdated()) {
			Messager.msgConsole("&c[WARN] The messages.yml file version is incorrect. EpicRename v" + PLUGIN_VERISON +
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

		// Start Metrics
		try {
			Metrics metrics = new Metrics(plugin);
			Graph ecoEnabledGraph = metrics.createGraph("Economy Features");
			ecoEnabledGraph.addPlotter(new Metrics.Plotter("Enabled") {
				@Override
				public int getValue() {
					if (Main.USE_ECO) {
						return 1; // True
					} else {
						return 0; // False
					}
				}
			});			
			ecoEnabledGraph.addPlotter(new Metrics.Plotter("Disabled") {
				@Override
				public int getValue() {
					if (Main.USE_ECO) {
						return 0; // True
					} else {
						return 1; // False
					}
				}
			});
			metrics.start();
		} catch (IOException e) {
			Messager.msgConsole("&cMCSTATS FAILED TO SUBMIT STATS.");
		}
		
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
				return Boolean.toString(Main.usesEpicRenameOnlineFeatures);
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
			MC_VERSION = MCVersion.OLDER_THAN_ONE_DOT_NINE;
			Debug.send("[Main#checkServerVersion()] Using methods for version 1.7 or 1.8");
		} else if ((version.contains("1.9")) || (version.contains("1.10"))
				|| (version.contains("1.11")) || version.contains("1.12") || 
				version.contains("1.13") || version.contains("1.14") ||
				version.contains("1.15")) {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
			Debug.send("[Main#checkServerVersion()] Using methods for version 1.9+");
		} else {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
			Messager.msgConsole("[Main#checkServerVersion()] Server running unknown version. Assuming newer than 1.13");
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
}
