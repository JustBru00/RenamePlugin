package justbru00.rename;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *******************************************
 * DO NOT COPY WITHOUT PERMISSION. EpicRename Plugin: This plugin renames the
 * current held item. Look at the comments. Author: Justin Brubaker aka.
 * JustBru00. Date: 2/11/2015
 *
 * Copyright (C) 2015 Justin Brubaker
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 * *******************************************
 */

public class Rename extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		Player player = (Player) sender;

		if (commandLabel.equalsIgnoreCase("rename")) {
			if (sender.hasPermission(new Permissions().rename)) {
				if (args.length == 1) {
					String nonColoredText = args[0];
					String coloredText = ChatColor
							.translateAlternateColorCodes('&', nonColoredText);

					PlayerInventory pi = player.getInventory();
					ItemStack inHand = pi.getItemInHand();

					if (player.getItemInHand().getType() != Material.AIR) {
						if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {

							ItemStack newitem = new ItemStack(inHand);
							ItemMeta im = inHand.getItemMeta();
							im.setDisplayName(coloredText);
							newitem.setItemMeta(im);
							pi.removeItem(inHand);
							pi.setItemInHand(newitem);
							this.logger.info(player.getName() + " renamed a(n) item to: " + coloredText);
							//this.logger.info(player.getName()
							//		+ ChatColor.translateAlternateColorCodes(
							//				'&',
							//				getConfig().getString("your msg"))
							//		+ coloredText);
							//player.sendMessage(ChatColor
							//		.translateAlternateColorCodes(
							//				'&',
							//				getConfig().getString(
							//						"rename complete")));
						} else {
							player.sendMessage(ChatColor.RED + "Item in hand is not a diamond pickaxe!!!");
							//String error4 = getConfig().getString(
								//	"item in hand is not a diamond pickaxe");
						//	player.sendMessage(ChatColor
							//		.translateAlternateColorCodes('&', error4));
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "The Item in hand is air.");
						//String error3 = getConfig().getString(
						//		"item in hand is air");
						//player.sendMessage(ChatColor
						//		.translateAlternateColorCodes('&', error3));
					}
				} else {
					player.sendMessage(ChatColor.RED + "Please put one only one word. Like this: /rename &b&lTest");
					//String error2 = getConfig().getString(
					//		"not enough or too many args");
					//player.sendMessage(ChatColor.translateAlternateColorCodes(
					//		'&', error2));
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission.");
				//String error1 = getConfig().getString("no permission");
				//player.sendMessage(ChatColor.translateAlternateColorCodes('&',
				//		error1));
			}
		}
		if (commandLabel.equalsIgnoreCase("renameany")) {
			if (sender.hasPermission(new Permissions().renameany)) {
				if (args.length == 1) {
					String nonColoredText2 = args[0];
					String coloredText2 = ChatColor
							.translateAlternateColorCodes('&', nonColoredText2);
					PlayerInventory pi2 = player.getInventory();
					ItemStack inHand2 = pi2.getItemInHand();
					if (player.getItemInHand().getType() != Material.AIR) {
						ItemStack newitem2 = new ItemStack(inHand2);
						ItemMeta im2 = inHand2.getItemMeta();
						im2.setDisplayName(coloredText2);
						newitem2.setItemMeta(im2);
						pi2.removeItem(inHand2);
						pi2.setItemInHand(newitem2);
						this.logger.info(player.getName()
								+ " renamed a(n) item to: " + coloredText2);
						player.sendMessage(ChatColor.GREEN + "Item renamed.");

					} else {
						player.sendMessage(ChatColor.RED
								+ "The Item in hand is air.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Please put one only one word. Like this: /renameany &b&lTest");
					//String error2 = getConfig().getString(
					//		"not_enough_or_too_many_args");

					//player.sendMessage(ChatColor.translateAlternateColorCodes(
					//		'&', error2));
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission.");
				//String error1 = getConfig().getString("no_permission");

				//player.sendMessage(ChatColor.translateAlternateColorCodes('&',
					//	error1));
			}
		}		
		if (commandLabel.equalsIgnoreCase("lore")) {
			if (player.hasPermission(new Permissions().lore)) {
				if (player.getItemInHand().getType() != Material.AIR) {	
					if (args.length > 0) {
						int i = 0;
						ArrayList<String> lore = new ArrayList<String>();
						while (args.length > i) {						
						lore.add(ChatColor.translateAlternateColorCodes('&', args[i]));
						i++;
						}
						ItemStack is = player.getItemInHand();
						ItemMeta im = is.getItemMeta();
						im.setLore(lore);
						is.setItemMeta(im);
						player.setItemInHand(is);		
						player.sendMessage(ChatColor.GREEN + "Item Relored");
					} else {
						player.sendMessage(ChatColor.RED + "Please put at least one word after /lore");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Item in hand is air.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission.");
			}
		}
		return false;

	}

	// When console disables plugin
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled.");

		getServer().getPluginManager().removePermission(
				new Permissions().rename);
		getServer().getPluginManager().removePermission(
				new Permissions().renameany);
		getServer().getPluginManager().removePermission(new Permissions().lore);
	}

	// When console enables plugin
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version: "
				+ pdfFile.getVersion() + " Has Been Enabled.");

		getServer().getPluginManager().addPermission(new Permissions().rename);
		getServer().getPluginManager().addPermission(
				new Permissions().renameany);
		getServer().getPluginManager().addPermission(new Permissions().lore);
		// Next Version
		//getConfig().options().copyDefaults(true);
		//saveConfig();

	}

}
