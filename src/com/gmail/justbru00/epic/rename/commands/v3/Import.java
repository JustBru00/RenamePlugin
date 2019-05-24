/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.justbru00.epic.rename.utils.v3.ItemSerialization;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
/**
 * Created for #106
 * @author Justin Brubaker
 *
 */
public class Import implements CommandExecutor {
	
	/**
	 * Usage:
	 * 
	 *  import <hand,inventory> <link,rawyaml>
	 *  
	 *  Examples:
	 *  
	 *  import hand https://pastebin.com/ValidLink
	 *  import hand random yaml stuff
	 *  import inventory https://pastebin.com/ValidLink
	 *  import inv https://pastebin.com/ValidLink
	 *  import h https://pastebin.com/ValidLink
	 *  import i https://pastebin.com/ValidLink
	 *  import raw yaml text here
	 *  
	 *  Importing an inventory forces a /import confirm before allowing a player to import once per server restart.
	 *  Importing to your hand only works if the hand is empty.
	 *
	 **/

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player p = (Player) sender;			
			
			if (sender.hasPermission("epicrename.import")) {
				
				if (args.length != 0) {
					PlayerInventory inv = p.getInventory();
					ItemStack mainHand = inv.getItemInMainHand();
					if (args[0].equalsIgnoreCase("hand") || args[0].equalsIgnoreCase("h")) {
						// /import hand
						
						
					} else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("inv")
							|| args[0].equalsIgnoreCase("i")) {						
						// /import inventory 
						
						
					} else if (args[0].equalsIgnoreCase("raw") || args[0].equalsIgnoreCase("r")) {
						// /import raw
						
						// Try to import the raw yaml text. Single item only.
						if (mainHand == null || mainHand.getType() == Material.AIR) {
							// Empty Main Hand
							StringBuilder yamlDataBuilder = new StringBuilder("");
							
							for (int i = 1; i < args.length; i++) {
								yamlDataBuilder.append(args[i] + " ");
							}
							
							String yamlData = yamlDataBuilder.toString().trim();
							
							ItemStack importedItem = ItemSerialization.toItem(yamlData);
							
							if (importedItem == null) {
								// FAILED
								Messager.msgSenderWithConfigMsg("import.raw_fail", sender);
								return true;
							}
							
							// Put ItemStack in the players main hand.
							
							inv.setItemInMainHand(importedItem);
							Messager.msgSenderWithConfigMsg("import.success", sender);									
							return true;
						} else {
							// Must have empty hand
							Messager.msgSenderWithConfigMsg("import.full_hand", sender);
							return true;
						}
					} else {
						Messager.msgSenderWithConfigMsg("import.wrong_args", sender);
						return true;
					}
				} else { // Player must provide at least one argument
					Messager.msgSenderWithConfigMsg("import.no_args", sender);
					return true;
				}
			} else { // sender doesn't have permission
				Messager.msgSenderWithConfigMsg("import.no_permission", sender);
				return true;
			}
		} else { // sender NOT Player
			Messager.msgSenderWithConfigMsg("import.wrong_sender", sender);
			return true;
		}
		
		return true;
	}

}
