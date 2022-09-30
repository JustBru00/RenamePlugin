/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Blacklists;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

public class RemoveLore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("removelore")) {
			if (sender.hasPermission("epicrename.removelore")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (WorldChecker.checkWorld(player)) {
						
						// Check Material Permissions
						if (!MaterialPermManager.checkPerms(EpicRenameCommands.REMOVELORE, RenameUtil.getInHand(player), player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removelore.no_permission_for_material"), player);
							return true;
						}
						
						// Check Blacklist
						if (!Blacklists.checkMaterialBlacklist(RenameUtil.getInHand(player).getType(), player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removelore.blacklisted_material_found"), player);
							return true;
						}
						
						// Check Existing Name Blacklist
						if (!Blacklists.checkExistingName(player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removelore.blacklisted_existing_name_found"), player);
							return true;
						}
						
						// Check Existing Lore Blacklist
						if (!Blacklists.checkExistingLore(player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removelore.blacklisted_existing_lore_found"), player);
							return true;
						}
						
						if (RenameUtil.getInHand(player).getType() == Material.AIR) {
							Messager.msgPlayer(Main.getMsgFromConfig("removelore.cannot_edit_air"), player);
							return true;
						}
						ItemMeta im = RenameUtil.getInHand(player).getItemMeta();
						im.setLore(null);
						RenameUtil.getInHand(player).setItemMeta(im);
						Messager.msgPlayer(Main.getMsgFromConfig("removelore.success"), player);
						return true;
						
					} else {
						Messager.msgSender(Main.getMsgFromConfig("removelore.disabled_world"), sender);
						return true;
					}
				} else {
					Messager.msgSender(Main.getMsgFromConfig("removelore.wrong_sender"), sender);
					return true;
				}				
			} else {
				Messager.msgSender(Main.getMsgFromConfig("removelore.no_permission"), sender);
				return true;
			}
		}
		return false;
	}

}
