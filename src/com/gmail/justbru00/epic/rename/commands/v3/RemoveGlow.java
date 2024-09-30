package com.gmail.justbru00.epic.rename.commands.v3;

import java.util.Map;

import com.gmail.justbru00.epic.rename.utils.v3.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class RemoveGlow implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("removeglow")) {
			if (sender.hasPermission("epicrename.removeglow")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (WorldChecker.checkWorld(player)) {
						ItemStack inHand = RenameUtil.getInHand(player);
						Material m = inHand.getType();
						
						// Issue #76 | Check Blacklist
						if (!Blacklists.checkMaterialBlacklist(RenameUtil.getInHand(player).getType(), player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removeglow.blacklisted_material_found"), player);
							return true;
						}
						// End Issue #76
						
						// Check Existing Name Blacklist #81
						if (!Blacklists.checkExistingName(player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removeglow.blacklisted_existing_name_found"), player);
							return true;
						}
						
						// Check Existing Lore Blacklist #81
						if (!Blacklists.checkExistingLore(player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removeglow.blacklisted_existing_lore_found"), player);
							return true;
						}
						
						// Check Material Permissions
						if (!MaterialPermManager.checkPerms(EpicRenameCommands.REMOVEGLOW, inHand, player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("removeglow.no_permission_for_material"), player);
							return true;
						}

						if (!(m == null || m == Material.AIR)) {
							// ISSUE #198 - Convert legacy glowing to modern glowing
							if (GlowingUtil.isLegacyToModernConversionEnabled()) {
								ItemStack converted = GlowingUtil.convertLegacyGlowingToModern(inHand);
								if (converted != null) {
									player.getInventory().setItemInMainHand(converted);
									inHand = converted;
								}
							} // END ISSUE #198

							Debug.send("[RemoveGlow] Enchantment List: " + inHand.getEnchantments().toString());

							if (GlowingUtil.isGlowingItem(inHand)) {
								ItemStack removed = GlowingUtil.removeGlowingFromItemModern(inHand);
								Debug.send("[RemoveGlow] After Removal Enchantment List: " + removed.getEnchantments().toString());
								if (removed != null) {
									player.getInventory().setItemInMainHand(removed);
									Messager.msgSender(Main.getMsgFromConfig("removeglow.success"), sender);
									return true;
								} else {
									Messager.msgSender(Main.getMsgFromConfig("removeglow.not_glowing"), sender);
									return true;
								}
							} else {
								Messager.msgSender(Main.getMsgFromConfig("removeglow.not_glowing"), sender);
								return true;
							}
						} else {
							Messager.msgSender(Main.getMsgFromConfig("removeglow.cannot_edit_air"), sender);
							return true;
						}
					} else {
						Messager.msgSender(Main.getMsgFromConfig("removeglow.disabled_world"), sender);
						return true;
					}
				} else {
					Messager.msgSender(Main.getMsgFromConfig("removeglow.wrong_sender"), sender);
					return true;
				}
			} else {
				Messager.msgSender(Main.getMsgFromConfig("removeglow.no_permission"), sender);
				return true;
			}
		}

		return false;
	}

}
