/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

public class RemoveLoreLine implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("removeloreline")) {
			if (sender.hasPermission("epicrename.removeloreline")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (WorldChecker.checkWorld(player)) {
						if (args.length == 1) {
							int lineNumber = -1;

							try {
								lineNumber = Integer.parseInt(args[0]);
							} catch (Exception e) {
								Messager.msgPlayer(Main.getMsgFromConfig("removeloreline.not_an_int"), player);
								return true;
							}

							if (RenameUtil.getInHand(player).hasItemMeta()) {
								if (RenameUtil.getInHand(player).getType() == Material.AIR) {
									Messager.msgPlayer(Main.getMsgFromConfig("removeloreline.cannot_edit_air"), player);
									return true;
								}

								if (RenameUtil.getInHand(player).getItemMeta().hasLore()) {

									List<String> itemLore = RenameUtil.getInHand(player).getItemMeta().getLore();

									if (lineNumber < (itemLore.size() + 1)) { // Line
																				// number
																				// exists
										itemLore.remove((lineNumber - 1));
										ItemMeta im = RenameUtil.getInHand(player).getItemMeta();
										im.setLore(itemLore);
										RenameUtil.getInHand(player).setItemMeta(im);
										Messager.msgPlayer(Main.getMsgFromConfig("removeloreline.success"), player);
										return true;
									} else {
										Messager.msgPlayer(Main.getMsgFromConfig("removeloreline.out_of_bounds"),
												player);
										return true;
									}

								} else {
									Messager.msgPlayer(Main.getMsgFromConfig("removeloreline.has_no_lore"), player);
									return true;
								}
							}

						} else {
							Messager.msgPlayer(Main.getMsgFromConfig("removeloreline.wrong_args"), player);
							return true;
						}
					} else {
						Messager.msgSender(Main.getMsgFromConfig("removeloreline.disabled_world"), sender);
						return true;
					}
				} else {
					Messager.msgSender(Main.getMsgFromConfig("removeloreline.wrong_sender"), sender);
					return true;
				}
			} else {
				Messager.msgSender(Main.getMsgFromConfig("removeloreline.no_permission"), sender);
				return true;
			}
		}

		return false;
	}

}
