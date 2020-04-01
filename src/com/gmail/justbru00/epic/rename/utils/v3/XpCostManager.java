/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.enums.v3.XpMessage;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class XpCostManager {

	/**
	 * Tries to take the specified amount of experience from the player. Will send the player any needed messages.
	 * @param player The {@link Player} to take the experience from.	
	 * @param erc The command that is trying to withdraw experience. (Used for getting the proper amount.)
	 * @return The {@link XpMessage}
	 */
	public static XpMessage takeXp(Player player, EpicRenameCommands erc){
		
		if (Main.USE_XP_COST == false) {
			return XpMessage.XP_DISABLED;
		}
		
		if (player.hasPermission("epicrename.bypass.costs.*")) {
			Messager.msgPlayer(Main.getMsgFromConfig("xp.bypass"), player);
			return XpMessage.XP_BYPASS;
		}
		
		if (erc == EpicRenameCommands.RENAME) {
			
			if (player.hasPermission("epicrename.bypass.costs.rename")) {
				Messager.msgPlayer(Main.getMsgFromConfig("xp.bypass"), player);
				return XpMessage.XP_BYPASS;
			}
			
			XpResponse r = withdraw(player, Main.getInstance().getConfig().getInt("xp.costs.rename"));
			
			Debug.send("Value from config was: " + Main.getInstance().getConfig().getInt("xp.costs.rename"));
				
			if (r.isTransactionSuccess()) {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("xp.transaction_success"), r), player);
				return XpMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("xp.transaction_error"), r), player);
				return XpMessage.TRANSACTION_ERROR;
			}
		
		} else if (erc == EpicRenameCommands.LORE) {
			
			if (player.hasPermission("epicrename.bypass.costs.lore")) {
				Messager.msgPlayer(Main.getMsgFromConfig("xp.bypass"), player);
				return XpMessage.XP_BYPASS;
			}
			XpResponse r = withdraw(player, Main.getInstance().getConfig().getInt("xp.costs.lore"));
			
			if (r.isTransactionSuccess()) {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("xp.transaction_success"), r), player);
				return XpMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("xp.transaction_error"), r), player);
				return XpMessage.TRANSACTION_ERROR;
			}
		} else if (erc == EpicRenameCommands.GLOW) {
			
			if (player.hasPermission("epicrename.bypass.costs.glow")) {
				Messager.msgPlayer(Main.getMsgFromConfig("economy.bypass"), player);
				return XpMessage.XP_BYPASS;
			}
			
			XpResponse r = withdraw(player, Main.getInstance().getConfig().getInt("xp.costs.glow"));
			
			if (r.isTransactionSuccess()) {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("xp.transaction_success"), r), player);
				return XpMessage.SUCCESS;
			} else {
				Messager.msgPlayer(formatMsg(Main.getMsgFromConfig("xp.transaction_error"), r), player);
				return XpMessage.TRANSACTION_ERROR;
			}
		}
		
		return XpMessage.UNHANDLED;
	}
	
	/**
	 * Formats the message with the {cost} and {error} variables.
	 * @param msg The message you want to replace the variables in.
	 * @param r The {@link XpResponse} that you want the variables replace with.
	 * @return The formated string with the variables replaced.
	 */
	public static String formatMsg(String msg, XpResponse r) {		
		
		msg = msg.replace("{cost}", String.valueOf(r.getXpAmount()));	
		
		if (!r.isTransactionSuccess()) msg = msg.replace("{error}", r.getErrorMessage());
		return msg;
	}
	/**
	 * Attempts to withdraw XP from the player.
	 * Will check if the player even has enough before attempting to withdraw.
	 * @param p
	 * @param xpCost
	 * @return An {@link XpResponse} with the outcome of this attempted withdraw.
	 */
	public static XpResponse withdraw(Player p, Integer xpCost) {
		Integer totalXp = getTotalExperience(p);
		XpResponse r = new XpResponse();
		r.setXpAmount(xpCost);
		
		if (totalXp < xpCost) {
			r.setTransactionSuccess(false);
			r.setErrorMessage("Not enough experience.");
			return r;
		}
		
		totalXp = totalXp - xpCost;
		setTotalExperience(p, totalXp);
		r.setTransactionSuccess(true);
		
		return r;
	}
	
	/**
	 * From @Djaytan on spigotmc.org.
	 * https://www.spigotmc.org/threads/solved-setting-and-getting-a-players-current-experience-points-not-levels.72804/#post-3466821
	 * @param level
	 * @return
	 */
	public static int getTotalExperience(int level) {
        int xp = 0;

        if (level >= 0 && level <= 15) {
            xp = (int) Math.round(Math.pow(level, 2) + 6 * level);
        } else if (level > 15 && level <= 30) {
            xp = (int) Math.round((2.5 * Math.pow(level, 2) - 40.5 * level + 360));
        } else if (level > 30) {
            xp = (int) Math.round(((4.5 * Math.pow(level, 2) - 162.5 * level + 2220)));
        }
        return xp;
    }

	/**
	 * From @Djaytan on spigotmc.org.
	 * https://www.spigotmc.org/threads/solved-setting-and-getting-a-players-current-experience-points-not-levels.72804/#post-3466821
	 * @param player
	 * @return
	 */
    public static int getTotalExperience(Player player) {
        return Math.round(player.getExp() * player.getExpToLevel()) + getTotalExperience(player.getLevel());
    }

    /**
     * From @Djaytan on spigotmc.org
     * https://www.spigotmc.org/threads/solved-setting-and-getting-a-players-current-experience-points-not-levels.72804/#post-3466821
     * @param player
     * @param amount
     */
    public static void setTotalExperience(Player player, int amount) {
        int level = 0;
        int xp = 0;
        float a = 0;
        float b = 0;
        float c = -amount;

        if (amount > getTotalExperience(0) && amount <= getTotalExperience(15)) {
            a = 1;
            b = 6;
        } else if (amount > getTotalExperience(15) && amount <= getTotalExperience(30)) {
            a = 2.5f;
            b = -40.5f;
            c += 360;
        } else if (amount > getTotalExperience(30)) {
            a = 4.5f;
            b = -162.5f;
            c += 2220;
        }
        level = (int) Math.floor((-b + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a));
        xp = amount - getTotalExperience(level);
        player.setLevel(level);
        player.setExp(0);
        player.giveExp(xp);
    }

	
}
