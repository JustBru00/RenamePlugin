package com.gmail.justbru00.epic.rename.commands.v3;

import java.util.ArrayList;
import org.bukkit.ChatColor;
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
 * 
 * @author Justin "JustBru00" Brubaker
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 * Created for Issue #86
 *
 */
public class Align implements CommandExecutor {
	
	private static final int ALIGN_LEFT = 1;
	private static final int ALIGN_CENTER = 2;
	private static final int ALIGN_RIGHT = 3;
	
	public static void main(String[] args) {
		// Test the align stuff
		ArrayList<String> text = new ArrayList<String>();
		text.add("             Swords");
		text.add("");
		text.add("Click to see all custom");
		text.add("enchantments for swords!");
		for(String s : alignStrings(text, ALIGN_CENTER)) {
			System.out.println(s);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (command.getName().equalsIgnoreCase("align")) {
			if (sender.hasPermission("epicrename.align")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					
					if (WorldChecker.checkWorld(player)) {
						ItemStack inHand = RenameUtil.getInHand(player);
						Material m = inHand.getType();
						
						// Check Material Permissions
						if (!MaterialPermManager.checkPerms(EpicRenameCommands.ALIGN, inHand, player)) {
							Messager.msgSenderWithConfigMsg("align.no_permission_for_material", sender);
							return true;
						}
						
						// Check Material Blacklist | #76
						if (!Blacklists.checkMaterialBlacklist(m, player)) {
							Messager.msgSenderWithConfigMsg("align.blacklisted_material_found", sender);
							return true;
						}
						
						// Check Existing Name Blacklist | #81
						if (!Blacklists.checkExistingName(player)) {
							Messager.msgSenderWithConfigMsg("align.blacklisted_existing_name_found", player);
							return true;
						}
						
						// Check Existing Lore Blacklist | #81
						if (!Blacklists.checkExistingLore(player)) {
							Messager.msgSenderWithConfigMsg("align.blacklisted_existing_lore_found", player);
							return true;
						}
						
						// Make sure item is not AIR or null
						if (m == Material.AIR || m == null) {
							Messager.msgSenderWithConfigMsg("align.cannot_edit_air", sender);
							return true;
						}
						
						// Example Command Usage: /align name <left || center || right>
						// /align lore <left || center || right> <1,2,3,5>
						// /align lore <left || center || right> <all>
						// /align both <left || center || right> 						
						if (args.length == 0) {
							Messager.msgSenderWithConfigMsg("align.not_enough_args", sender);
							return true;
						}						
						
						if (args.length >= 2) {				
							ArrayList<String> textToAlign = new ArrayList<String>();
							
							if (inHand.getItemMeta().hasDisplayName()) {
								textToAlign.add(inHand.getItemMeta().getDisplayName());
							} else {
								// TODO Fix this section. Need to research proper method.
								if (inHand.getItemMeta().hasLocalizedName()) {
									textToAlign.add(inHand.getItemMeta().getLocalizedName());
								} else {
									textToAlign.add(Messager.color("&rERROR - NO LOCAL NAME FOUND"));
								}
							}
								
							if (inHand.getItemMeta().hasLore()) {
								for (String s : inHand.getItemMeta().getLore()) {
									textToAlign.add(s);
								}
							} else {
								// Well nothing /shrug
							}
							
							if (args[0].equalsIgnoreCase("name")) {
								if (args[1].equalsIgnoreCase("left")) {
									ArrayList<String> aligned = alignStrings(textToAlign, ALIGN_LEFT);
									
									ItemMeta meta = inHand.getItemMeta();
									meta.setDisplayName(aligned.get(0));
									
									/*if (aligned.size() > 1) {
										// Lore in this thing
										List<String> lore = new ArrayList<String>();
										for (int i = 1; i < aligned.size(); i++) {
											lore.add(aligned.get(i));
										}
										meta.setLore(lore);
									} */
									
									inHand.setItemMeta(meta);
									if (Main.USE_NEW_GET_HAND) { // Use 1.9+ method
										player.getInventory().setItemInMainHand(inHand);
									} else { // Use older method.
										player.setItemInHand(inHand);
									}
									Messager.msgSenderWithConfigMsg("align.name_aligned_left_success", sender);
									return true;
								} else if (args[1].equalsIgnoreCase("center")) {
									ArrayList<String> aligned = alignStrings(textToAlign, ALIGN_CENTER);
									
									ItemMeta meta = inHand.getItemMeta();
									meta.setDisplayName(aligned.get(0));
									
									/*if (aligned.size() > 1) {
										// Lore in this thing
										List<String> lore = new ArrayList<String>();
										for (int i = 1; i < aligned.size(); i++) {
											lore.add(aligned.get(i));
										}
										meta.setLore(lore);
									} */
									
									inHand.setItemMeta(meta);
									if (Main.USE_NEW_GET_HAND) { // Use 1.9+ method
										player.getInventory().setItemInMainHand(inHand);
									} else { // Use older method.
										player.setItemInHand(inHand);
									}
									Messager.msgSenderWithConfigMsg("align.name_aligned_center_success", sender);
									return true;
								} else if (args[1].equalsIgnoreCase("right")) {
									// TODO
									return true;
								} else {
									Messager.msgSenderWithConfigMsg("align.incorrect_name_args", sender);
									return true;
								}
							} else if (args[0].equalsIgnoreCase("lore")) {
								if (args[1].equalsIgnoreCase("left")) {
									// TODO
									return true;
								} else if (args[1].equalsIgnoreCase("center")) {
									// TODO
									return true;
								} else if (args[1].equalsIgnoreCase("right")) {
									// TODO
									return true;
								} else {
									Messager.msgSenderWithConfigMsg("align.incorrect_lore_args", sender);
									return true;
								}
							} else if (args[0].equalsIgnoreCase("both")) {
								if (args[1].equalsIgnoreCase("left")) {
									// TODO
									return true;
								} else if (args[1].equalsIgnoreCase("center")) {
									// TODO
									return true;
								} else if (args[1].equalsIgnoreCase("right")) {
									// TODO
									return true;
								} else {
									Messager.msgSenderWithConfigMsg("align.incorrect_both_args", sender);
									return true;
								}
							} else {
								Messager.msgSenderWithConfigMsg("align.incorrect_args", sender);
								return true;
							}
						} else {
							Messager.msgSenderWithConfigMsg("align.not_enough_args", sender);
							return true;
						}
					} else {
						Messager.msgSenderWithConfigMsg("align.disabled_world", sender);
						return true;
					}
				} else {
					Messager.msgSenderWithConfigMsg("align.wrong_sender", sender);
					return true;
				}
			} else {
				Messager.msgSenderWithConfigMsg("align.no_permission", sender);
				return true;
			}
		}
		
		return false;
	}
	/**
	 * Aligns text to the LEFT, CENTER, or RIGHT
	 * Ignores color codes.
	 * @param itemTextList
	 * @param alignmentScheme
	 * @return
	 */
	public static ArrayList<String> alignStrings(ArrayList<String> itemTextList, int alignmentScheme) {
		
		ArrayList<String> toReturn = new ArrayList<String>();
		
		if (alignmentScheme == ALIGN_LEFT) {
			for (String s : itemTextList) {
				toReturn.add(s.trim());
			}
		} else if (alignmentScheme == ALIGN_CENTER) {
			int placeholder = 0;
			for (String s : itemTextList) {
				itemTextList.set(placeholder, s.trim());
				placeholder++;
			}
			
			toReturn = itemTextList;
			ArrayList<String> colorRemovedStrings = new ArrayList<String>();
			for (String s : itemTextList) {
				colorRemovedStrings.add(ChatColor.stripColor(Messager.color(s)));
			}
			
			int longestText = 0;
			
			// Find longest string
			for (String s : colorRemovedStrings) {
				if (s.length() > longestText) {
					longestText = s.length();
				}
			}
			
			int current = 0;
			
			for (String s : colorRemovedStrings) {				
				if (s.length() != longestText) {
					int totalToAdd = longestText - s.length();				
						
					String coloredValue = itemTextList.get(current);
						
					String addLeft = "";
					String addRight = "";
						
					for (int i = 0; i < totalToAdd / 2; i++) {
						addLeft = addLeft + " ";
					}
						
					for (int i = 0; i < totalToAdd / 2; i++) {
						addRight = addRight + " ";
					}				
						
					coloredValue = addLeft + coloredValue + addRight;
					toReturn.set(current, coloredValue);
					
				} else {
					// Doesn't need space padding
					toReturn.set(current, itemTextList.get(current));
				}
				current++;
			}			
		} else if (alignmentScheme == ALIGN_RIGHT) {
			int placeholder = 0;
			for (String s : itemTextList) {
				itemTextList.set(placeholder, s.trim());
				placeholder++;
			}
			
			toReturn = itemTextList;
			ArrayList<String> colorRemovedStrings = new ArrayList<String>();
			for (String s : itemTextList) {
				colorRemovedStrings.add(ChatColor.stripColor(Messager.color(s)));
			}
			
			int longestText = 0;
			
			// Find longest string
			for (String s : colorRemovedStrings) {
				if (s.length() > longestText) {
					longestText = s.length();
				}
			}
			
			int current = 0;
			
			for (String s : colorRemovedStrings) {				
				if (s.length() != longestText) {
					int totalToAdd = longestText - s.length();				
						
					String coloredValue = itemTextList.get(current);
						
					String addLeft = "";
					
						
					for (int i = 0; i < totalToAdd; i++) {
						addLeft = addLeft + " ";
					}						
						
					coloredValue = addLeft + coloredValue;
					toReturn.set(current, coloredValue);					
				} else {
					// Doesn't need space padding
					toReturn.set(current, itemTextList.get(current));
				}
				current++;
			}
		}
		
		for (String value : toReturn) {
			Debug.send(value);
		}
		
		return toReturn;
	}

}
