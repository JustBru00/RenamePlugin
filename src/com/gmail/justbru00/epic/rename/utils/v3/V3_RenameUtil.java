/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.V3_EcoMessage;
import com.gmail.justbru00.epic.rename.enums.v3.V3_EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.RenameRewrite;
import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.CharLimit;
import com.gmail.justbru00.epic.rename.utils.Debug;
import com.gmail.justbru00.epic.rename.utils.Messager;


public class V3_RenameUtil {

	// VERSION 3
	
	/**
	 * 
	 * @param sender The {@link Player} who sent the command.
	 * @param args The arguments of the command sent to the {@link CommandExecutor}
	 * @param erc The command that called this method.
	 */
	@SuppressWarnings("deprecation")
	public static void renameHandle(Player player, String[] args, V3_EpicRenameCommands erc) {
		
		if (erc == V3_EpicRenameCommands.RENAME) { // Command /rename handling START
			Debug.send("RenameUtil.rename() handling from /rename");
			
			ItemStack inHand = V3_RenameUtil.getInHand(player); // Item in the players hand.
			
			if (V3_Blacklists.checkTextBlacklist(args)) { // Check Text Blacklist
				Debug.send("Passed Text Blacklist");
				if (V3_Blacklists.checkMaterialBlacklist(inHand.getType())) { // Check Material Blacklist
					Debug.send("Passed Material Blacklist Check.");
					if (CharLimit.v3_checkCharLimit(args, player)) { // Check Character Limit
						Debug.send("Passed Character Limit Check.");
						if (inHand.getType() != Material.AIR) { // Check != Air
							if (player.hasPermission("epicrename.rename." + inHand.getType().toString()) || player.hasPermission("epicrename.rename.*")) { // Check for per material permissions		
								
								V3_EcoMessage ecoStatus = V3_EconomyManager.takeMoney(player, V3_EpicRenameCommands.RENAME);
								
								if (ecoStatus == V3_EcoMessage.TRANSACTION_ERROR) {
									return;
								}
								
								if (V3_Main.USE_NEW_GET_HAND) { // Use 1.9+ method
									player.getInventory().setItemInMainHand(V3_RenameUtil.renameItemStack(player, args, inHand));
									return;
								} else { // Use older method.
									player.setItemInHand(V3_RenameUtil.renameItemStack(player, args, inHand));
									return;
								}									
							} else {
								Messager.msgPlayer(V3_Main.getMsgFromConfig("rename.no_permission_for_material"), player);
								return;
							}
						} else {
							Messager.msgPlayer(V3_Main.getMsgFromConfig("rename.cannot_rename_air"), player);
							return;
						}
					} else {
						Messager.msgPlayer(V3_Main.getMsgFromConfig("rename.charlimit.name_too_long"), player);
						return;
					}
				} else {
					Messager.msgPlayer(V3_Main.getMsgFromConfig("rename.blacklisted_material_found"), player);
					return;
				}
			} else {
				Messager.msgPlayer(V3_Main.getMsgFromConfig("rename.blacklisted_word_found"), player);
				return;
			}
		} else if (erc == V3_EpicRenameCommands.RENAMEENTITY) { // Command /rename handling END | Command /renameentity START
			Debug.send("RenameUtil.rename() handling from /renameentity");
			
			// TODO Handle /renameentity
			
		} else {
			Debug.send("What are you sending a non rename command to RenameUtil#rename() for? Either you failed at using this plugin as a API or JustBru00 failed big time.");
		}		
	}
	/**
	 * 
	 * @param player The {@link Player} who ran the command.
	 * @param args The arguments of the command.
	 * @param toRename The {@link ItemStack} that is being renamed.
	 * @return The renamed {@link ItemStack}.
	 */
	public static ItemStack renameItemStack(Player player, String[] args, ItemStack toRename) {
		
		StringBuilder builder = new StringBuilder("");		
		String completeArgs = "";		
		
		for (String item : args) {
			builder.append(item + " ");
		}
		
		completeArgs = builder.toString();
		Debug.send("The args result is: " + completeArgs);
		
		ItemMeta im = toRename.getItemMeta();
		im.setDisplayName(Messager.color(completeArgs));
		toRename.setItemMeta(im);
		
		return toRename;
	}
	
	@SuppressWarnings("deprecation")
	/** 
	 * @param player The player to get the item from.
	 * @return The item stack in the players hand.
	 */
	public static ItemStack getInHand(Player player) {	
		ItemStack returning = null;
		
		if (RenameRewrite.USE_NEW_GET_HAND) {
			returning = player.getInventory().getItemInMainHand();
			return returning;
		} else {
			
			try {
				returning = player.getItemInHand();
			} catch (Exception e) {
				Debug.send("Problem while getting the ItemStack inHand. (Failed at player.getItemInHand()) Version problem?");				
			}
			
			return returning;
		}	
	}
	
}
