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
import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.LoreUtil;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.RenameUtil;
import com.gmail.justbru00.epic.rename.utils.v3.WorldChecker;

public class SetLoreLine implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("setloreline")) { // Start
																	// /setloreline
																	// code
			if (sender.hasPermission("epicrename.setloreline")) {
				if (sender instanceof Player) {

					Player player = (Player) sender;

					if (WorldChecker.checkWorld(player)) {

						if (args.length > 1) {

							int lineNumber = -1;

							try {
								lineNumber = Integer.parseInt(args[0]);
							} catch (Exception e) {
								Debug.send(e.getMessage());
								Messager.msgPlayer(Main.getMsgFromConfig("setloreline.not_an_int"), player);
								return true;
							}
							
							// Issue #80
							if (lineNumber <= 0) {
								Debug.send("[SetLoreLine] The number " + lineNumber + " is below or equal to 0.");
								Messager.msgPlayer(Main.getMsgFromConfig("setloreline.invalid_number"), player);
								return true;
							}
							// End Issue #80
							
							// Issue #95
							int maxLines = Main.getInstance().getConfig().getInt("setloreline_max_lines");
							if (lineNumber > maxLines) {
								Debug.send("[SetLoreLine] The number " + lineNumber + " is greater than " + maxLines + ".");
								Messager.msgPlayer(Main.getMsgFromConfig("setloreline.invalid_number"), player);
								return true;
							}
							// End Issue #95
							
							if (args.length == 1) {
								Messager.msgPlayer(Main.getMsgFromConfig("setloreline.provide_text"), player);
								return true;
							}

							if (RenameUtil.getInHand(player).getType() == Material.AIR
									|| RenameUtil.getInHand(player) == null) {

								Messager.msgPlayer(Main.getMsgFromConfig("setloreline.cannot_edit_air"), player);

								return true;
							}

							LoreUtil.setLoreLine(lineNumber, player, args);

							return true;
						} else {
							Messager.msgPlayer(Main.getMsgFromConfig("setloreline.wrong_args"), player);
							return true;
						}
					} else {
						Messager.msgSender(Main.getMsgFromConfig("setloreline.disabled_world"), sender);
						return true;
					}
				} else {
					Messager.msgSender(Main.getMsgFromConfig("setloreline.wrong_sender"), sender);
					return true;
				}
			} else {
				Messager.msgSender(Main.getMsgFromConfig("setloreline.no_permission"), sender);
				return true;
			}
		} // Stop /setloreline code.

		return false;
	}

}
