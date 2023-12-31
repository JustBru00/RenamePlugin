package com.gmail.justbru00.epic.rename.tabcompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class GenericNoArgsTabCompleter implements TabCompleter {
	
	private ArrayList<String> empty = new ArrayList<String>();
	private String commandName;
	
	public GenericNoArgsTabCompleter(String _commandName) {
		commandName = _commandName;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase(commandName)) {
			return null;
		}		
		
		return empty;
	}

}
