package com.gmail.justbru00.epic.rename.commands.v3;

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

import com.gmail.justbru00.epic.rename.enums.v3.EcoMessage;
import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.enums.v3.XpMessage;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class Glow implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("glow")) {
			if (sender.hasPermission("epicrename.glow")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;

					if (WorldChecker.checkWorld(player)) {
						ItemStack inHand = RenameUtil.getInHand(player);
						Material m = inHand.getType();
						
						// Check Material Permissions
						if (!MaterialPermManager.checkPerms(EpicRenameCommands.GLOW, inHand, player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("glow.no_permission_for_material"), player);
							return true;
						}
						
						// Issue #76 | Check Blacklist						
						if (!Blacklists.checkMaterialBlacklist(RenameUtil.getInHand(player).getType(), player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("glow.blacklisted_material_found"), player);
							return true;
						}
						// End Issue #76
						
						// Check Existing Name Blacklist #81
						if (!Blacklists.checkExistingName(player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("glow.blacklisted_existing_name_found"), player);
							return true;
						}
						
						// Check Existing Lore Blacklist #81
						if (!Blacklists.checkExistingLore(player)) {
							Messager.msgPlayer(Main.getMsgFromConfig("glow.blacklisted_existing_lore_found"), player);
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

							if (inHand.getEnchantments().size() == 0) {
								// Add economy cost option #101
								EcoMessage ecoStatus = EconomyManager.takeMoney(player,	EpicRenameCommands.GLOW);

								if (ecoStatus == EcoMessage.TRANSACTION_ERROR) {
									return true;
								}
									
								// Add experience cost option #121
								XpMessage xpStatus = XpCostManager.takeXp(player, EpicRenameCommands.GLOW);
									
								if (xpStatus == XpMessage.TRANSACTION_ERROR) {
									return true;
								}

								player.getInventory().setItemInMainHand(GlowingUtil.addGlowingToItemModern(inHand));
								Messager.msgSender(Main.getMsgFromConfig("glow.success"), sender);
								return true;
							} else {
								Messager.msgSender(Main.getMsgFromConfig("glow.has_enchants"), sender);
								return true;
							}
						} else {
							Messager.msgSender(Main.getMsgFromConfig("glow.cannot_edit_air"), sender);
							return true;
						}
					} else {
						Messager.msgSender(Main.getMsgFromConfig("glow.disabled_world"), sender);
						return true;
					}
				} else {
					Messager.msgSender(Main.getMsgFromConfig("glow.wrong_sender"), sender);
					return true;
				}
			} else {
				Messager.msgSender(Main.getMsgFromConfig("glow.no_permission"), sender);
				return true;
			}
		}

		return false;
	}

}
