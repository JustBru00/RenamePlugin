package com.gmail.justbru00.epic.rename.tabcompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class GenericOneArgTabCompleter implements TabCompleter {

	private ArrayList<String> empty = new ArrayList<String>();
	private ArrayList<String> firstArgument = new ArrayList<String>();
	private String commandName;
	
	public GenericOneArgTabCompleter(String _commandName, String _firstArgument) {
		firstArgument.add(_firstArgument);
		commandName = _commandName;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase(commandName)) {
			return null;
		}		
		
		if (args.length == 1) {			
			if (args[0].equals("")) {
				return firstArgument;			
			}
		}		
		
		return empty;
	}

}
