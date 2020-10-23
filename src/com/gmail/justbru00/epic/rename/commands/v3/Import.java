/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.justbru00.epic.rename.exceptions.EpicRenameOnlineExpiredException;
import com.gmail.justbru00.epic.rename.exceptions.EpicRenameOnlineNotFoundException;
import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.ItemSerialization;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.EpicRenameOnlineAPI;

/**
 * Created for #106
 * 
 * @author Justin Brubaker
 *
 */
public class Import implements CommandExecutor {

	private static ArrayList<UUID> playersWhoHaveConfirmed = new ArrayList<UUID>();

	/**
	 * Usage:
	 * 
	 * import <hand,inventory> <link,rawyaml>
	 * 
	 * Importing an inventory forces a /import confirm before allowing a player to
	 * import once per server restart. Importing to your hand only works if the hand
	 * is empty.
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
						if (args.length == 2) {

							if (mainHand == null || mainHand.getType() == Material.AIR) {

								String link = args[1];
								String textFromWeb = null;

								try {
									textFromWeb = EpicRenameOnlineAPI.getTextFromURL(link).get();
									Debug.send("Got: " + textFromWeb);
								} catch (IOException e) {
									Debug.send("Failed to GET text data from URL.");
									Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
											.replace("{link}", link).replace("{error}", "IOException. Turn on debugging with /epicrename debug."), sender);
									Debug.send("[Import] " + Main.getStackTrace(e));
									return true;
								} catch (NoSuchElementException e2) {
									Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
											.replace("{link}", link).replace("{error}", "Text received from server is null."), sender);
									return true;
								} catch (EpicRenameOnlineExpiredException e) {
									Debug.send("EpicRenameOnlineExpiredException - Link has expired.");
									Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
											.replace("{link}", link).replace("{error}", "EpicRenameOnline reports that the link has expired."), sender);
									return true;
								} catch (EpicRenameOnlineNotFoundException e) {
									Debug.send("EpicRenameOnlineNotFoundException - Link doesn't exist.");
									Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
											.replace("{link}", link).replace("{error}", "EpicRenameOnline reports that the link doesn't exist."), sender);
									return true;
								}

								ItemStack imported = ItemSerialization.toItem(textFromWeb);

								if (imported == null) {
									// FAILED
									Messager.msgSenderWithConfigMsg("import.yaml_fail", sender);
									return true;
								}

								inv.setItemInMainHand(imported);
								Messager.msgSenderWithConfigMsg("import.success", sender);
								return true;
							} else {
								Messager.msgSenderWithConfigMsg("import.full_hand", sender);
								return true;
							}
						} else {
							Messager.msgSenderWithConfigMsg("import.wrong_args_hand", sender);
							return true;
						}
					} else if (args[0].equalsIgnoreCase("inventory") || args[0].equalsIgnoreCase("inv")
							|| args[0].equalsIgnoreCase("i")) {
						// /import inventory

						// Confirmation
						if (!playersWhoHaveConfirmed.contains(p.getUniqueId())) {
							Messager.msgSenderWithConfigMsg("import.not_confirmed", sender);
							return true;
						}

						if (args.length == 2) {

							String link = args[1];
							String textFromWeb = null;

							try {
								textFromWeb = EpicRenameOnlineAPI.getTextFromURL(link).get();	
								Debug.send("Got :" + textFromWeb);
							} catch (IOException e) {
								Debug.send("Failed to GET text data from URL.");
								Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
										.replace("{link}", link).replace("{error}", "IOException. Turn on debugging with /epicrename debug."), sender);
								Debug.send("[Import] " + Main.getStackTrace(e));
								return true;
							} catch (NoSuchElementException e2) {
								Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
										.replace("{link}", link).replace("{error}", "Text received from server is null."), sender);
								return true;
							} catch (EpicRenameOnlineExpiredException e) {
								Debug.send("EpicRenameOnlineExpiredException - Link has expired.");
								Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
										.replace("{link}", link).replace("{error}", "EpicRenameOnline reports that the link has expired."), sender);
								return true;
							} catch (EpicRenameOnlineNotFoundException e) {
								Debug.send("EpicRenameOnlineNotFoundException - Link doesn't exist.");
								Messager.msgSender(Main.getMsgFromConfig("import.failed_to_get_data")
										.replace("{link}", link).replace("{error}", "EpicRenameOnline reports that the link doesn't exist."), sender);
								return true;
							}

							ItemSerialization.fillInventoryFromString(textFromWeb, p);

							Messager.msgSenderWithConfigMsg("import.success", sender);
							return true;
						} else {
							Messager.msgSenderWithConfigMsg("import.wrong_args_inventory", sender);
							return true;
						}
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
								Messager.msgSenderWithConfigMsg("import.yaml_fail", sender);
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
					} else if (args[0].equalsIgnoreCase("confirm")) {
						playersWhoHaveConfirmed.add(p.getUniqueId());
						Messager.msgSenderWithConfigMsg("import.confirmed", sender);
						return true;
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
	}

}
