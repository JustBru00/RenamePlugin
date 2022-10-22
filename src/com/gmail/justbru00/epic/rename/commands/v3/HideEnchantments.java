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
 * Issue #133
 * @author Justin Brubaker
 *
 */
public class HideEnchantments implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!command.getName().equalsIgnoreCase("hideenchantments")) {
			return false;
		}		
		
		if (!sender.hasPermission("epicrename.hideenchantments")) {
			Messager.msgSenderWithConfigMsg("hideenchantments.no_permission", sender);
		}
		
		if (!(sender instanceof Player)) {
			Messager.msgSenderWithConfigMsg("hideenchantments.wrong_sender", sender);
		}
		
		Player player = (Player) sender;
		if (!WorldChecker.checkWorld(player)) {
			Messager.msgSenderWithConfigMsg("hideenchantments.disabled_world", sender);
			return true;
		}
		
		ItemStack inHand = RenameUtil.getInHand(player);
		Material m = inHand.getType();
						
		// Issue #76 | Check Blacklist
		if (!Blacklists.checkMaterialBlacklist(RenameUtil.getInHand(player).getType(), player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("hideenchantments.blacklisted_material_found"), player);
			return true;
		}
		// End Issue #76
						
		// Check Existing Name Blacklist #81
		if (!Blacklists.checkExistingName(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("hideenchantments.blacklisted_existing_name_found"), player);
			return true;
		}
						
		// Check Existing Lore Blacklist #81
		if (!Blacklists.checkExistingLore(player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("hideenchantments.blacklisted_existing_lore_found"), player);
			return true;
		}
						
		// Check Material Permissions
		if (!MaterialPermManager.checkPerms(EpicRenameCommands.HIDEENCHANTMENTS, inHand, player)) {
			Messager.msgPlayer(Main.getMsgFromConfig("hideenchantments.no_permission_for_material"), player);
			return true;
		}

		if ((m == Material.AIR || m == null)) {
			Messager.msgPlayer(Main.getMsgFromConfig("hideenchantments.cannot_edit_air"), player);		
			return true;
		}

		ItemMeta im = inHand.getItemMeta();
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		inHand.setItemMeta(im);
		
		Messager.msgPlayer(Main.getMsgFromConfig("hideenchantments.success"), player);		
		
		return true;
	}

}
