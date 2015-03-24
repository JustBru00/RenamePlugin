package justbru00.rename;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *******************************************
 * @author Justin Brubaker
 * Plugin name: EpicRename
 *
 *             Copyright (C) 2015 Justin Brubaker
 *
 *             This program is free software; you can redistribute it and/or
 *             modify it under the terms of the GNU General Public License as
 *             published by the Free Software Foundation; either version 2 of
 *             the License, or (at your option) any later version.
 *
 *             This program is distributed in the hope that it will be useful,
 *             but WITHOUT ANY WARRANTY; without even the implied warranty of
 *             MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *             General Public License for more details.
 *
 *             You should have received a copy of the GNU General Public License
 *             along with this program; if not, write to the Free Software
 *             Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 *             02110-1301 USA. 
 *             
 *             You can contact the author @ justbru00@gmail.com
 */

public class Rename extends JavaPlugin {

	public final Logger logger = Logger.getLogger("Minecraft");
	ConsoleCommandSender clogger = this.getServer().getConsoleSender();

	public String Prefix = color("&8[&bEpic&fRename&8] ");
	
	public static String color(String uncoloredstring) {
		String colored = uncoloredstring.replace('_', ' ');
		colored = ChatColor.translateAlternateColorCodes('&', colored);
		return colored;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String commandLabel, String[] args) {

		if (commandLabel.equalsIgnoreCase("rename")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (sender.hasPermission(new Permissions().rename)) {
					if (args.length == 1) {
						String coloredText = color(args[0]);

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

								clogger.sendMessage(Prefix + ChatColor.RED + player.getName()
										 + ChatColor
												.translateAlternateColorCodes(
														'&',
														getConfig().getString(
																"your msg"))
										+ coloredText);

								player.sendMessage(ChatColor
										.translateAlternateColorCodes(
												'&',
												getConfig().getString(
														"rename complete")));
								// end of command.
							} else {
								player.sendMessage(ChatColor
										.translateAlternateColorCodes(
												'&',
												getConfig()
														.getString(
																"item in hand is not a diamond pickaxe")));
							}
						} else {
							player.sendMessage(ChatColor
									.translateAlternateColorCodes(
											'&',
											getConfig().getString(
													"item in hand is air")));
						}
					} else {
						player.sendMessage(ChatColor
								.translateAlternateColorCodes(
										'&',
										getConfig().getString(
												"not enough or too many args")));
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', getConfig().getString("no permission")));
				}
			} else {
				clogger.sendMessage(Prefix + ChatColor.RED + "You can't use that command from CONSOLE.");
			}
		}
		if (commandLabel.equalsIgnoreCase("renameany")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (sender.hasPermission(new Permissions().renameany)) {
					if (args.length == 1) {
						String coloredText2 = color(args[0]);
						PlayerInventory pi2 = player.getInventory();
						ItemStack inHand2 = pi2.getItemInHand();
						if (player.getItemInHand().getType() != Material.AIR) {
							ItemStack newitem2 = new ItemStack(inHand2);
							ItemMeta im2 = inHand2.getItemMeta();
							im2.setDisplayName(coloredText2);
							newitem2.setItemMeta(im2);
							pi2.removeItem(inHand2);
							pi2.setItemInHand(newitem2);
							clogger.sendMessage(Prefix + ChatColor.RED + player.getName()
									 + ChatColor
											.translateAlternateColorCodes(
													'&',
													getConfig().getString(
															"your msg"))
									+ coloredText2);
								
							// end of command.
						} else {
							player.sendMessage(ChatColor
									.translateAlternateColorCodes(
											'&',
											getConfig().getString(
													"item in hand is air")));
						}
					} else {
						player.sendMessage(ChatColor
								.translateAlternateColorCodes(
										'&',
										getConfig().getString(
												"not enough or too many args")));
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', getConfig().getString("no permission")));
				}
			} else {
				clogger.sendMessage(ChatColor.RED + "You can't use that command from CONSOLE.");
			}
		}
		if (commandLabel.equalsIgnoreCase("lore")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission(new Permissions().lore)) {
					if (player.getItemInHand().getType() != Material.AIR) {
						if (args.length > 0) {
							int i = 0;
							ArrayList<String> lore = new ArrayList<String>();
							while (args.length > i) {
								lore.add(color(args[i]));
								i++;
							}
							ItemStack is = player.getItemInHand();
							ItemMeta im = is.getItemMeta();
							im.setLore(lore);
							is.setItemMeta(im);
							player.setItemInHand(is);
							player.sendMessage(ChatColor
									.translateAlternateColorCodes(
											'&',
											getConfig().getString(
													"lore complete")));
						} else {
							player.sendMessage(ChatColor
									.translateAlternateColorCodes('&',
											getConfig().getString("lore usage")));
						}
					} else {
						player.sendMessage(ChatColor
								.translateAlternateColorCodes('&', getConfig()
										.getString("item in hand is air")));
					}
				} else {
					player.sendMessage(ChatColor.translateAlternateColorCodes(
							'&', getConfig().getString("no permission")));
				}
			} else {
				clogger.sendMessage(Prefix + ChatColor.RED + "You can't use that command from CONSOLE.");
			}
		}

		return false;

	}

	// When console disables plugin
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		clogger.sendMessage(Prefix + ChatColor.RED + "Has Been Disabled.");

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

		clogger.sendMessage(Prefix + ChatColor.GOLD + "Version: "
				+ pdfFile.getVersion() + " Has Been Enabled.");

		getServer().getPluginManager().addPermission(new Permissions().rename);
		getServer().getPluginManager().addPermission(
				new Permissions().renameany);
		getServer().getPluginManager().addPermission(new Permissions().lore);
		getConfig().options().copyDefaults(true);
		saveConfig();

	}

}
