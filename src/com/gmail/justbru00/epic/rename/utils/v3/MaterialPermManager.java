package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;

public class MaterialPermManager {

	public static final String MATERIAL_PERM = "epicrename.{CMD}.material.{MATERIAL}";
	/**
	 * @deprecated depreciated in RELEASE 3.2
	 */
	public static final String OLD_MATERIAL_PERM = "epicrename.{CMD}.{MATERIAL}";
	
	/**
	 * Checks if the player has permission for the material provided.
	 * @param erc The command that is being performed.
	 * @param toCheck The {@link ItemStack} to check the material of.
	 * @param p The player that we are checking permissions for.
	 * @return True if the player has permission. False if the player doesn't have permission.
	 */
	public static boolean checkPerms(EpicRenameCommands erc, ItemStack toCheck, Player p) {
		// New Permission Checks
		String newPerm = MATERIAL_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc).replace("{MATERIAL}", toCheck.getType().toString()));
		String allNewPerm = MATERIAL_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc).replace("{MATERIAL}", "*"));
		
		if (p.hasPermission(allNewPerm)) {
			Debug.send("[MaterialPermManager] The player has permission. Perm: " + allNewPerm);
			return true;
		}
		
		if (p.hasPermission(newPerm)) {
			Debug.send("[MaterialPermManager] The player has permission. Perm: " + newPerm);
			return true;
		}
		
		// Old Permission Checks | These will be removed in 3.3
		if (erc.equals(EpicRenameCommands.RENAME) || erc.equals(EpicRenameCommands.LORE)) { // Old permissions only for /rename and /lore
			String oldPerm = OLD_MATERIAL_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc).replace("{MATERIAL}", toCheck.getType().toString()));
			String allOldPerm = OLD_MATERIAL_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc).replace("{MATERIAL}", "*"));
			
			if (p.hasPermission(allOldPerm)) {
				Debug.send("[MaterialPermManager] The player has permission. Perm: " + allOldPerm);
				Messager.msgConsole("&cThe permission " + allOldPerm + " is depreciated. It will be removed in the v3.3 update. Use " + allNewPerm + " instead.");
				return true;
			}
			
			if (p.hasPermission(oldPerm)) {
				Debug.send("[MaterialPermManager] The player has permission. Perm: " + oldPerm);
				Messager.msgConsole("&cThe permission " + oldPerm + " is depreciated. It will be removed in the v3.3 update. Use " + newPerm + " instead.");
				return true;
			}
		}
		
		return false;
	}
}
