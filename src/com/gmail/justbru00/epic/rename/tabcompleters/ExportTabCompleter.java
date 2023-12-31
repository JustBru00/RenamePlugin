package com.gmail.justbru00.epic.rename.tabcompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ExportTabCompleter implements TabCompleter {
	
	private ArrayList<String> exportFirstArgumentList = new ArrayList<String>();
	private ArrayList<String> empty = new ArrayList<String>();
	
	public ExportTabCompleter() {
		exportFirstArgumentList.add("hand");
		exportFirstArgumentList.add("inventory");
	}	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!command.getName().equalsIgnoreCase("export")) {
			return null;
		}		
		
		if (args.length == 1) {			
			if (!args[0].equals("")) {
				ArrayList<String> completion = new ArrayList<String>();
				
				for (String first : exportFirstArgumentList) {
					if (first.toLowerCase().startsWith(args[0].toLowerCase())) {
						completion.add(first);
					}
				}
				
				return completion;				
			} else {
				return exportFirstArgumentList;
			}
		}		
		
		return empty;
	}

}
