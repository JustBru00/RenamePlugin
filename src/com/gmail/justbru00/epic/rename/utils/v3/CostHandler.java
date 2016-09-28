package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.customexceptions.v3.EcoDisabledException;
import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.RenameRewrite;
import com.gmail.justbru00.epic.rename.utils.Messager;

import net.milkbowl.vault.economy.EconomyResponse;

public class CostHandler {

	// This Class will handle all economy stuff in version 3
	
	/**
	 * 
	 * @param sender The Player who sent this command.
	 * @param cmd The command that sent this command.
	 * @return False if failed true if successfull.
	 * @throws EcoDisabledException 
	 */
	public boolean withdrawMoney(Player sender, EpicRenameCommands cmd) throws EcoDisabledException {
	
		if (RenameRewrite.getInstance().useEconomy) {
			
		}
		
		throw new EcoDisabledException(); // If Economy is disabled throw exception.
	}
	
	//EconomyResponse r = RenameRewrite.econ.withdrawPlayer(sender, main.config.getInt("economy.costs.rename"));
	//if (r.transactionSuccess()) {
		//player.sendMessage(String.format(
			//	RenameRewrite.Prefix + Messager
				//		.color("&6Withdrew &a%s &6from your balance. Your current balance is now: &a%s"),
			//	RenameRewrite.econ.format(r.amount),
				//RenameRewrite.econ.format(r.balance)));
	//	player.setItemInHand(main.renameItemStack(player, args[0], inHand));
}
