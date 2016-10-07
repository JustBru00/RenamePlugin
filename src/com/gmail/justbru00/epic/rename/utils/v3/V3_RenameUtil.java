package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
	public static void rename(Player player, String[] args, V3_EpicRenameCommands erc) {
		
		if (erc == V3_EpicRenameCommands.RENAME) { // Command /rename handling START
			Debug.send("RenameUtil.rename() handling from /rename");
			
			ItemStack inHand = V3_RenameUtil.getInHand(player); // Item in the players hand.
			
			if (V3_Blacklists.checkTextBlacklist(args)) { // Check Text Blacklist
				Debug.send("Passed Text Blacklist");
				if (V3_Blacklists.checkMaterialBlacklist(inHand.getType())) { // Check Material Blacklist
					Debug.send("Passed Material Blacklist Check.");
					if (CharLimit.v3_checkCharLimit(args, player)) { // Check Character Limit
						Debug.send("Passed Character Limit Check.");
						if (inHand.getType() != Material.AIR) {
							//TODO Finish Command
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
			
			Debug.send("Code didn't go through Blacklist check. Well that shouldn't happen.");
			
			return;
		} else if (erc == V3_EpicRenameCommands.RENAMEENTITY) { // Command /rename handling END | Command /renameentity START
			Debug.send("RenameUtil.rename() handling from /renameentity");
			
			// TODO Handle /renameentity
			
		} else {
			Debug.send("What are you sending a non rename command to RenameUtil#rename() for? Either you failed at using this plugin as a API or JustBru00 failed big time.");
		}		
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
