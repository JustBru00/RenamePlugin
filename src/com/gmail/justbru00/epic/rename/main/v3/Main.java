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
import com.gmail.justbru00.epic.rename.commands.v3.Lore;
import com.gmail.justbru00.epic.rename.commands.v3.RemoveLoreLine;
import com.gmail.justbru00.epic.rename.commands.v3.Rename;
import com.gmail.justbru00.epic.rename.commands.v3.SetLoreLine;
import com.gmail.justbru00.epic.rename.enums.v3.MCVersion;
import com.gmail.justbru00.epic.rename.listeners.v3.OnJoin;
import com.gmail.justbru00.epic.rename.main.v3.Metrics.Graph;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.PluginFile;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	public static boolean debug = false;
	public static String PLUGIN_VERISON = null;
	public static boolean USE_NEW_GET_HAND = true; // Default to the post 1.9.x
													// get in hand item method.
	public static MCVersion MC_VERSION; // Version is set in
										// #checkServerVerison()
	public static Main plugin;
	public static PluginFile messages = null;
	public static Economy econ = null; // Vault economy.
	public static boolean USE_ECO = false;
	public static boolean AUTO_UPDATE = true; // For the SpigetUpdater (Issue
												// #45)
	public static final int CONFIG_VERSION = 1;
	public static final int MESSAGES_VERSION = 1;
	public static ConsoleCommandSender clogger = Bukkit.getServer().getConsoleSender();
	public static Logger log = Bukkit.getLogger();
	
	public static String prefix = Messager.color("&8[&bEpic&fRename&8] &f");

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
		messages = new PluginFile(this, "messages.yml", "messages.yml");
		PLUGIN_VERISON = Main.getInstance().getDescription().getVersion();

		Messager.msgConsole("&bVersion: &c" + PLUGIN_VERISON + " &bMC Version: &c" + MC_VERSION.toString());
		Messager.msgConsole("&cThis plugin is Copyright (c) " + Calendar.getInstance().get(Calendar.YEAR)
				+ " Justin \"JustBru00\" Brubaker. This plugin is licensed under the MPL v2.0. "
				+ "You can view a copy of it at: http://bit.ly/2eMknxx");

		Messager.msgConsole("&aStarting plugin enable...");

		checkConfigVersions();

		if (Main.getInstance().getConfig().getBoolean("economy.use")) {
			USE_ECO = true;
			Messager.msgConsole("&aEconomy is enabled in the config.");
		}

		if (!setupEconomy()) {
			Messager.msgConsole(
					"&cVault not found! Disabling support for economy features. If you would like to use economy features download Vault at: "
							+ "http://dev.bukkit.org/bukkit-plugins/vault/");
			USE_ECO = false;
		}

		// Register Listeners
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(), this);

		// Command Executors
		getCommand("rename").setExecutor(new Rename());
		getCommand("epicrename").setExecutor(new EpicRename());
		getCommand("lore").setExecutor(new Lore());
		// TODO /saveitem (Version 3.1)
		// TODO /getitem (Version 3.1)
		getCommand("setloreline").setExecutor(new SetLoreLine());
		getCommand("removeloreline").setExecutor(new RemoveLoreLine());
		

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
		
		// Prefix 
		if (Main.getInstance().getConfig().getString("prefix") != null) {
			prefix = Messager.color(Main.getInstance().getConfig().getString("prefix"));
		} else {
			Messager.msgConsole("&cSorry the prefix in the config is null. Keeping default instead.");
		}
		Messager.msgConsole("&Prefix set to: '" + prefix + "'");

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
			Debug.send("Message in V3_Main.getMsgFromConfig() is NULL. Bugged path is: " + path);
			return "MESSAGE IS NULL FROM CONFIG.";
		}
		return Messager.color(messages.getString(path));
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
			Debug.send("Using methods for version 1.7 or 1.8");
		} else if ((Bukkit.getVersion().contains("1.9")) || (Bukkit.getVersion().contains("1.10"))
				|| (Bukkit.getVersion().contains("1.11"))) {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
			Debug.send("Using methods for version 1.9+");
		} else {
			USE_NEW_GET_HAND = true;
			MC_VERSION = MCVersion.NEWER_THAN_ONE_DOT_EIGHT;
			Debug.send("Server running unknown version. Assuming newer than 1.11");
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
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
