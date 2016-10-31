/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.enums.v3.V3_EcoMessage;
import com.gmail.justbru00.epic.rename.enums.v3.V3_EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.Messager;

import net.milkbowl.vault.economy.EconomyResponse;

public class V3_EconomyManager {

	/**
	 * Tries to take the specified amount of money from the players balance. Will message player any needed economy messages.
	 * @param player The {@link Player} to take the money from.	
	 * @param erc The command that is trying to withdraw money. (Used for getting the proper amount.)
	 * @return The {@link V3_EcoMessage}
	 */
	public static V3_EcoMessage takeMoney(Player player, V3_EpicRenameCommands erc){
		
		if (V3_Main.USE_ECO == false) {
			return V3_EcoMessage.ECO_DISABLED;
		}
		
		if (player.hasPermission("epicrename.bypass.costs.*")) {
			Messager.msgPlayer(V3_Main.getMsgFromConfig("economy.bypass"), player);
			return V3_EcoMessage.ECO_BYPASS;
		}
		
		if (erc == V3_EpicRenameCommands.RENAME) {
			
			if (player.hasPermission("epicrename.bypass.costs.rename")) {
				Messager.msgPlayer(V3_Main.getMsgFromConfig("economy.bypass"), player);
				return V3_EcoMessage.ECO_BYPASS;
			}
			
			EconomyResponse r = V3_Main.econ.withdrawPlayer(player, V3_Main.getInstance().getConfig().getInt("economy.costs.rename"));
				
			if (r.transactionSuccess()) {
				Messager.msgPlayer(formatMsg(V3_Main.getMsgFromConfig("economy.transaction_success"), r), player);
				return V3_EcoMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(V3_Main.getMsgFromConfig("economy.transaction_error"), r), player);
				return V3_EcoMessage.TRANSACTION_ERROR;
			}
		
		} else if (erc == V3_EpicRenameCommands.LORE) {
			
			if (player.hasPermission("epicrename.bypass.costs.lore")) {
				Messager.msgPlayer(V3_Main.getMsgFromConfig("economy.bypass"), player);
				return V3_EcoMessage.ECO_BYPASS;
			}
			
			EconomyResponse r = V3_Main.econ.withdrawPlayer(player, V3_Main.getInstance().getConfig().getInt("economy.costs.lore"));
			
			if (r.transactionSuccess()) {
				Messager.msgPlayer(formatMsg(V3_Main.getMsgFromConfig("economy.transaction_success"), r), player);
				return V3_EcoMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(V3_Main.getMsgFromConfig("economy.transaction_error"), r), player);
				return V3_EcoMessage.TRANSACTION_ERROR;
			}
		} else if (erc == V3_EpicRenameCommands.RENAMEENTITY) {
			
			if (player.hasPermission("epicrename.bypass.costs.renameentity")) {
				Messager.msgPlayer(V3_Main.getMsgFromConfig("economy.bypass"), player);
				return V3_EcoMessage.ECO_BYPASS;
			}
			
			EconomyResponse r = V3_Main.econ.withdrawPlayer(player, V3_Main.getInstance().getConfig().getInt("economy.costs.renameentity"));
			
			if (r.transactionSuccess()) {
				Messager.msgPlayer(formatMsg(V3_Main.getMsgFromConfig("economy.transaction_success"), r), player);
				return V3_EcoMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(V3_Main.getMsgFromConfig("economy.transaction_error"), r), player);
				return V3_EcoMessage.TRANSACTION_ERROR;
			}
		}	
		
		return V3_EcoMessage.UNHANDLED;
	}
	
	/**
	 * Formats the message with the {cost} and {error} varibles.
	 * @param msg The message you want to replace the varibles in.
	 * @param r The {@link EconomyResponse} that you want the varibles replace with.
	 * @return The formated string with the variibles replaced.
	 */
	public static String formatMsg(String msg, EconomyResponse r) {		
		
		msg = msg.replace("{cost}", String.valueOf(100.0));	
		
		if (!r.transactionSuccess()) msg = msg.replace("{error}", r.errorMessage);
		return msg;
	}
	
}
