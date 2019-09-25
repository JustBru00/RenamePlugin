package com.gmail.justbru00.epic.rename.configuration;

/**
 * ISSUE #113
 * @author Justin Brubaker
 *
 */
public class ConfigurationManager {

	public static final int CONFIG_VERSION = 7;
	public static final int MESSAGES_VERSION = 11;
	
	/**
	 * Checks the config version number vs the one this version of EpicRename has.
	 * @return True if it needs updated, false if it doesn't.
	 */
	public static boolean doesConfigYmlNeedUpdated() {
		
		
		return false;
	}
	
	/**
	 * Checks the messages.yml version vs the one this version of Epicrename has.
	 * @return
	 */
	public static boolean doesMessagesYmlNeedUpdated() {
		
		
		return false;
	}
	
	/**
	 * Copies new values from the config.yml file in the jar to the server config.yml file.
	 * Doesn't touch old values.
	 */
	public static void updateConfigYml() {
		
	}
	
	/**
	 * Copies new values from the messages.yml file in the jar to the server messages.yml file.
	 * Doesn't touch old values.
	 */
	public static void updateMessagesYml() {
		
	}
	
}
