package justbru00.rename;


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
*DO NOT COPY WITHOUT PERMISSION.
*EpicRename Plugin:
*This plugin renames the current held item.
*Look at the comments.
*Author: Justin Brubaker aka. JustBru00.
*Date: 2/11/2015
*
*Copyright (C) 2015 Justin Brubaker
*
*This program is free software; you can redistribute it and/or modify
*it under the terms of the GNU General Public License as published by
*the Free Software Foundation; either version 2 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License along
*with this program; if not, write to the Free Software Foundation, Inc.,
*51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
* *******************************************
*/ 
 


// Class declaration 
public class Rename extends JavaPlugin {
	// Console Messages
	public final Logger logger = Logger.getLogger("Minecraft");
	
		

	// On Command
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {
		Player player = (Player) sender;

		// if CommandLabel is rename (/rename)
		if (commandLabel.equalsIgnoreCase("rename")) {
			if (sender.hasPermission(new Permissions().rename)) {
				// if args is length 1
				if (args.length == 1) {
					// Non-Colored Text
					String nonColoredText = args[0];
					// Colored Text
					String coloredText = ChatColor
							.translateAlternateColorCodes('&', nonColoredText);
					// PlayerInventory (pi) Used later
					PlayerInventory pi = player.getInventory();
					// ItemStack inHand gets what player is holding (again used
					// later)
					ItemStack inHand = pi.getItemInHand();

					// Check if the player is not holding Air (nothing)
					if (player.getItemInHand().getType() != Material.AIR) {
						// Check if the player is holding a diamond pick
						if (player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
							// Make a new ItemStack that is the type of what
							// they are holding
							ItemStack newitem = new ItemStack(inHand);
							// Make a new ItemMeta (im) that = What the player
							// is holdings ItemMeta
							ItemMeta im = inHand.getItemMeta();
							// set (im) to coloredText (What the user typed in
							// /rename
							im.setDisplayName(coloredText);
							// Set newitems Meta to (im)
							newitem.setItemMeta(im);
							// Get rid of the Item the player is holding
							pi.removeItem(inHand);
							// Set what they are holding to be our new item
							pi.setItemInHand(newitem);
							// Print to console
							this.logger.info(player.getName()
									+ " renamed a(n) item to: " + coloredText);
							// Tell the player renamed
							player.sendMessage(ChatColor.GREEN
									+ "Item renamed.");
						} else
							// if player isn't holding a diamond pick
							player.sendMessage(ChatColor.RED
									+ "You can only rename a diamondpickaxe.");
					} else
						// if the player is holding AIR (nothing)
						player.sendMessage(ChatColor.RED
								+ "The Item in hand is air.");

				} else
					// if player types /rename with no text after it or player
					// types to many word like: /rename hi hi hello.
					player.sendMessage(ChatColor.RED
							+ "Please only put 1 word. Like this: &bT&6E&5S&1T or TEST");
			} else {
				String error1 = getConfig().getString("no_permission");
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', error1));
			}
		}
		if (commandLabel.equalsIgnoreCase("renameany")) {
			if (sender.hasPermission(new Permissions().renameany)) {
				// if args is length 1
				if (args.length == 1) {
					// Non-Colored Text
					String nonColoredText2 = args[0];
					// Colored Text
					String coloredText2 = ChatColor
							.translateAlternateColorCodes('&', nonColoredText2);
					// PlayerInventory (pi2) Used later
					PlayerInventory pi2 = player.getInventory();
					// ItemStack inHand2 gets what player is holding (again used
					// later)
					ItemStack inHand2 = pi2.getItemInHand();

					// Check if the player is not holding Air (nothing)
					if (player.getItemInHand().getType() != Material.AIR) {
						// Make a new ItemStack that is the type of what
						// they are holding
						ItemStack newitem2 = new ItemStack(inHand2);
						// Make a new ItemMeta (im2) that = What the player
						// is holdings ItemMeta
						ItemMeta im2 = inHand2.getItemMeta();
						// set (im2) to coloredText (What the user typed in
						// /rename
						im2.setDisplayName(coloredText2);
						// Set newitems Meta to (im2)
						newitem2.setItemMeta(im2);
						// Get rid of the Item the player is holding
						pi2.removeItem(inHand2);
						// Set what they are holding to be our new item
						pi2.setItemInHand(newitem2);
						// Print to console
						this.logger.info(player.getName()
								+ " renamed a(n) item to: " + coloredText2);
						// Tell the player renamed
						player.sendMessage(ChatColor.GREEN + "Item renamed.");

					} else
						// if the player is holding AIR (nothing)
						player.sendMessage(ChatColor.RED
								+ "The Item in hand is air.");

				} else
					// if player types /rename with no text after it or player
					// types to many word like: /rename hi hi hello.
					player.sendMessage(ChatColor.RED
							+ "Please only put 1 word. Like this: &bT&6E&5S&1T or TEST");
			} else {
				String error1 = getConfig().getString("no_permission");
		
				
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', error1));
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
	}

	// When console enables plugin
	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version: "
				+ pdfFile.getVersion() + " Has Been Enabled.");

		getServer().getPluginManager().addPermission(new Permissions().rename);
		getServer().getPluginManager().addPermission(new Permissions().renameany);
		getConfig().options().copyDefaults(true);
		saveConfig();
				
		
	}

}
