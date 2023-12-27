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

public class EditLore implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase("editlore")) {
			return false;
		}
		
		if (!sender.hasPermission("epicrename.editlore")) {
			Messager.msgSenderWithConfigMsg("editlore.no_permission", sender);
			return true;
		}
		
		if (!(sender instanceof Player)) {
			Messager.msgSenderWithConfigMsg("editlore.wrong_sender", sender);
			return true;
		}
		
		Player player = (Player) sender;
		if (!WorldChecker.checkWorld(player)) {
			Messager.msgSenderWithConfigMsg("editlore.disabled_world", sender);
			return true;
		}
		
		ItemStack inHand = RenameUtil.getInHand(player);
		Material m = inHand.getType();
		
		if (!Blacklists.checkMaterialBlacklist(m, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editlore.blacklisted_material_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingName(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editlore.blacklisted_existing_name_found"), player);
			return true;
		}
		
		if (!Blacklists.checkExistingLore(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editlore.blacklisted_existing_lore_found"), player);
			return true;
		}
		
		if (!MaterialPermManager.checkPerms(EpicRenameCommands.EDITLORE, inHand, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editlore.no_permission_for_material"), player);
			return true;
		}
		
		if ((m == Material.AIR || m == null)) {
			Messager.msgPlayer(Main.getMsgFromConfig("editlore.cannot_edit_air"), player);		
			return true;
		}
		
		ItemMeta im = inHand.getItemMeta();
		if (!im.hasLore()) {
			Messager.msgPlayer(Main.getMsgFromConfig("editlore.no_lore"), player);
			return true;
		}	
		
		StringBuilder loreBuilder = new StringBuilder();
		
		for (String loreLine : im.getLore()) {
			Debug.sendPlain("[EditLore] Original: " + loreLine);
			String normalColorsReversed = Messager.reverseSectionSignTo(loreLine, '&');
			Debug.sendPlain("[EditLore] Reversed: " + normalColorsReversed);	
			String hexReversed = Messager.reverseFromXToHex(normalColorsReversed);
			Debug.sendPlain("[EditLore] Hex Reversal: " + hexReversed);
			loreBuilder.append(hexReversed + "|");
		}
		
		if (loreBuilder.length() == 0) {
			Messager.msgPlayer(Main.getMsgFromConfig("editlore.lore_content_empty"), player);
			return true;
		}
		
		String loreArgs = loreBuilder.substring(0, loreBuilder.length() - 1);
		Debug.sendPlain("[EditLore] Lore Args: " + loreArgs);
		
		if (label.equalsIgnoreCase("epeditlore")) {
			Messager.sendCommandSuggestionToPlayer(Main.getMsgFromConfig("editlore.click_to_edit"), "/eplore " + loreArgs, player);
		} else {
			Messager.sendCommandSuggestionToPlayer(Main.getMsgFromConfig("editlore.click_to_edit"), "/lore " + loreArgs, player);
		}		
		return true;
	}

}
