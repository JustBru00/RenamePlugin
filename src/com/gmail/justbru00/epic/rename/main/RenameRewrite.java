package com.gmail.justbru00.epic.rename.main;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *******************************************
 * @author Justin Brubaker Plugin name: EpicRename
 *
 *         Copyright (C) 2015 Justin Brubaker
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

public class RenameRewrite extends JavaPlugin {

	public static Economy econ = null;
	public Boolean useEconomy = false;
	public final Logger logger = Logger.getLogger("Minecraft");
	private ConsoleCommandSender clogger = this.getServer().getConsoleSender();
	public static String Prefix = color("&8[&bEpic&fRename&8] &f");

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (command.getName().equalsIgnoreCase("rename")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("epicrename.rename")) {
					ItemStack inHand = player.getItemInHand();
					if (args.length == 1) {
						if (inHand.getType() != Material.AIR) {
							if (inHand.getType() == Material.DIAMOND_PICKAXE) {
								if (useEconomy) {
									EconomyResponse r = econ.withdrawPlayer(player,	getConfig().getInt("economy.costs.rename"));
									if (r.transactionSuccess()) {
										player.sendMessage(String.format(Prefix	+ color("&6Withdrawed &a%s &6from your balance. Your current balance is now: &a%s"), econ.format(r.amount),	econ.format(r.balance)));
										player.setItemInHand(renameItemStack(player, args[0], inHand));
										clogger.sendMessage(Prefix + ChatColor.RED + player.getName() + ChatColor.translateAlternateColorCodes('&', getConfig()	.getString("your msg")) + color(args[0]));
										msg(player,	getConfig().getString("rename complete"));
										return true;
									} else {
										sender.sendMessage(String.format(Prefix	+ color("&6An error occured:&c %s"), r.errorMessage));
										return true;
									}
								}
								player.setItemInHand(renameItemStack(player, args[0], inHand));
								msg(player,	getConfig()	.getString("rename complete"));
								return true;
							} else {
								msg(player,	getConfig().getString("item in hand is not a diamond pickaxe"));
							}
						} else {
							msg(player,	getConfig().getString("item in hand is air"));
							return true;
						}
					} else {
						msg(player,	getConfig().getString("not enough or too many args"));
					}
				} else {
					msg(player, getConfig().getString("no permission"));
					return true;
				}
			}
		}

		return false;
	}

	public ItemStack renameItemStack(Player player, String displayname, ItemStack tobeRenamed) {
		ItemStack newitem = new ItemStack(tobeRenamed);
		ItemMeta im = tobeRenamed.getItemMeta();
		im.setDisplayName(color(displayname));
		newitem.setItemMeta(im);
		clogger.sendMessage(Prefix
				+ ChatColor.RED
				+ player.getName()
				+ ChatColor.translateAlternateColorCodes('&', getConfig()
						.getString("your msg")) + color(displayname));
		return newitem;
	}

	/**
	 * @param player
	 *            Player you want to msg
	 * @param msg
	 *            message.
	 */
	public static void msg(Player player, String msg) {
		if (msg == null) {
			Bukkit.broadcastMessage(Prefix
					+ color("&4ERROR: null message in msg()"));
		}
		player.sendMessage(Prefix + color(msg));
	}

	/**
	 * @param uncoloredstring
	 *            String with & color codes.
	 * @return Returns string with ChatColor.[colorhere] instead of &b ect.
	 */
	public static String color(String uncoloredstring) {
		String colored = uncoloredstring.replace('_', ' ');
		colored = ChatColor.translateAlternateColorCodes('&', colored);
		return colored;
	}

	@Override
	public void onDisable() {
		getServer().getPluginManager().removePermission(
				new Permissions().rename);
		getServer().getPluginManager().removePermission(
				new Permissions().renameany);
		getServer().getPluginManager().removePermission(new Permissions().lore);
		clogger.sendMessage(Prefix + ChatColor.RED + "Has Been Disabled.");

	}

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		clogger.sendMessage(color(Prefix
				+ "&bThis plugin is made by Justin Brubaker."));
		clogger.sendMessage(color(Prefix + "&bEpicRename version "
				+ pdfFile.getVersion()
				+ " is Copyright (C) 2015 Justin Brubaker"));
		clogger.sendMessage(color(Prefix
				+ "&bSee LICENSE infomation here: https://github.com/JustBru00/RenamePlugin/blob/master/Rename/src/LICENSE.txt"));
		this.saveDefaultConfig();
		Prefix = color(getConfig().getString("prefix"));
		clogger.sendMessage(color(Prefix
				+ "&6Prefix has been set to the one in the config."));
		getServer().getPluginManager().addPermission(new Permissions().rename);
		getServer().getPluginManager().addPermission(
				new Permissions().renameany);
		getServer().getPluginManager().addPermission(new Permissions().lore);

		if (getConfig().getBoolean("economy.use")) {
			useEconomy = true;
			clogger.sendMessage(Prefix + ChatColor.GOLD
					+ "Use economy in the config is true. Enabling Economy.");
		}
		if (!setupEconomy()) {
			clogger.sendMessage(Prefix
					+ color("&cVault not found disabling support for economy. If you would like economy download Vault at: "
							+ "http://dev.bukkit.org/bukkit-plugins/vault/"));
			useEconomy = false;
		}

		clogger.sendMessage(Prefix + ChatColor.GOLD + "Version: "
				+ pdfFile.getVersion() + " Has Been Enabled.");

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
}