/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.enums.v3.EcoMessage;
import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;

import net.milkbowl.vault.economy.EconomyResponse;

public class EconomyManager {

	/**
	 * Tries to take the specified amount of money from the players balance. Will send the player any needed economy messages.
	 * @param player The {@link Player} to take the money from.	
	 * @param erc The command that is trying to withdraw money. (Used for getting the proper amount.)
	 * @return The {@link EcoMessage}
	 */
	public static EcoMessage takeMoney(Player player, EpicRenameCommands erc){
		
		if (Main.USE_ECO == false) {
			return EcoMessage.ECO_DISABLED;
		}
		
		if (player.hasPermission("epicrename.bypass.costs.*")) {
			Messager.msgPlayer(Main.getMsgFromConfig("economy.bypass"), player);
			return EcoMessage.ECO_BYPASS;
		}
		
		if (erc == EpicRenameCommands.RENAME) {
			
			if (player.hasPermission("epicrename.bypass.costs.rename")) {
				Messager.msgPlayer(Main.getMsgFromConfig("economy.bypass"), player);
				return EcoMessage.ECO_BYPASS;
			}
			
			EconomyResponse r = Main.econ.withdrawPlayer(player, Main.getInstance().getConfig().getInt("economy.costs.rename"));
			
			Debug.send("Value from config was: " + Main.getInstance().getConfig().getInt("economy.costs.rename"));
				
			if (r.transactionSuccess()) {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("economy.transaction_success"), r), player);
				return EcoMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("economy.transaction_error"), r), player);
				return EcoMessage.TRANSACTION_ERROR;
			}
		
		} else if (erc == EpicRenameCommands.LORE) {
			
			if (player.hasPermission("epicrename.bypass.costs.lore")) {
				Messager.msgPlayer(Main.getMsgFromConfig("economy.bypass"), player);
				return EcoMessage.ECO_BYPASS;
			}
			
			EconomyResponse r = Main.econ.withdrawPlayer(player, Main.getInstance().getConfig().getInt("economy.costs.lore"));
			
			if (r.transactionSuccess()) {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("economy.transaction_success"), r), player);
				return EcoMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("economy.transaction_error"), r), player);
				return EcoMessage.TRANSACTION_ERROR;
			}
		} else if (erc == EpicRenameCommands.GLOW) { // ISSUE #101
			
			if (player.hasPermission("epicrename.bypass.costs.glow")) {
				Messager.msgPlayer(Main.getMsgFromConfig("economy.bypass"), player);
				return EcoMessage.ECO_BYPASS;
			}
			
			EconomyResponse r = Main.econ.withdrawPlayer(player, Main.getInstance().getConfig().getInt("economy.costs.glow"));
			
			if (r.transactionSuccess()) {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("economy.transaction_success"), r), player);
				return EcoMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("economy.transaction_error"), r), player);
				return EcoMessage.TRANSACTION_ERROR;
			}
		} // END ISSUE #101
		
		return EcoMessage.UNHANDLED;
	}
	
	/**
	 * Formats the message with the {cost} and {error} varibles.
	 * @param msg The message you want to replace the varibles in.
	 * @param r The {@link EconomyResponse} that you want the varibles replace with.
	 * @return The formated string with the variibles replaced.
	 */
	public static String formatMsg(String msg, EconomyResponse r) {		
		
		msg = msg.replace("{cost}", String.valueOf(r.amount));	
		
		if (!r.transactionSuccess()) msg = msg.replace("{error}", r.errorMessage);
		return msg;
	}
	
}
