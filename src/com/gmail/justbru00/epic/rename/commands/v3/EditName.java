package com.gmail.justbru00.epic.rename.commands.v3;

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
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

/**
 * ISSUE #168
 * @author Justin Brubaker
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
public class EditName implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase("editname")) {
			return false;
		}
		
		if (!sender.hasPermission("epicrename.editname")) {
			Messager.msgSenderWithConfigMsg("editname.no_permission", sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			Messager.msgSenderWithConfigMsg("editname.wrong_sender", sender);
			return true;
		}
		
		Player player = (Player) sender;
		if (!WorldChecker.checkWorld(player)) {
			Messager.msgSenderWithConfigMsg("editname.disabled_world", sender);
			return true;
		}
		
		ItemStack inHand = RenameUtil.getInHand(player);
		Material m = inHand.getType();
		
		if (!Blacklists.checkMaterialBlacklist(m, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editname.blacklisted_material_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingName(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editname.blacklisted_existing_name_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingLore(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editname.blacklisted_existing_lore_found"), player);
			return true;
		}
		
		if (!MaterialPermManager.checkPerms(EpicRenameCommands.EDITNAME, inHand, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editname.no_permission_for_material"), player);
			return true;
		}
		
		if ((m == Material.AIR || m == null)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editname.cannot_edit_air"), player);		
			return true;
		}
		
		ItemMeta im = inHand.getItemMeta();
		if (!im.hasDisplayName()) {
			Messager.msgPlayer(Main.getMsgFromConfig("editname.no_displayname"), player);
			return true;
		}	
		
		Debug.sendPlain("[EditName] Original: " + im.getDisplayName());
		String normalColorsReversed = Messager.reverseSectionSignTo(im.getDisplayName(), '&');
		Debug.sendPlain("[EditName] Reversed: " + normalColorsReversed);	
		String hexReversed = Messager.reverseFromXToHex(normalColorsReversed);
		Debug.sendPlain("[EditName] Hex Reversal: " + hexReversed);
		
		if (label.equalsIgnoreCase("epeditname")) {
			Messager.sendCommandSuggestionToPlayer(Main.getMsgFromConfig("editname.click_to_edit"), "/eprename " + hexReversed, player);
		} else {
			Messager.sendCommandSuggestionToPlayer(Main.getMsgFromConfig("editname.click_to_edit"), "/rename " + hexReversed, player);
		}		
		return true;
	}

}
