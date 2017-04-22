/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.enums.v3.EpicRenameCommands;
import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

public class Rename implements CommandExecutor {

	private static final EpicRenameCommands COMMAND = EpicRenameCommands.RENAME;

	// VERSION 3

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {

		if (command.getName().equalsIgnoreCase("rename")) { // Start /rename
															// code
			if (sender instanceof Player) {

				Player player = (Player) sender; // Player is sender so do stuff
													// :D

				if (WorldChecker.checkWorld(player)) {

					if (player.hasPermission("epicrename.rename")) {

						if (args.length >= 1) {

							RenameUtil.renameHandle(player, args, COMMAND);

							return true;
						} else { // No Args
							Messager.msgPlayer(Main.getMsgFromConfig("rename.no_args"), player);
							return true;
						}
					} else { // No basic permission.
						Messager.msgPlayer(Main.getMsgFromConfig("rename.no_permission"), player);
						return true;
					}
				} else {
					Messager.msgSender(Main.getMsgFromConfig("rename.disabled_world"), sender);
					return true;
				}
			} else { // Wrong sender
				Messager.msgSender(Main.getMsgFromConfig("rename.wrong_sender"), sender);
				return true;
			}
		} // End /rename code

		return false;
	}

}
