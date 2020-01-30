/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
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
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.FormattingCodeCounter;
import com.gmail.justbru00.epic.rename.utils.v3.FormattingPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.MaterialPermManager;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

public class InsertLoreLine implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("insertloreline")) {
			if (sender.hasPermission("epicrename.insertloreline")) {
				if (sender instanceof Player) {
					
					Player player = (Player) sender;
					
					if (WorldChecker.checkWorld(player)) {
						
						if (args.length >= 1) {
							
							int lineNumber = -1;
							
							try {
								lineNumber = Integer.parseInt(args[0]);
							} catch (NumberFormatException e){
								// Not an integer.
								Debug.send(e.getMessage());
								Messager.msgSenderWithConfigMsg("insertloreline.not_an_int", sender);
								return true;
							}
							
							if (args.length == 1) {
								// No text arguments
								Messager.msgSenderWithConfigMsg("insertloreline.wrong_args", sender);
								return true;
							}
							
							if (lineNumber <= 0) {
								Debug.send("[InsertLoreLine] The number " + lineNumber + " is below or equal to 0.");
								Messager.msgPlayer(Main.getMsgFromConfig("insertloreline.invalid_number"), player);
								return true;
							}
							
							Material type = RenameUtil.getInHand(player).getType();
							
							if (type== Material.AIR || type == null) {
								// Cannot edit air
								Messager.msgSenderWithConfigMsg("insertloreline.cannot_edit_air", sender);
								return true;
							}
							
							ItemStack inHand = RenameUtil.getInHand(player);
							
							// Check Material Permissions
							if (!MaterialPermManager.checkPerms(EpicRenameCommands.INSERTLORELINE, inHand, player)) {
								// Doesn't have material permissions
								Messager.msgSenderWithConfigMsg("insertloreline.no_permission_for_material", sender);
								return true;
							}
							
							// Check Text Blacklist
							if (!Blacklists.checkTextBlacklist(args, player)) {
								// Failed text blacklist.
								Messager.msgSenderWithConfigMsg("insertloreline.blacklisted_word_found", sender);
								return true;
							}
							
							// Check Material Blacklist
							if (!Blacklists.checkMaterialBlacklist(type, player)) {
								// Failed material blacklist
								Messager.msgSenderWithConfigMsg("insertloreline.blacklisted_material_found", sender);
								return true;
							}
							
							// Check Existing Name Blacklist #81
							if (!Blacklists.checkExistingName(player)) {
								// Failed existing name blacklist
								Messager.msgSenderWithConfigMsg("insertloreline.blacklisted_existing_name_found", sender);
								return true;
							}
							
							// Check Existing Lore Blacklist #81
							if (!Blacklists.checkExistingLore(player)) {
								// Failed existing lore blacklist
								Messager.msgSenderWithConfigMsg("insertloreline.blacklisted_existing_lore_found", sender);
								return true;
							}
							
							// Check FormattingPerms
							if (!FormattingPermManager.checkPerms(EpicRenameCommands.INSERTLORELINE, args, player)) {
								// FormattingPermManager handles the message.
								return true;
							}
							
							StringBuilder textArguments = new StringBuilder("");						
							
							for (int i = 1; i < args.length; i++) {
								textArguments.append(args[i] + " ");
							}
							
							String lineToInsert = textArguments.toString().trim();
							Debug.send("Line to be inserted is: " + lineToInsert);
							
							// Issue #32
							if (!FormattingCodeCounter.checkMinColorCodes(player, lineToInsert, EpicRenameCommands.INSERTLORELINE, true)) {
								FormattingCodeCounter.sendMinNotReachedMsg(player, EpicRenameCommands.INSERTLORELINE);
								return true;
							}		
									
							if (!FormattingCodeCounter.checkMaxColorCodes(player, lineToInsert, EpicRenameCommands.INSERTLORELINE, true)) {
								FormattingCodeCounter.sendMaxReachedMsg(player, EpicRenameCommands.INSERTLORELINE);
								return true;
							}		
							// End Issue #32
							
							List<String> newLore = new ArrayList<String>();
							
							lineToInsert = Messager.color(lineToInsert);
							
							Debug.send("Colored line to insert: " + lineToInsert);
							
							ItemMeta im = inHand.getItemMeta();
							
							if (im.hasLore()) {
								List<String> oldLore = im.getLore();
								
								if (oldLore.size() < lineNumber) {
									// selected line number is too large.
									Messager.msgSenderWithConfigMsg("insertloreline.invalid_number", sender);
									return true;
								} 
								
								// Lore lines are from 0-x instead of 1-x
								lineNumber = lineNumber - 1;
								
								for (int i = 0; i < oldLore.size(); i++) {
									if (i == lineNumber) {
										newLore.add(lineToInsert);
										newLore.add(oldLore.get(i));
									} else {
										newLore.add(oldLore.get(i));
									}
								}
								
								im.setLore(newLore);
								inHand.setItemMeta(im);
								// SUCCESS
								Messager.msgSenderWithConfigMsg("insertloreline.success", sender);
								return true;								
							} else {
								// Item doesn't have any lore
								Messager.msgSenderWithConfigMsg("insertloreline.has_no_lore", sender);
								return true;
							}
						} else {
							// Must have more than one argument
							Messager.msgSenderWithConfigMsg("insertloreline.wrong_args", sender);
							return true;
						}
					} else {
						// Plugin is disabled in this world.
						Messager.msgSenderWithConfigMsg("insertloreline.disabled_world", sender);
						return true;
					}					
				} else {
					// Not a player
					Messager.msgSenderWithConfigMsg("insertloreline.wrong_sender", sender);
					return true;
				}
			} else {
				// No permission
				Messager.msgSenderWithConfigMsg("insertloreline.no_permission", sender);
				return true;
			}
		}
		
		return false;
	}

}
