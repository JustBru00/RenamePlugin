package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Blacklists;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

/**
 * ISSUE #176
 * @author Justin Brubaker
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
public class UnHideEnchantments implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase("unhideenchantments")) {
			return false;
		}
		
		if (!sender.hasPermission("epicrename.unhideenchantments")) {
			Messager.msgSenderWithConfigMsg("unhideenchantments.no_permission", sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			Messager.msgSenderWithConfigMsg("unhideenchantments.wrong_sender", sender);
			return true;
		}
		
		Player player = (Player) sender;
		if (!WorldChecker.checkWorld(player)) {
			Messager.msgSenderWithConfigMsg("unhideenchantments.disabled_world", sender);
			return true;
		}
		
		ItemStack inHand = RenameUtil.getInHand(player);
		Material m = inHand.getType();
		
		if (!Blacklists.checkMaterialBlacklist(m, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("unhideenchantments.blacklisted_material_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingName(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("unhideenchantments.blacklisted_existing_name_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingLore(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("unhideenchantments.blacklisted_existing_lore_found"), player);
			return true;
		}
		
		if (!MaterialPermManager.checkPerms(EpicRenameCommands.UNHIDEENCHANTMENTS, inHand, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("unhideenchantments.no_permission_for_material"), player);
			return true;
		}
		
		if ((m == Material.AIR || m == null)) {
			Messager.msgPlayer(Main.getMsgFromConfig("unhideenchantments.cannot_edit_air"), player);		
			return true;
		}
		
		ItemMeta im = inHand.getItemMeta();
		im.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
		inHand.setItemMeta(im);
		
		Messager.msgPlayer(Main.getMsgFromConfig("unhideenchantments.success"), player);		
		
		return true;
	}

}
