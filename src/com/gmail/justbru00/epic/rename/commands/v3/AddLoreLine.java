package com.gmail.justbru00.epic.rename.commands.v3;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Blacklists;
import com.gmail.justbru00.epic.rename.utils.v3.FormattingCodeCounter;
import com.gmail.justbru00.epic.rename.utils.v3.FormattingPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

/**
 * ISSUE #132
 * @author Justin Brubaker
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
public class AddLoreLine implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase("addloreline")) {
			return false;
		}
		
		if (!sender.hasPermission("epicrename.addloreline")) {
			Messager.msgSenderWithConfigMsg("addloreline.no_permission", sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			Messager.msgSenderWithConfigMsg("addloreline.wrong_sender", sender);
			return true;
		}
		
		Player player = (Player) sender;
		if (!WorldChecker.checkWorld(player)) {
			Messager.msgSenderWithConfigMsg("addloreline.disabled_world", sender);
			return true;
		}
		
		ItemStack inHand = RenameUtil.getInHand(player);
		Material m = inHand.getType();
		
		if (!Blacklists.checkTextBlacklist(args, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("setloreline.blacklisted_word_found"), player);
			return true;
		}
		
		if (!Blacklists.checkMaterialBlacklist(RenameUtil.getInHand(player).getType(), player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("addloreline.blacklisted_material_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingName(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("addloreline.blacklisted_existing_name_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingLore(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("addloreline.blacklisted_existing_lore_found"), player);
			return true;
		}
		
		if (!MaterialPermManager.checkPerms(EpicRenameCommands.ADDLORELINE, inHand, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("addloreline.no_permission_for_material"), player);
			return true;
		}
		
		if ((m == Material.AIR || m == null)) {
			Messager.msgPlayer(Main.getMsgFromConfig("addloreline.cannot_edit_air"), player);		
			return true;
		}
		
		if (!FormattingPermManager.checkPerms(EpicRenameCommands.ADDLORELINE, args, player)) {
			// FormattingPermManager handles the message.
			return true;
		}
		
		if (args.length == 0) {
			Messager.msgPlayer(Main.getMsgFromConfig("addloreline.wrong_args"), player);
			return true;
		}
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < args.length; i++) {
			builder.append(args[i] + " ");
		}
		
		String loreToBeSet = builder.toString().trim();
		
		if (!FormattingCodeCounter.checkMinColorCodes(player, loreToBeSet, EpicRenameCommands.ADDLORELINE, true)) {
			FormattingCodeCounter.sendMinNotReachedMsg(player, EpicRenameCommands.ADDLORELINE);
			return true;
		}		
				
		if (!FormattingCodeCounter.checkMaxColorCodes(player, loreToBeSet, EpicRenameCommands.ADDLORELINE, true)) {
			FormattingCodeCounter.sendMaxReachedMsg(player, EpicRenameCommands.ADDLORELINE);
			return true;
		}
		
		ItemMeta im = inHand.getItemMeta();
		
		List<String> newLore = new ArrayList<String>();
		
		if (im.hasLore()) {
			newLore = im.getLore();
		} 
		
		newLore.add(Messager.color(loreToBeSet));		
		im.setLore(newLore);
		inHand.setItemMeta(im);
		if (Main.USE_NEW_GET_HAND) {
			player.getInventory().setItemInMainHand(inHand);
		} else {
			player.setItemInHand(inHand);
		}		
		
		Messager.msgPlayer(Main.getMsgFromConfig("addloreline.success"), player);
		
		return true;
	}

}
