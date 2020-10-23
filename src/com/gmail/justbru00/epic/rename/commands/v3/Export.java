/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.ItemSerialization;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.EpicRenameOnlineAPI;

/**
 * Created for #105
 * @author Justin Brubaker
 *
 */
public class Export implements CommandExecutor {
	
	private static ArrayList<UUID> playersWhoHaveConfirmed = new ArrayList<UUID>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (sender.hasPermission("epicrename.export")) {
				
				if (args.length != 0) {
					if (args.length == 1) {
						String arg = args[0];
						if (arg.equalsIgnoreCase("confirm")) { // /export confirm							
							playersWhoHaveConfirmed.add(p.getUniqueId());
							
							Messager.msgSenderWithConfigMsg("export.confirmed", sender);
							return true;
						}
						
						// Check confirmation
						if (!playersWhoHaveConfirmed.contains(p.getUniqueId())) {
							Messager.msgSenderWithConfigMsg("export.warn_public", sender);
							return true;
						}
						
						if (arg.equalsIgnoreCase("hand") || arg.equalsIgnoreCase("h")) { // /export hand
							ItemStack inHand = p.getInventory().getItemInMainHand();
							
							if (inHand.getType() == Material.AIR || inHand == null) {
								Messager.msgSenderWithConfigMsg("export.no_air", sender);
								return true;
							}
							
							String theItem = ItemSerialization.toString(inHand);
							String response = null;
							try {
								response = EpicRenameOnlineAPI.paste(theItem);
							} catch (MalformedURLException e) {
								if (Main.debug) {
									e.printStackTrace();
								}
								Messager.msgSender(Main.getMsgFromConfig("export.post_fail").replace("{error}", "MalformedURLException"), sender);
								return true;
							} catch (IOException e) {
								if (Main.debug) {
									e.printStackTrace();
								}
								Messager.msgSender(Main.getMsgFromConfig("export.post_fail").replace("{error}", "IOException"), sender);
								return true;
							}
							
							if (response.startsWith("ERROR:")) {
								// FAILED
								Messager.msgSender(Main.getMsgFromConfig("export.post_fail").replace("{error}", response), sender);
							} else {
								// SUCCESS
								Messager.msgSender(Main.getMsgFromConfig("export.success").replace("{link}", response), sender);
							}			
							
							return true;
						} else if (arg.equalsIgnoreCase("inventory") || arg.equalsIgnoreCase("inv") || arg.equalsIgnoreCase("i")) { // /export inventory
							
							String theInventory = ItemSerialization.toString(p.getInventory());
							String response = null;
							try {
								response = EpicRenameOnlineAPI.paste(theInventory);
							} catch (MalformedURLException e) {
								if (Main.debug) {
									e.printStackTrace();
								}
								Messager.msgSender(Main.getMsgFromConfig("export.post_fail").replace("{error}", "MalformedURLException"), sender);
								return true;
							} catch (IOException e) {
								if (Main.debug) {
									e.printStackTrace();
								}
								Messager.msgSender(Main.getMsgFromConfig("export.post_fail").replace("{error}", "IOException"), sender);
								return true;
							}
							
							if (response.startsWith("ERROR:")) {
								// FAILED
								Messager.msgSender(Main.getMsgFromConfig("export.post_fail").replace("{error}", response), sender);
							} else {
								// SUCCESS
								Messager.msgSender(Main.getMsgFromConfig("export.success").replace("{link}", response), sender);
							}						
							
							return true;
						} else {
							Messager.msgSenderWithConfigMsg("export.wrong_args", sender);
							return true;
						}
					} else { // More than 1 argument
						Messager.msgSenderWithConfigMsg("export.wrong_args", sender);
						return true;
					}					
				} else { // Player must provide at least one argument
					Messager.msgSenderWithConfigMsg("export.no_args", sender);
					return true;
				}				
			} else { // sender doesn't have permission
				Messager.msgSenderWithConfigMsg("export.no_permission", sender);
				return true;
			}
		} else { // sender NOT Player
			Messager.msgSenderWithConfigMsg("export.wrong_sender", sender);
			return true;
		}
	}

}
