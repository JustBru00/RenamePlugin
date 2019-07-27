package com.gmail.justbru00.epic.rename.commands.v3;

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
import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Blacklists;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.EconomyManager;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

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

						if (!(m == Material.AIR || m == null)) {
							if (inHand.getEnchantments().size() == 0) {								
								if (m == Material.FISHING_ROD) {
									Debug.send("Item is a fishing rod");
									
									// Add economy cost option #101
									EcoMessage ecoStatus = EconomyManager.takeMoney(player,	EpicRenameCommands.GLOW);

									if (ecoStatus == EcoMessage.TRANSACTION_ERROR) {
										return true;
									}
									
									inHand.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 4341);
									ItemMeta im = inHand.getItemMeta();
									im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
									inHand.setItemMeta(im);

									player.getInventory().setItemInMainHand(inHand);
									Messager.msgSender(Main.getMsgFromConfig("glow.success"), sender);
									return true;
								} else {
									Debug.send("Item is not a fishing rod.");
									
									// Add economy cost option #101
									EcoMessage ecoStatus = EconomyManager.takeMoney(player,	EpicRenameCommands.GLOW);

									if (ecoStatus == EcoMessage.TRANSACTION_ERROR) {
										return true;
									}
									
									inHand.addUnsafeEnchantment(Enchantment.LURE, 4341);
									ItemMeta im = inHand.getItemMeta();
									im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
									inHand.setItemMeta(im);

									if (Main.USE_NEW_GET_HAND) { // Use 1.9+ method
										player.getInventory().setItemInMainHand(inHand);
									} else { // Use older method.
										player.setItemInHand(inHand);
									}
									Messager.msgSender(Main.getMsgFromConfig("glow.success"), sender);
									return true;
								}
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
