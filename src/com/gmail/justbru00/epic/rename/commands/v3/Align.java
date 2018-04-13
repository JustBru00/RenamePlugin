package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.utils.v3.Blacklists;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

/**
 * 
 * @author Justin "JustBru00" Brubaker
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 * Created for Issue #86
 *
 */
public class Align implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("align")) {
			if (sender.hasPermission("epicrename.align")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					
					if (WorldChecker.checkWorld(player)) {
						ItemStack inHand = RenameUtil.getInHand(player);
						Material m = inHand.getType();
						
						// Check Material Permissions
						if (!MaterialPermManager.checkPerms(EpicRenameCommands.ALIGN, inHand, player)) {
							Messager.msgSenderWithConfigMsg("align.no_permission_for_material", sender);
							return true;
						}
						
						// Check Material Blacklist | #76
						if (!Blacklists.checkMaterialBlacklist(m, player)) {
							Messager.msgSenderWithConfigMsg("align.blacklisted_material_found", sender);
							return true;
						}
						
						// Check Existing Name Blacklist | #81
						if (!Blacklists.checkExistingName(player)) {
							Messager.msgSenderWithConfigMsg("align.blacklisted_existing_name_found", player);
							return true;
						}
						
						// Check Existing Lore Blacklist | #81
						if (!Blacklists.checkExistingLore(player)) {
							Messager.msgSenderWithConfigMsg("align.blacklisted_existing_lore_found", player);
							return true;
						}
						
						// Make sure item is not AIR or null
						if (m == Material.AIR || m == null) {
							Messager.msgSenderWithConfigMsg("align.cannot_edit_air", sender);
							return true;
						}
						
						// Example Command Usage: /align name <left || center || right>
						// /align lore <left || center || right> <1,2,3,5>
						// /align lore <left || center || right> <all>
						
						if (args.length == 0) {
							Messager.msgSenderWithConfigMsg("align.not_enough_args", sender);
							return true;
						}
						
					} else {
						Messager.msgSenderWithConfigMsg("align.disabled_world", sender);
						return true;
					}
				} else {
					Messager.msgSenderWithConfigMsg("align.wrong_sender", sender);
					return true;
				}
			} else {
				Messager.msgSenderWithConfigMsg("align.no_permission", sender);
				return true;
			}
		}
		
		return false;
	}

}
