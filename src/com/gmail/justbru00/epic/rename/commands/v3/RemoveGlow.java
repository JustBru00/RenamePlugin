package com.gmail.justbru00.epic.rename.commands.v3;

import java.util.Map;

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
import com.gmail.justbru00.epic.rename.utils.v3.Blacklists;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

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

						if (!(m == Material.AIR || m == null)) {
							Debug.send("[RemoveGlow] Enchantment List: " + inHand.getEnchantments().toString());
							
							Map<Enchantment, Integer> enchantments = inHand.getEnchantments();
							
							if (inHand.getType() == Material.FISHING_ROD) {
								Debug.send("[RemoveGlow] Fishing Rod has ARROW_INFINITE enchantment level of: " + inHand.getEnchantmentLevel(Enchantment.ARROW_INFINITE));
								Object arrowInfinite = enchantments.get(Enchantment.ARROW_INFINITE);
								
								if (arrowInfinite != null && (Integer) arrowInfinite == 4341) { // Has glowing
									inHand.removeEnchantment(Enchantment.ARROW_INFINITE);
									ItemMeta im = inHand.getItemMeta();
									im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
									inHand.setItemMeta(im);

									if (Main.USE_NEW_GET_HAND) { // Use 1.9+
																	// method
										player.getInventory().setItemInMainHand(inHand);
									} else { // Use older method.
										player.setItemInHand(inHand);
									}
									Messager.msgSender(Main.getMsgFromConfig("removeglow.success"), sender);
								} else {
									Messager.msgSender(Main.getMsgFromConfig("removeglow.not_glowing"), sender);
									return true;
								}
							} else {
								Debug.send("[RemoveGlow] Item has LURE enchantment level of: " + inHand.getEnchantmentLevel(Enchantment.LURE));
								Object lure = enchantments.get(Enchantment.LURE);
								
								if (lure != null && (Integer) lure == 4341) { // Has glowing	
									inHand.removeEnchantment(Enchantment.LURE);
									ItemMeta im = inHand.getItemMeta();
									im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
									inHand.setItemMeta(im);

									if (Main.USE_NEW_GET_HAND) { // Use 1.9+
																	// method
										player.getInventory().setItemInMainHand(inHand);
									} else { // Use older method.
										player.setItemInHand(inHand);
									}
									Messager.msgSender(Main.getMsgFromConfig("removeglow.success"), sender);
								} else {
									Messager.msgSender(Main.getMsgFromConfig("removeglow.not_glowing"), sender);
									return true;
								}
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
