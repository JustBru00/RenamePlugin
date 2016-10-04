package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.main.RenameRewrite;

public class RenameUtil {

	@SuppressWarnings("deprecation")
	/** 
	 * @param player The player to get the item from.
	 * @return The item stack in the players hand.
	 */
	public static ItemStack getInHand(Player player) {		
		if (RenameRewrite.USE_NEW_GET_HAND) {
			return player.getInventory().getItemInMainHand();
		} else {
			return player.getItemInHand();
		}	
	}
	
}
