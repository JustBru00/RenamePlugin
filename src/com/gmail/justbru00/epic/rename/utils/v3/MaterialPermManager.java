package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;

public class MaterialPermManager {

	public static final String MATERIAL_PERM = "epicrename.{CMD}.material.{MATERIAL}";
	
	/**
	 * Checks if the player has permission for the material provided.
	 * @param erc The command that is being performed.
	 * @param toCheck The {@link ItemStack} to check the material of.
	 * @param p The player that we are checking permissions for.
	 * @return True if the player has permission. False if the player doesn't have permission.
	 */
	public static boolean checkPerms(EpicRenameCommands erc, ItemStack toCheck, Player p) {
		// New Permission Checks
		String newPerm = MATERIAL_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc)).replace("{MATERIAL}", toCheck.getType().toString());
		String allNewPerm = MATERIAL_PERM.replace("{CMD}", EpicRenameCommands.getStringName(erc)).replace("{MATERIAL}", "*");
		
		//Debug.send("[MaterialPermManager] NewPerm: " + newPerm + " allNewPerm: " + allNewPerm);
		
		if (p.hasPermission(allNewPerm)) {
			Debug.send("[MaterialPermManager] The player has permission. Perm: " + allNewPerm);
			return true;
		}
		
		if (p.hasPermission(newPerm)) {
			Debug.send("[MaterialPermManager] The player has permission. Perm: " + newPerm);
			return true;
		}

		Debug.send("[MaterialPermManager] The player doesn't have any of the correct material permissions.");
		return false;
	}
}
