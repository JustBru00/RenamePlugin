package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.main.RenameRewrite;
import com.gmail.justbru00.epic.rename.utils.CharLimit;
import com.gmail.justbru00.epic.rename.utils.Debug;
import com.gmail.justbru00.epic.rename.utils.Messager;

public class RenameUtil {

	@SuppressWarnings("deprecation")
	public static void inHand(String[] args, Player player) {
		
		ItemStack inHand;
		
		if (RenameRewrite.USE_NEW_GET_HAND) {
			inHand = player.getInventory().getItemInMainHand();
		} else {
			inHand = player.getItemInHand();
		}
		
		for (String argscombined : args) {
			Debug.send(argscombined);			
		}
		
		// Check Text Blacklist
		if (RenameRewrite.getInstance().checkBlacklist(args[0])) {
			Messager.msgPlayer(player, RenameRewrite.getInstance().config.getString("found blacklisted word"));
			return;
		}

		// Check Material Blacklist
		if (!RenameRewrite.getInstance().checkMaterialBlacklist(player, inHand.getType())) {
			Messager.msgPlayer(player, RenameRewrite.getInstance().config.getString("found blacklisted material"));
			return;
		}

		// Check Char Limit
		if (CharLimit.checkCharLimit(args[0], player)) {
			// Too long
			Messager.msgPlayer(player, RenameRewrite.getInstance().getConfig().getString("charlimitmessage"));
			return;
		}
		
		
		
	}
	
}
