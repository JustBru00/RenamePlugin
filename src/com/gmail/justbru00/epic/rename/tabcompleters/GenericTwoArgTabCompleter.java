package com.gmail.justbru00.epic.rename.tabcompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class GenericTwoArgTabCompleter implements TabCompleter {

	private ArrayList<String> empty = new ArrayList<String>();
	private ArrayList<String> firstArgument = new ArrayList<String>();
	private ArrayList<String> secondArgument = new ArrayList<String>();
	private String commandName;	
	
	public GenericTwoArgTabCompleter(String _commandName, String _firstArgument, String _secondArgument) {
		firstArgument.add(_firstArgument);
		secondArgument.add(_secondArgument);
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
		
		if (args.length == 2) {			
			if (args[1].equals("")) {
				return secondArgument;			
			}
		}	
		
		return empty;
	}

}
