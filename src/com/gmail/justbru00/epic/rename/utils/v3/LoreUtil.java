/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.EcoMessage;
import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class LoreUtil {
	
	@SuppressWarnings("deprecation")
	public static void setLoreLine(int lineNumber, Player player, String[] args) {		
		Debug.send("LoreUtil#setLoreLine() start.");
		StringBuilder builder = new StringBuilder("");
		
		lineNumber = lineNumber - 1;
		
		for (int i = 1; i < args.length; i++) {
			builder.append(args[i] + " ");
		}
		
		
		
		String loreToBeSet = builder.toString().trim();
		Debug.send("Text to set is: " + loreToBeSet);
		List<String> newLore = new ArrayList<String>();
	
		loreToBeSet = Messager.color(loreToBeSet);
		
		Debug.send("Colored args are: " + loreToBeSet);
		
		ItemStack inHand = RenameUtil.getInHand(player);						
		ItemMeta im = inHand.getItemMeta();
		
		if (im.hasLore()) {
			
			Debug.send("Item has lore");
			
			List<String> oldLore = im.getLore();			
			
			try {
				
				oldLore.set(lineNumber, loreToBeSet); // ERROR WILL BE CAUSED IF BIGGER
				
				newLore = oldLore;
				Debug.send("Line number " + lineNumber + " fits in the current lore.");			
			} catch (IndexOutOfBoundsException e) {
				Debug.send("Line number is bigger than current size.");
				
				// Debug
				if (Main.debug) for (String item : oldLore) {Debug.send("oldLore has: " + item);}
				
				for (int i = 0; i < oldLore.size(); i++) { // Fill new lore with old stuff
					newLore.add(oldLore.get(i));
				}
				
				// Debug
				if (Main.debug) for (String item : newLore) {Debug.send("newLore has: " + item);}
				
				for (int i = oldLore.size() - 1; i <= lineNumber; i++) { // Expand new lore to proper size
					newLore.add("");
				}
				
				newLore.set(lineNumber, loreToBeSet);
			}
			
			im.setLore(newLore);
			inHand.setItemMeta(im);
			if (Main.USE_NEW_GET_HAND) {
				player.getInventory().setItemInMainHand(inHand);
			} else {
				player.setItemInHand(inHand);
			}
			
		} else { // Item has no lore
			Debug.send("Item has no lore D:");
			
			for (int i = 0; i <= lineNumber; i++) {
				newLore.add("");
			}
			
			Debug.send("New Lore size is: " + newLore.size());
			
			newLore.set(lineNumber, loreToBeSet);
			
			im.setLore(newLore);
			inHand.setItemMeta(im);
			if (Main.USE_NEW_GET_HAND) {
				player.getInventory().setItemInMainHand(inHand);
			} else {
				player.setItemInHand(inHand);
			}
		}
		
	}

	@SuppressWarnings("deprecation")
	/**
	 * Handles the lore command.
	 * 
	 * @param args
	 * @param player
	 */
	public static void loreHandle(String[] args, Player player) {
		if (Blacklists.checkTextBlacklist(args)) {
			Debug.send("Passed Text Blacklist");
			if (Blacklists.checkMaterialBlacklist(RenameUtil.getInHand(player).getType())) {
				Debug.send("Passed Material Blacklist");

				ItemStack inHand = RenameUtil.getInHand(player);

				if (inHand.getType() != Material.AIR) {
					Debug.send("Passed Air check");

					if ((player.hasPermission("epicrename.lore." + inHand.getType().toString()))
							|| player.hasPermission("epicrename.lore.*")) {

						EcoMessage ecoStatus = EconomyManager.takeMoney(player, EpicRenameCommands.LORE);

						if (ecoStatus == EcoMessage.TRANSACTION_ERROR) {
							return;
						}

						ItemStack toLore = inHand;
						ItemMeta toLoreMeta = toLore.getItemMeta();
						toLoreMeta.setLore(LoreUtil.buildLoreFromArgs(args));
						toLore.setItemMeta(toLoreMeta);

						if (Main.USE_NEW_GET_HAND) { // Use 1.9+ method
							player.getInventory().setItemInMainHand(toLore);
							Messager.msgPlayer(Main.getMsgFromConfig("lore.success"), player);
							return;
						} else { // Use older method.
							player.setItemInHand(toLore);
							Messager.msgPlayer(Main.getMsgFromConfig("lore.success"), player);
							return;
						}

					} else {
						Messager.msgPlayer(Main.getMsgFromConfig("lore.no_permission_for_material"), player);
						return;
					}
				} else {
					Messager.msgPlayer(Main.getMsgFromConfig("lore.cannot_lore_air"), player);
					return;
				}
			} else {
				Messager.msgPlayer(Main.getMsgFromConfig("lore.blacklisted_material_found"), player);
				return;
			}
		} else {
			Messager.msgPlayer(Main.getMsgFromConfig("lore.blacklisted_word_found"), player);
			return;
		}
	}

	/**
	 * Takes the command args and changes them to a ArrayList with multible
	 * lines and color
	 * 
	 * @param args
	 *            The args you want to change.
	 * @return An ArrayList with line breaks at every '|'
	 */
	public static List<String> buildLoreFromArgs(String[] args) {
		List<String> toBeLore = new ArrayList<String>();

		StringBuilder builder = new StringBuilder("");
		String completeArgs = "";

		for (String item : args) {
			builder.append(item + " ");
		}

		completeArgs = builder.toString();

		int lastBreak = 0;

		for (int i = 0; i < completeArgs.length(); i++) {

			char testing = completeArgs.charAt(i);

			if (testing == '|') {
				toBeLore.add(completeArgs.substring(lastBreak, i));
				lastBreak = i;
			}
		}

		toBeLore.add(completeArgs.substring(lastBreak, completeArgs.length()));

		List<String> loreToReturn = new ArrayList<String>();

		for (String item : toBeLore) {
			loreToReturn.add(Messager.color(item.replace("|", "")));
		}

		return loreToReturn;
	}

}
