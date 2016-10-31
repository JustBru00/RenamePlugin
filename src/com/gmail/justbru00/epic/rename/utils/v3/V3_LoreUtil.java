package com.gmail.justbru00.epic.rename.utils.v3;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.V3_EcoMessage;
import com.gmail.justbru00.epic.rename.enums.v3.V3_EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.V3_Main;
import com.gmail.justbru00.epic.rename.utils.Debug;
import com.gmail.justbru00.epic.rename.utils.Messager;

public class V3_LoreUtil {

	@SuppressWarnings("deprecation")
	/**
	 * Handles the lore command.
	 * 
	 * @param args
	 * @param player
	 */
	public static void loreHandle(String[] args, Player player) {
		if (V3_Blacklists.checkTextBlacklist(args)) {
			Debug.send("Passed Text Blacklist");
			if (V3_Blacklists.checkMaterialBlacklist(V3_RenameUtil.getInHand(player).getType())) {
				Debug.send("Passed Material Blacklist");

				ItemStack inHand = V3_RenameUtil.getInHand(player);

				if (inHand.getType() != Material.AIR) {
					Debug.send("Passed Air check");

					if ((player.hasPermission("epicrename.lore." + inHand.getType().toString()))
							|| player.hasPermission("epicrename.lore.*")) {

						V3_EcoMessage ecoStatus = V3_EconomyManager.takeMoney(player, V3_EpicRenameCommands.LORE);

						if (ecoStatus == V3_EcoMessage.TRANSACTION_ERROR) {
							return;
						}

						ItemStack toLore = inHand;
						ItemMeta toLoreMeta = toLore.getItemMeta();
						toLoreMeta.setLore(V3_LoreUtil.buildLoreFromArgs(args));
						toLore.setItemMeta(toLoreMeta);

						if (V3_Main.USE_NEW_GET_HAND) { // Use 1.9+ method
							player.getInventory().setItemInMainHand(toLore);
							Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.success"), player);
							return;
						} else { // Use older method.
							player.setItemInHand(toLore);
							Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.success"), player);
							return;
						}

					} else {
						Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.no_permission_for_material"), player);
						return;
					}
				} else {
					Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.cannot_rename_air"), player);
					return;
				}
			} else {
				Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.blacklisted_material_found"), player);
				return;
			}
		} else {
			Messager.msgPlayer(V3_Main.getMsgFromConfig("lore.blacklisted_word_found"), player);
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
