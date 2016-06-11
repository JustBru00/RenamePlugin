/**
 *******************************************
 * @author Justin Brubaker Plugin name: EpicRename
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
package com.gmail.justbru00.epic.rename.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.main.RenameRewrite;
import com.gmail.justbru00.epic.rename.utils.CharLimit;
import com.gmail.justbru00.epic.rename.utils.Messager;

import net.milkbowl.vault.economy.EconomyResponse;

public class Rename implements CommandExecutor {
	
	public RenameRewrite main;

	public Rename(RenameRewrite main) {
		this.main = main;
	}
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (command.getName().equalsIgnoreCase("rename")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("epicrename.rename")) {
					ItemStack inHand = player.getItemInHand();
					if (args.length == 1) {
						
						// Check Text Blacklist
						if (main.checkBlacklist(args[0])) {
							Messager.msgPlayer(player, main.config.getString("found blacklisted word"));
							return true;
						}
						
						// Check Material Blacklist
						if (!main.checkMaterialBlacklist(player, inHand.getType())) {
							Messager.msgPlayer(player, main.config.getString("found blacklisted material"));
							return true;
						}
						
						// Check CharLimit
						CharLimit cl = new CharLimit();
						cl.ready(main);
						if (cl.checkCharLimit(args[0], player)) {
							//Too long
							Messager.msgPlayer(player, main.getConfig().getString("charlimitmessage"));
							return true;
						}
						
						// Check Format and Color permissions
						// Removing it for temp
						//if (!main.checkColorPermissions(player, args[0])) {
						//	RenameRewrite.msg(player, main.config.getString("color or blacklist permission not found"));
						//	return true;
						//}
						
						// Begin Command Code.
						if (inHand.getType() != Material.AIR) {
							if (player.hasPermission("epicrename.rename." + inHand.getType().toString()) || player.hasPermission("epicrename.rename.*")) {
								// Print correct permission
								if (RenameRewrite.debug) Messager.msgPlayer(player, "Correct Permission: " + "epicrename.rename." + inHand.getType().toString());
								if (main.useEconomy) {
									EconomyResponse r = RenameRewrite.econ.withdrawPlayer(player, main.config.getInt("economy.costs.rename"));
									if (r.transactionSuccess()) {
										player.sendMessage(String.format(RenameRewrite.Prefix + Messager.color("&6Withdrew &a%s &6from your balance. Your current balance is now: &a%s"), RenameRewrite.econ.format(r.amount),	RenameRewrite.econ.format(r.balance)));
										player.setItemInHand(main.renameItemStack(player, args[0], inHand));
										Messager.msgConsole(ChatColor.RED + player.getName() + ChatColor.translateAlternateColorCodes('&', main.config.getString("your msg")) + Messager.color(args[0]));
										Messager.msgPlayer(player, main.config.getString("rename complete"));
										return true;
									} else {
										sender.sendMessage(String.format(RenameRewrite.Prefix + Messager.color("&6An error occured:&c %s"), r.errorMessage));
										return true;
									}
								}
								player.setItemInHand(main.renameItemStack(player, args[0], inHand));
								Messager.msgPlayer(player, main.config.getString("rename complete"));
								return true;
							} else {
								Messager.msgPlayer(player, main.config.getString("you do not have permission for that item"));
								return true;
							}
						} else {
							Messager.msgPlayer(player, main.config.getString("item in hand is air"));
							return true;
						}
					} else {
						Messager.msgPlayer(player, main.config.getString("not enough or too many args"));
						return true;
					}
				} else {
					Messager.msgPlayer(player, main.config.getString("no permission"));
					return true;
				}
			} else {
				Messager.msgSender(Messager.color("&4Sorry you can't use that command."), sender);
				return true;
			}			
		} // End of Command Rename.
		return false;
	}

}
