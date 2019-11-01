package com.gmail.justbru00.epic.rename.configuration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.PluginFile;

/**
 * Is there a better way to do what this class does?
 * Definitely, although google doesn't want to tell me, hence this class.
 * @author Justin Brubaker
 * 
 * ISSUE #113
 *
 */
public class ConfigUpdater {
	private static FileConfiguration config = Main.getInstance().getConfig();
	private static PluginFile messages = Main.getMessagesYmlFile();

	public static void updateConfigYml() {				
		int currentConfigVersion = 7;
		
		if (config.getInt("config_version") != 0) {
			if (config.getInt("config_version") == currentConfigVersion) {
				// The config version is up to date.
			} else {
				config.set("config_version", currentConfigVersion);
				Debug.send("[ConfigUpdater] config.yml - Updated config_version = " + currentConfigVersion);
			}
		} else {
			// Config value cannot be found.
			config.set("config_version", currentConfigVersion);
		}
		
		
		// Add values if they are needed.	
		updateConfigYmlString("prefix", "&8[&bEpic&fRename&8] &f");
		
		updateConfigYmlBoolean("economy.use", false);
		updateConfigYmlInteger("economy.costs.rename", 100);
		updateConfigYmlInteger("economy.costs.lore", 100);
		updateConfigYmlInteger("economy.costs.glow", 100);
		
		updateConfigYmlStringList("blacklists.material", "VAILD_MATERIAL_HERE");
		updateConfigYmlStringList("blacklists.text", "some_bad_phrase_here");
		updateConfigYmlStringList("blacklists.existingname", "a name that is not case sensitive");
		updateConfigYmlStringList("blacklists.existingloreline", "a string of text to check for every lore line that is not case sensitive");
		
		updateConfigYmlBoolean("rename_character_limit.enabled", false);
		updateConfigYmlInteger("rename_character_limit.limit", 30);
		
		updateConfigYmlInteger("setloreline_max_lines", 20);
		
		updateConfigYmlBoolean("formatting_code_limit.enabled", false);
		updateConfigYmlInteger("formatting_code_limit.rename.min", 0);
		updateConfigYmlInteger("formatting_code_limit.rename.max", 30);
		updateConfigYmlInteger("formatting_code_limit.lore.min", 0);
		updateConfigYmlInteger("formatting_code_limit.lore.max", 30);
		updateConfigYmlInteger("formatting_code_limit.setloreline.min", 0);
		updateConfigYmlInteger("formatting_code_limit.setloreline.max", 30);
		
		updateConfigYmlBoolean("replace_underscores", false);
		
		updateConfigYmlBoolean("pre_world", false);
		
		updateConfigYmlStringList("enabled_worlds", "world", "world_nether", "world_the_end");
		
		updateConfigYmlBoolean("disable_bypass_messages", false);
		
		updateConfigYmlBoolean("disable_grindstone_for_glowing_items", false);
	}
	
	public static void updateMessagesYml() {
		// TODO
		
		// pseudo code
				/*
				 * if (configOptionDoesn't exist) {
				 * 	set the default value from messages.yml that is stored in the jar file.
				 * }
				 */
		
		// Set the messagesyml version to the new version number.
	}
	
	private static void updateConfigYmlInteger(String path, int updatedValue) {
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
		} 
	}
	
	private static void updateConfigYmlString(String path, String updatedValue) {
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
		} 
	}
	
	private static void updateConfigYmlBoolean(String path, boolean updatedValue) {
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
		} 
	}
	
	private static void updateConfigYmlStringList(String path, String... updatedValue) {
		List<String> stringList = new ArrayList<String>();
		for (String s : updatedValue) {
			stringList.add(s);
		}		
		
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			config.set(path, stringList);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
		} 
	}
	
}
