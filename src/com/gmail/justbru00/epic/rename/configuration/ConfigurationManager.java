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

	public static final int CONFIG_VERSION = 10;
	public static final int MESSAGES_VERSION = 15;
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
	 * Adds any new config values to the config.yml file if the config_version value doesn't match the value of this version of EpicRename.
	 * These must be set manually in the {@link ConfigUpdater}.
	 * Doesn't touch old values.
	 */
	public static void updateConfigYml() {
		ConfigUpdater.updateConfigYml();
	}
	
	/**
	 * Adds any new messages values to the messages.yml file if the messages_version value doesn't match the value of this version of EpicRename.
	 * These must be set manually in the {@link ConfigUpdater}.
	 * Doesn't touch old values.
	 * */
	public static void updateMessagesYml() {
		ConfigUpdater.updateMessagesYml();
	}
	
}
