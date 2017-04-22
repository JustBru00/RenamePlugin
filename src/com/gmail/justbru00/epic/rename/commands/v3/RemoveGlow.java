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

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;

public class RemoveGlow implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("removeglow")) {
			if (sender.hasPermission("epicrename.removeglow")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					ItemStack inHand = RenameUtil.getInHand(player);
					Material m = inHand.getType();

					if (!(m == Material.AIR || m == null)) {
						if (inHand.getType() == Material.FISHING_ROD) {
							if (inHand.getEnchantmentLevel(Enchantment.ARROW_INFINITE) == 4341) { // Has glowing
								inHand.removeEnchantment(Enchantment.ARROW_INFINITE);
								ItemMeta im = inHand.getItemMeta();
								im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
								inHand.setItemMeta(im);
								
								if (Main.USE_NEW_GET_HAND) { // Use 1.9+ method
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
							if (inHand.getEnchantmentLevel(Enchantment.LURE) == 4341) { // Has glowing
								
								inHand.removeEnchantment(Enchantment.LURE);
								ItemMeta im = inHand.getItemMeta();
								im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
								inHand.setItemMeta(im);
								
								if (Main.USE_NEW_GET_HAND) { // Use 1.9+ method
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
