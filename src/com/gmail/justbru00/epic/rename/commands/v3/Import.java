/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.commands.v3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
	 *  import i random yaml stuff
	 *  
	 *  Importing an inventory forces a /import confirm before allowing a player to import once per server restart.
	 *  Importing to your hand only works if the hand is empty.
	 *
	 **/

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		return false;
	}

}
