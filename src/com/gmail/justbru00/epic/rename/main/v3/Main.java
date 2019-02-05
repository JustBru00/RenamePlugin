/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.main.v3;

import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.justbru00.epic.rename.commands.v3.EpicRename;
import com.gmail.justbru00.epic.rename.commands.v3.Glow;
import com.gmail.justbru00.epic.rename.commands.v3.Lore;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveGlow;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.Rename;
import com.gmail.justbru00.epic.rename.commands.v3.SetLoreLine;
import com.gmail.justbru00.epic.rename.enums.v3.MCVersion;
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
	
	public static final int CONFIG_VERSION = 6;
	public static final int MESSAGES_VERSION = 8;
	public static ConsoleCommandSender clogger = Bukkit.getServer().getConsoleSender();
	public static Logger log = Bukkit.getLogger();
	
	public static String prefix = Messager.color("&8[&bEpic&fRename&8] &f");
	
	public static boolean enableMvdwPlaceholderApi = false;

	@Override
	public void onDisable() {
		Messager.msgConsole("&cPlugin Disabled.");
		plugin = null; // Fix memory leak.
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

		checkConfigVersions();

		if (Main.getInstance().getConfig().getBoolean("economy.use")) {
			USE_ECO = true;
			Messager.msgConsole("&aEconomy is enabled in the config.");
		}

		if (!setupEconomy()) {
			// Message now handled in setupEconomy()
			Messager.msgConsole("&cDisabling support for economy features.");
			USE_ECO = false;
		}

		// Register Listeners
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(), this);

		// Command Executors
		getCommand("rename").setExecutor(new Rename());
		getCommand("epicrename").setExecutor(new EpicRename());
		getCommand("lore").setExecutor(new Lore());		
		getCommand("setloreline").setExecutor(new SetLoreLine());
		getCommand("removeloreline").setExecutor(new RemoveLoreLine());
		getCommand("glow").setExecutor(new Glow());
		getCommand("removeglow").setExecutor(new RemoveGlow());
		// TODO Re add /align once it is working
		//getCommand("align").setExecutor(new Align());

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
		BStats bstats = new BStats(this);
		
		bstats.addCustomChart(new BStats.SimplePie("economy_features") {
			
			@Override
			public String getValue() {
				
				return Boolean.toString(Main.USE_ECO);
			}
		});
		
		
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
	 * @param path
	 *            Path to the message in messages.yml
	 * @return The colored string from messages.yml
	 */
	public static String getMsgFromConfig(String path) {
		if (messages.getString(path) == null) {
			Debug.send("[Main#getMsgFromConfig()] A message from messages.yml was NULL. The path to the message is: " + path);
			return "[ERROR] Message from config is null. Ask a server admin to enable /epicrename debug to find the broken value. [ERROR]";
		}
		return messages.getString(path); // Removed duplicate Messager.color(); Messager#msgXXXX colors the message.
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
	}

	public static void checkServerVerison() {
		// Check Server Version
		if ((Bukkit.getVersion().contains("1.7")) || (Bukkit.getVersion().contains("1.8"))) {
			USE_NEW_GET_HAND = false;
			MC_VERSION = MCVersion.OLDER_THAN_ONE_DOT_NINE;
			Debug.send("[Main#checkServerVersion()] Using methods for version 1.7 or 1.8");
		} else if ((Bukkit.getVersion().contains("1.9")) || (Bukkit.getVersion().contains("1.10"))
				|| (Bukkit.getVersion().contains("1.11")) || Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.13")) {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
			Debug.send("[Main#checkServerVersion()] Using methods for version 1.9+");
		} else {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
			Messager.msgConsole("[Main#checkServerVersion()] Server running unknown version. Assuming newer than 1.13");
		} // End of Server Version Check
	}

	public static void checkConfigVersions() {
		if (getInstance().getConfig().getInt("config_version") != CONFIG_VERSION) {
			Messager.msgConsole(
					"&cWARNING -> config.yml is outdated. Please delete it and restart the server. The plugin may not work as intended.");
		}

		if (messages.getInt("messages_yml_version") != MESSAGES_VERSION) {
			Messager.msgConsole(
					"&cWARNING -> messages.yml is outdated. Please delete it and restart the server. The plugin may not work as intended.");
		}
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

}
