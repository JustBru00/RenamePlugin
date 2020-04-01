/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.justbru00.epic.rename.enums.v3.EcoMessage;
import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.enums.v3.XpMessage;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class RenameUtil {

	// VERSION 3

	/**
	 * 
	 * @param sender
	 *            The {@link Player} who sent the command.
	 * @param args
	 *            The arguments of the command sent to the {@link CommandExecutor}
	 * @param erc
	 *            The command that called this method.
	 */
	@SuppressWarnings("deprecation")
	public static void renameHandle(Player player, String[] args, EpicRenameCommands erc) {

		if (erc == EpicRenameCommands.RENAME) { // Command /rename handling START
			Debug.send("[RenameUtil] RenameUtil.rename() handling from /rename");

			ItemStack inHand = RenameUtil.getInHand(player); // Item in the players hand.

			if (Blacklists.checkTextBlacklist(args, player)) { // Check Text Blacklist
				Debug.send("[RenameUtil] Passed Text Blacklist");
				if (Blacklists.checkMaterialBlacklist(inHand.getType(), player)) { // Check Material Blacklist
					Debug.send("[RenameUtil] Passed Material Blacklist Check.");
					if (Blacklists.checkExistingName(player)) { // Check Existing Name Blacklist
						Debug.send("[RenameUtil] Passed Existing Name Blacklist Check.");
						if (Blacklists.checkExistingLore(player)) { // Check Existing Lore Blacklist
							Debug.send("[RenameUtil] Passed Existing Lore Blacklist Check.");
							if (CharLimit.checkCharLimit(args, player)) { // Check Character Limit
								Debug.send("[RenameUtil] Passed Character Limit Check.");
								if (FormattingPermManager.checkPerms(erc, args, player)) {
									Debug.send("[RenameUtil] Passed Format Permissions Check.");
									if (inHand.getType() != Material.AIR) { // Check != Air
										if (MaterialPermManager.checkPerms(erc, inHand, player)) { // Check for per material permissions
											
											StringBuilder builder = new StringBuilder("");
											String completeArgs = "";

											for (String item : args) {
												builder.append(item + " ");
											}
											
											completeArgs = builder.toString().trim();
											
											if (Main.getInstance().getConfig().getBoolean("replace_underscores")) {
												completeArgs = completeArgs.replace("_", " ");
												Debug.send("[RenameUtil] Replaced the underscores.");
											}
											
											// Issue #32
											if (!FormattingCodeCounter.checkMinColorCodes(player, completeArgs, erc, true)) {
												FormattingCodeCounter.sendMinNotReachedMsg(player, erc);
												return;
											}
											Debug.send("[RenameUtil] Passed FormattingCodeCounter Minimum Check.");
											
											if (!FormattingCodeCounter.checkMaxColorCodes(player, completeArgs, erc, true)) {
												FormattingCodeCounter.sendMaxReachedMsg(player, erc);
												return;
											}
											Debug.send("[RenameUtil] Passed FormattingCodeCounter Maximum Check.");
											// End Issue #32

											completeArgs = Messager.color(completeArgs);
											
											EcoMessage ecoStatus = EconomyManager.takeMoney(player,	EpicRenameCommands.RENAME);

											if (ecoStatus == EcoMessage.TRANSACTION_ERROR) {
												return;
											}
											
											// Add experience cost option #121
											XpMessage xpStatus = XpCostManager.takeXp(player, EpicRenameCommands.RENAME);
											
											if (xpStatus == XpMessage.TRANSACTION_ERROR) {
												return;
											}

											if (Main.USE_NEW_GET_HAND) { // Use 1.9+ method
												player.getInventory().setItemInMainHand(
														RenameUtil.renameItemStack(player, args, inHand));
												Messager.msgPlayer(Main.getMsgFromConfig("rename.success"), player);
												Messager.msgConsole(Main.getMsgFromConfig("rename.log")
														.replace("{player}", player.getName())
														.replace("{name}", completeArgs));
												return;
											} else { // Use older method.
												player.setItemInHand(RenameUtil.renameItemStack(player, args, inHand));
												Messager.msgPlayer(Main.getMsgFromConfig("rename.success"), player);
												Messager.msgConsole(Main.getMsgFromConfig("rename.log")
														.replace("{player}", player.getName())
														.replace("{name}", completeArgs));
												return;
											}
										} else {
											Messager.msgPlayer(
													Main.getMsgFromConfig("rename.no_permission_for_material"), player);
											return;
										}
									} else {
										Messager.msgPlayer(Main.getMsgFromConfig("rename.cannot_rename_air"), player);
										return;
									}
								} else {
									// Message handled by FormattingPermManager
									return;
								}
							} else {
								Messager.msgPlayer(Main.getMsgFromConfig("rename_character_limit.name_too_long"), player);
								return;
							}
						} else {
							Messager.msgPlayer(Main.getMsgFromConfig("rename.blacklisted_existing_lore_found"), player);
							return;
						}
					} else {
						Messager.msgPlayer(Main.getMsgFromConfig("rename.blacklisted_existing_name_found"), player);
						return;
					}
				} else {
					Messager.msgPlayer(Main.getMsgFromConfig("rename.blacklisted_material_found"), player);
					return;
				}
			} else {
				Messager.msgPlayer(Main.getMsgFromConfig("rename.blacklisted_word_found"), player);
				return;
			}
		} else {
			Debug.send(
					"What are you sending a non rename command to RenameUtil#rename() for? Either you failed at using this plugin as a API or JustBru00 failed big time.");
		}
	}

	/**
	 * 
	 * @param player
	 *            The {@link Player} who ran the command.
	 * @param args
	 *            The arguments of the command.
	 * @param toRename
	 *            The {@link ItemStack} that is being renamed.
	 * @return The renamed {@link ItemStack}.
	 */
	public static ItemStack renameItemStack(Player player, String[] args, ItemStack toRename) {

		StringBuilder builder = new StringBuilder("");
		String completeArgs = "";

		for (String item : args) {
			builder.append(item + " ");
		}

		completeArgs = builder.toString().trim();
		if (Main.getInstance().getConfig().getBoolean("replace_underscores")) {
			completeArgs = completeArgs.replace("_", " ");
			Debug.send("Replaced the underscores.");
		}
		
		// ISSUE #130
		if (Main.getInstance().getConfig().getBoolean("add_trailing_space_to_rename")) {
			completeArgs = completeArgs + " ";
			Debug.send("[RenameUtil] Added trailing space to rename arguments.");
		}
		// END ISSUE #130

		Debug.send("The args result is: " + completeArgs);

		ItemMeta im = toRename.getItemMeta();
		im.setDisplayName(Messager.color(completeArgs));
		toRename.setItemMeta(im);

		return toRename;
	}

	/**
	 * This method gets the item in the players main hand. It will use the correct
	 * method for the server version it is running on.
	 * 
	 * @param player
	 *            The player to get the item from.
	 * @return The item stack in the players hand.
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getInHand(Player player) {
		ItemStack returning = null;

		if (Main.USE_NEW_GET_HAND) {
			returning = player.getInventory().getItemInMainHand();
			return returning;
		} else {

			try {
				returning = player.getItemInHand();
			} catch (Exception e) {
				Debug.send(
						"&cProblem while getting the ItemStack inHand. Failed at player.getItemInHand() Server version problem?");
				Messager.msgConsole(
						"&cProblem while getting the ItemStack inHand. Failed at player.getItemInHand() Server version problem?");
			}

			return returning;
		}
	}

}
