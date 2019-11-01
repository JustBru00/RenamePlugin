package com.gmail.justbru00.epic.rename.configuration;

import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.PluginFile;

/**
 * ISSUE #113
 * @author Justin Brubaker
 *
 */
public class ConfigurationManager {

	public static final int CONFIG_VERSION = 7;
	public static final int MESSAGES_VERSION = 11;
	private static FileConfiguration config = null;
	private static PluginFile messages = null;
	
	/**
	 * Checks the config version number vs the one this version of EpicRename has.
	 * @return True if it needs updated, false if it doesn't.
	 */
	public static boolean doesConfigYmlNeedUpdated() {
		if (config == null) {
			config = Main.getInstance().getConfig();
		}
		
		return config.getInt("config_version") < CONFIG_VERSION;	
	}
	
	/**
	 * Checks the messages.yml version vs the one this version of Epicrename has.
	 * @return
	 */
	public static boolean doesMessagesYmlNeedUpdated() {
		if (messages == null) {
			messages = Main.getMessagesYmlFile();
		}
		
		return messages.getInt("messages_yml_version") < MESSAGES_VERSION;
	}
	
	/**
	 * Tries to add only new values to the file. These must be set manually in the ConfigUpdater file.
	 * Doesn't touch old values.
	 */
	public static void updateConfigYml() {
		ConfigUpdater.updateConfigYml();
	}
	
	/**
	 * Copies new values from the messages.yml file in the jar to the server messages.yml file.
	 * Doesn't touch old values.
	 */
	public static void updateMessagesYml() {
		ConfigUpdater.updateMessagesYml();
	}
	
}
