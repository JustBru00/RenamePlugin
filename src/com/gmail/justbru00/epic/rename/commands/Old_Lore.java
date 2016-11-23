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

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.Old_RenameRewrite;
import com.gmail.justbru00.epic.rename.utils.Messager;

import net.milkbowl.vault.economy.EconomyResponse;

public class Old_Lore implements CommandExecutor {

	public Old_RenameRewrite main;

	public Old_Lore(Old_RenameRewrite main) {
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("lore")) {
			
			if (sender instanceof Player) {
				
				Player player = (Player) sender;
				
				if (player.hasPermission("epicrename.lore")) {
					
					if (args.length > 0) {
						
						if (main.useEconomy) { // Start Economy 
							
							if (player.hasPermission("epicrename.bypass.costs.*") || player.hasPermission("epicrename.bypass.costs.lore")) { // Start bypass
								
								Messager.msgPlayer(player, Old_RenameRewrite.getInstance().config.getString("economy.bypassmsg"));
								
								// Start Lore
								int i = 0;
								ArrayList<String> lore = new ArrayList<String>();
								
								while (args.length > i) {
									if (main.checkBlacklist(args[i])) {
										Messager.msgPlayer(main.config.getString("found blacklisted word"), player);
										return true;
									}

									lore.add(Messager.color(args[i]));
									i++;
								}
								// Check Material Blacklist
								if (!main.checkMaterialBlacklist(player, player.getItemInHand().getType())) {
									Messager.msgPlayer(main.config.getString("found blacklisted material"), player);
									return true;
								}
								player.setItemInHand(main.renameItemStack(player, lore, player.getItemInHand()));
								Messager.msgPlayer(player, main.config.getString("lore complete"));
								// End Lore
							} // End bypass
							
							EconomyResponse r = Old_RenameRewrite.econ.withdrawPlayer(player, main.getConfig().getInt("economy.costs.lore"));
							
							if (r.transactionSuccess()) { // Start Eco Success
								player.sendMessage(String.format(Old_RenameRewrite.Prefix + Messager.color("&6Withdrew &a%s &6from your balance. Your current balance is now: &a%s"),
										Old_RenameRewrite.econ.format(r.amount), Old_RenameRewrite.econ.format(r.balance)));
								
								// Start Lore
								int i = 0;
								ArrayList<String> lore = new ArrayList<String>();
								
								while (args.length > i) {
									if (main.checkBlacklist(args[i])) {
										Messager.msgPlayer(main.config.getString("found blacklisted word"), player);
										return true;
									}

									lore.add(Messager.color(args[i]));
									i++;
								}
								// Check Material Blacklist
								if (!main.checkMaterialBlacklist(player, player.getItemInHand().getType())) {
									Messager.msgPlayer(main.config.getString("found blacklisted material"), player);
									return true;
								}
								player.setItemInHand(main.renameItemStack(player, lore, player.getItemInHand()));
								Messager.msgPlayer(player, main.config.getString("lore complete"));
								// End Lore
								
								return true;
								
							} else { // End Eco Success | Start Eco Fail
								sender.sendMessage(
										String.format(Old_RenameRewrite.Prefix + Messager.color("&6An error occured:&c %s"),
												r.errorMessage));
								return true;
							} // End of Eco Fail
						} // End of economy
						
						int i = 0;
						ArrayList<String> lore = new ArrayList<String>();
						while (args.length > i) {
							if (main.checkBlacklist(args[i])) {
								Messager.msgPlayer(main.config.getString("found blacklisted word"), player);
								return true;
							}
							lore.add(Messager.color(args[i]));
							i++;
						}
						// Check Material Blacklist
						if (!main.checkMaterialBlacklist(player, player.getItemInHand().getType())) {
							Messager.msgPlayer(main.config.getString("found blacklisted material"), player);
							return true;
						}
						player.setItemInHand(main.renameItemStack(player, lore, player.getItemInHand()));
						Messager.msgPlayer(main.config.getString("lore complete"), player);
						return true;

					} else {
						Messager.msgPlayer(main.config.getString("not enough or too many args"), player);
						return true;
					}
				} else {
					Messager.msgPlayer(player, main.config.getString("no permission"));
					return true;
				}
			} else {
				Messager.msgSender(Messager.color("&4Sorry you can't use this command."), sender);
				return true;
			}
		} // End of command Lore
		return false;
	}

}
