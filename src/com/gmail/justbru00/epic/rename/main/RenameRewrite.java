/**
 *******************************************
 * @author Justin Brubaker 
 * Plugin name: EpicRename
 *
 *         Copyright (C) 2016 Justin Brubaker
 *
 *         This program is free software; you can redistribute it and/or modify
 *         it under the terms of the GNU General Public License as published by
 *         the Free Software Foundation; either version 2 of the License, or (at
 *         your option) any later version.
 *
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 *
 *         You should have received a copy of the GNU General Public License
 *         along with this program; if not, write to the Free Software
 *         Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *         02110-1301 USA.
 * 
 *         You can contact the author @ justbru00@gmail.com
 */
package com.gmail.justbru00.epic.rename.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.justbru00.epic.rename.commands.EpicRename;
import com.gmail.justbru00.epic.rename.commands.Lore;
import com.gmail.justbru00.epic.rename.commands.Rename;
import com.gmail.justbru00.epic.rename.commands.RenameEntity;
import com.gmail.justbru00.epic.rename.utils.Messager;

public class RenameRewrite extends JavaPlugin {

	public static Economy econ = null;
	public Boolean useEconomy = false;
	public static Logger log = Bukkit.getLogger();
	public static ConsoleCommandSender clogger = Bukkit.getServer().getConsoleSender();
	public static String Prefix = Messager.color("&8[&bEpic&fRename&8] &f");
	public FileConfiguration config = getConfig();
	public List<String> blacklist;
	public String[] colorLetters = {"a", "b", "c", "d", "e", "f"};
	public boolean optOut = false;
	public List<String> materialBlacklist;
	public final String PLUGIN_VERSION = this.getDescription().getVersion();
	public final int RESOURCE_NUMBER = 4341;
	public static boolean debug = false;
	private static RenameRewrite plugin;
	public static boolean USE_NEW_GET_HAND;
		
	public static RenameRewrite getInstance() {
		return plugin;
	}
	
	@Override
	public void onEnable() {	
		plugin = this;
				
		this.saveDefaultConfig();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		Messager.msgConsole("&bThis plugin is made by Justin Brubaker.");
		Messager.msgConsole("&bThis plugin sends anonymous stats to mcstats.org.");
		Messager.msgConsole("&bTo disable change opt-out in the config to true.");
		Messager.msgConsole("&bEpicRename version " + pdfFile.getVersion() + " is Copyright (C) 2016 Justin Brubaker");
		Messager.msgConsole("&bSee LICENSE infomation here: http://tinyurl.com/epicrename1");
		
		// Check Server Version
		if ((Bukkit.getVersion().contains("1.7")) || (Bukkit.getVersion().contains("1.8"))) {
			USE_NEW_GET_HAND = false;
		} else if ((Bukkit.getVersion().contains("1.9")) || (Bukkit.getVersion().contains("1.10"))) {
			USE_NEW_GET_HAND = true;
		} else {
			USE_NEW_GET_HAND = false;
		}	// End of Server Version Check
		
		
		// Check for updates
		try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + RESOURCE_NUMBER).getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version != null) {            	
            	if (version.equalsIgnoreCase(PLUGIN_VERSION)) {
            		Messager.msgConsole("No Update Found.");
            	} else {
            		Messager.msgConsole("&6Found an update please download it at: https://www.spigotmc.org/resources/epicrename.4341/");
            	}
            }
        } catch (Exception ex) {
           Messager.msgConsole(Prefix + "&cFailed to check for a update on spigot.");
        }
		
		
		optOut = config.getBoolean("opt-out");
		
		// Start stats
		if (!optOut) {		
			try {
				Metrics metrics = new Metrics(this);
				metrics.start();
			} catch (IOException e) {
				// Failed to submit the stats :-(
			}
		}
		
		Prefix = Messager.color(config.getString("prefix"));
		Messager.msgConsole("&6Prefix has been set to the one in the config.");		

		if (config.getBoolean("economy.use")) {
			useEconomy = true;
			Messager.msgConsole(ChatColor.GOLD	+ "Use economy in the config is true. Enabling Economy.");
		}
		
		if (!setupEconomy()) {
			Messager.msgConsole("&cVault not found disabling support for economy. If you would like economy download Vault at: "
							+ "http://dev.bukkit.org/bukkit-plugins/vault/");
			useEconomy = false;
		}

		// Register Listener
		Bukkit.getServer().getPluginManager().registerEvents(new Watcher(), this);
		
		// Register Command Executors
		Bukkit.getPluginCommand("rename").setExecutor(new Rename(this));		
		Bukkit.getPluginCommand("lore").setExecutor(new Lore(this));
		Bukkit.getPluginCommand("renameentity").setExecutor(new RenameEntity(this));
		Bukkit.getPluginCommand("epicrename").setExecutor(new EpicRename(this));
		
		
		
		Messager.msgConsole(ChatColor.GOLD + "Version: " + PLUGIN_VERSION + " Has Been Enabled.");

	} //End of enable.
	
	/**
	 * @deprecated
	 * @param uncolored 
	 * @return colored string
	 */
	public static String color(String uncolored) {		
		return Messager.color(uncolored);
	}
	
	/**
	 * 
	 * @param player Player 
	 * @param material Material that needs checked
	 * @return True if ok False if not ok
	 */
	public boolean checkMaterialBlacklist(Player player, Material material) {
		if (player.hasPermission("epicrename.bypass.materialblacklist")) {
			return true;
		}
		this.materialBlacklist = config.getStringList("material blacklist");
		
		if (materialBlacklist == null) return false;
					
		int i = 0;
		while (i < blacklist.size()){
			Material temp = Material.getMaterial(materialBlacklist.get(i));
			
			if (temp == null) break;
			
			if (blacklist.get(i) == null) {
				break;
			} else if (material == temp) {				
				return false;
			}	
			
			i++;
		}
		
		return true;
	}
	
	public ItemStack renameItemStack(Player player, String displayname, ItemStack tobeRenamed) {
		ItemStack newitem = new ItemStack(tobeRenamed);
		ItemMeta im = tobeRenamed.getItemMeta();
		im.setDisplayName(Messager.color(displayname));
		newitem.setItemMeta(im);
		Messager.msgConsole(Prefix
				+ ChatColor.RED
				+ player.getName()
				+ ChatColor.translateAlternateColorCodes('&', config
						.getString("your msg")) + Messager.color(displayname));
		return newitem;		
	}
	
	/**
	 * 
	 * @param player 
	 * @param lore
	 * @param tobeRenamed
	 * @return new item with the name.
	 */
	public ItemStack renameItemStack(Player player, ArrayList<String> lore, ItemStack tobeRenamed) {
		ItemStack newitem = new ItemStack(tobeRenamed);
		ItemMeta im = tobeRenamed.getItemMeta();
		im.setLore(lore);
		newitem.setItemMeta(im);		
		return newitem;
	}

	
	@Override
	public void onDisable() {
		
		Messager.msgConsole(ChatColor.RED + "Has Been Disabled.");
		plugin = null;
	}



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
	
	/**
	 * 
	 * @param checking String to check for a blacklisted word.
	 * @return False if ok. True if found in blacklist
	 */
	public boolean checkBlacklist(String checking) {
		
		this.blacklist = config.getStringList("blacklist");
		
		int i = 0;
		while (i < blacklist.size()){
			if (blacklist.get(i) == null) {
				break;
			} else if (checking.toLowerCase().contains(blacklist.get(i).toLowerCase())) {				
				return true;
			}	
			
			i++;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return True if player can use the color. False if player can't
	 */
	public boolean checkColorPermissions(Player player, String proposedItemName) {
		boolean color = false;
		boolean format = false;
		
		// Check 0-9
		int i = 0;
		while (i < 10){
			if (proposedItemName.contains("&" + i)) {
				color = true;
			}
			i++;
		}
		
		// Check a-f
		i = 0;
		while (i < 6){
			if (proposedItemName.contains("&" + colorLetters[i])) {
				color = true;
			}
			i++;
		}
		
		// Check format
		if (proposedItemName.contains("&k")) format = true;
		if (proposedItemName.contains("&l")) format = true;
		if (proposedItemName.contains("&m")) format = true;
		if (proposedItemName.contains("&n")) format = true;
		if (proposedItemName.contains("&o")) format = true;
		if (proposedItemName.contains("&r")) format = true;
		
		
		if (color) {
			if (player.hasPermission("epicrename.color.*")) {				
				if (format) {
					if (player.hasPermission("epicrename.format.*")) {
						return true;
					} else {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}			
		}	
			return false;
	}
	
	
}