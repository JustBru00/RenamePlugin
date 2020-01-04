package com.gmail.justbru00.epic.rename.configuration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.PluginFile;

/**
 * Is there a better way to do what this class does? Definitely, although google
 * doesn't want to tell me, hence this class.
 * 
 * @author Justin Brubaker
 * 
 *         ISSUE #113
 *
 */
public class ConfigUpdater {
	private static FileConfiguration config;
	private static PluginFile messages;

	public static void updateConfigYml() {
		int currentConfigVersion = ConfigurationManager.CONFIG_VERSION;
		if (config == null) {
			config = Main.getInstance().getConfig();
		}

		if (config.getInt("config_version") != 0) {
			if (config.getInt("config_version") == currentConfigVersion) {
				// The config version is up to date.
			} else {
				config.set("config_version", currentConfigVersion);
				Debug.send("[ConfigUpdater] Found config version  = " + currentConfigVersion);
				Main.getInstance().saveConfig();
			}
		} else {
			// Config value cannot be found.
			config.set("config_version", currentConfigVersion);
			Main.getInstance().saveConfig();
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
		updateConfigYmlStringList("blacklists.existingloreline",
				"a string of text to check for every lore line that is not case sensitive");

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

		updateConfigYmlBoolean("per_world", false);

		updateConfigYmlStringList("enabled_worlds", "world", "world_nether", "world_the_end");

		updateConfigYmlBoolean("disable_bypass_messages", false);

		updateConfigYmlBoolean("disable_grindstone_for_glowing_items", false);
	}

	public static void updateMessagesYml() {
		int currentMessagesVersion = ConfigurationManager.MESSAGES_VERSION;
		if (messages == null) {
			messages = Main.getMessagesYmlFile();
		}

		if (messages.getInt("messages_yml_version") != 0) {
			if (messages.getInt("messages_yml_version") == currentMessagesVersion) {
				// The config version is up to date.
			} else {
				messages.set("messages_yml_version", currentMessagesVersion);
				Debug.send("[ConfigUpdater] Found config version  = " + currentMessagesVersion);
				Main.getInstance().saveConfig();
			}
		} else {
			// Config value cannot be found.
			messages.set("messages_yml_version", currentMessagesVersion);
			Main.getInstance().saveConfig();
		}

		// Add new values if needed.
		// /epicrename
		updateMessagesYmlString("epicrename.no_permission",
				"&cSorry you don''t have permission to perform that command.");
		updateMessagesYmlString("epicrename.no_args", "&cType /epicrename help for commands. (No Arguments)");
		updateMessagesYmlString("epicrename.license", "&6View license information at: http://bit.ly/2eMknxx");
		updateMessagesYmlStringList("epicrename.help", "&6/rename <name>", "&6/lore <lore>",
				"&6/setloreline <linenum> <text>", "&6/removeloreline <linenum>", "&6/glow", "&6/removeglow",
				"&6/export <hand,inventory>", "&6/import <hand,inventory> <webLink>", "&6/import raw <rawYAML>",
				"&6See more command help at https://www.spigotmc.org/resources/epicrename.4341/",
				"&6/epicrename <help, license, reload, debug, version>");
		updateMessagesYmlString("epicrename.reload_success", "&aReloaded the plugin successfully!");
		updateMessagesYmlString("epicrename.debug_enable", "&6Plugin debug messages are now &a&lON&6.");
		updateMessagesYmlString("epicrename.debug_disable", "&6Plugin debug messages are now &4&lOFF&6.");
		updateMessagesYmlString("epicrename.invalid_args",
				"&cInvalid arguments. Please put help, license, reload, debug, or version after /epicrename.");
		updateMessagesYmlString("epicrename.version", "&6You are running &bEpic&fRename &6version {version}.");

		// /rename
		updateMessagesYmlString("rename.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("rename.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("rename.no_args", "&cYou need to place at least one argument after /rename.");
		updateMessagesYmlString("rename.blacklisted_word_found", "&cSorry that name contains a blacklisted word.");
		updateMessagesYmlString("rename.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("rename.blacklisted_existing_name_found",
				"&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("rename.blacklisted_existing_lore_found",
				"&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("rename.cannot_rename_air", "&cSorry you can't rename nothing.");
		updateMessagesYmlString("rename.no_permission_for_material",
				"&cSorry you don't have permission for that material.");
		updateMessagesYmlString("rename.success", "&aRenamed the item successfully.");
		updateMessagesYmlString("rename.log", "&aThe player &f{player} &ajust renamed an item to: &f\"{name}&f\"&a.");
		updateMessagesYmlString("rename.disabled_world", "&cYou are in a disabled world.");

		// /lore
		updateMessagesYmlString("lore.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("lore.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("lore.no_args", "&cYou need to place at least one argument after /lore.");
		updateMessagesYmlString("lore.blacklisted_word_found", "&cSorry that name contains a blacklisted word.");
		updateMessagesYmlString("lore.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("lore.blacklisted_existing_name_found",
				"&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("lore.blacklisted_existing_lore_found",
				"&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("lore.cannot_lore_air", "&cSorry you can't relore nothing.");
		updateMessagesYmlString("lore.no_permission_for_material",
				"&cSorry you don't have permission for that material.");
		updateMessagesYmlString("lore.success", "&aRelored the item successfully.");
		updateMessagesYmlString("lore.disabled_world", "&cYou are in a disabled world.");

		// /setloreline
		updateMessagesYmlString("setloreline.no_permission",
				"&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("setloreline.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("setloreline.wrong_args",
				"&cYou need to place some arguments after /setloreline. /setloreline <linenumber> <text>");
		updateMessagesYmlString("setloreline.not_an_int", "&cYou need to provide an integer as the first argument.");
		updateMessagesYmlString("setloreline.invalid_number", "&cSorry that line number is not valid.");
		updateMessagesYmlString("setloreline.provide_text",
				"&cPlease provide some text. /setloreline <linenumber> <text>");
		updateMessagesYmlString("setloreline.blacklisted_word_found", "&cSorry that name contains a blacklisted word.");
		updateMessagesYmlString("setloreline.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("setloreline.blacklisted_existing_name_found",
				"&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("setloreline.blacklisted_existing_lore_found",
				"&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("setloreline.cannot_edit_air", "&cSorry you can't relore nothing.");
		updateMessagesYmlString("setloreline.no_permission_for_material",
				"&cSorry you don't have permission for that material.");
		updateMessagesYmlString("setloreline.success", "&aRelored the item successfully.");
		updateMessagesYmlString("setloreline.disabled_world", "&cYou are in a disabled world.");

		// /removeloreline
		updateMessagesYmlString("removeloreline.no_permission",
				"&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("removeloreline.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("removeloreline.wrong_args",
				"&cYou need to place some arguments after /removeloreline. /removeloreline <linenumber>");
		updateMessagesYmlString("removeloreline.not_an_int", "&cYou need to provide an integer as the first argument.");
		updateMessagesYmlString("removeloreline.invalid_number", "&cSorry that line number is not valid.");
		updateMessagesYmlString("removeloreline.has_no_lore", "&cThat item has no lore.");
		updateMessagesYmlString("removeloreline.out_of_bounds", "&cThat line number does not exist in this lore.");
		updateMessagesYmlString("removeloreline.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("removeloreline.blacklisted_existing_name_found",
				"&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("removeloreline.blacklisted_existing_lore_found",
				"&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("removeloreline.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("removeloreline.no_permission_for_material",
				"&cSorry you don't have permission for that material.");
		updateMessagesYmlString("removeloreline.success", "&aRelored the item successfully.");
		updateMessagesYmlString("removeloreline.disabled_world", "&cYou are in a disabled world.");

		// /glow
		updateMessagesYmlString("glow.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("glow.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("glow.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("glow.no_permission_for_material",
				"&cSorry you don't have permission for that material.");
		updateMessagesYmlString("glow.success", "&aAdded glow to the item successfully.");
		updateMessagesYmlString("glow.has_enchants", "&cSorry that item has enchantments or is already glowing.");
		updateMessagesYmlString("glow.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("glow.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("glow.blacklisted_existing_name_found",
				"&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("glow.blacklisted_existing_lore_found",
				"&cSorry that item has an existing line of lore that is blacklisted.");

		// /removeglow
		updateMessagesYmlString("removeglow.no_permission",
				"&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("removeglow.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("removeglow.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("removeglow.no_permission_for_material",
				"&cSorry you don't have permission for that material.");
		updateMessagesYmlString("removeglow.success", "&aRemoved glow from the item successfully.");
		updateMessagesYmlString("removeglow.not_glowing", "&cThat item is not glowing or has other enchantments.");
		updateMessagesYmlString("removeglow.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("removeglow.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("removeglow.blacklisted_existing_name_found",
				"&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("removeglow.blacklisted_existing_lore_found",
				"&cSorry that item has an existing line of lore that is blacklisted.");

		// /import
		updateMessagesYmlString("import.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("import.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("import.no_args",
				"&cPlease provide at least one argument after /import. /import <hand, inventory, raw>");
		updateMessagesYmlString("import.wrong_args",
				"&cPlease follow the correct argument usage. /import <hand, inventory, raw>");
		updateMessagesYmlString("import.success", "&aImported successfully.");
		updateMessagesYmlString("import.failed_to_get_data", "&cFailed to import from \"{link}\". Error: {error}");
		updateMessagesYmlString("import.confirmed",
				"&aYou confirmed that you understand your items will be deleted by /import inventory. You can now use /import inventory normally until the next server restart.");
		updateMessagesYmlString("import.not_confirmed",
				"&c/import inventory deletes all items in your inventory. Please type /import confirm to confirm that you understand.");
		updateMessagesYmlString("import.full_hand",
				"&cYour main hand must be empty to import an item. Try again after clearing your main hand.");
		updateMessagesYmlString("import.yaml_fail",
				"&cFailed to import an item from the provided yaml. Make sure the yaml text is formatted correctly.");
		updateMessagesYmlString("import.wrong_args_hand",
				"&cYour arguments provided to /import hand <link> are not correct. /import hand <link>");
		updateMessagesYmlString("import.wrong_args_inventory",
				"&cYour arguments provided to /import inventory <link> are not correct. /import inventory <link>");

		// /export
		updateMessagesYmlString("export.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("export.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("export.no_args",
				"&cPlease provide at least one argument after /export. /export <hand, inventory>");
		updateMessagesYmlString("export.wrong_args",
				"&cPlease follow the correct argument usage. /export <hand, inventory>");
		updateMessagesYmlString("export.warn_public",
				"&6/export sends your item(s) to a public EpicRename.com link. By using /export you are agreeing to EpicRename.com's privacy policy which can be found at https://epicrename.com/privacy. Type \"/export confirm\" to confirm you are okay with this action. You will not be asked again until after the next server restart.");
		updateMessagesYmlString("export.success",
				"&aExported item(s) successfully. Use the following link to /import the item(s). The link expires in one month. {link}");
		updateMessagesYmlString("export.post_fail", "&cFailed to post to EpicRenameOnline: {error}");
		updateMessagesYmlString("export.confirmed",
				"&aYou confirmed that it is okay to post your item(s) publicly. You can now use /export normally until the next server restart.");
		updateMessagesYmlString("export.no_air", "&cYou cannot export nothing.");

		// rename_character_limit
		updateMessagesYmlString("rename_character_limit.name_too_long",
				"&cSorry that name is too long. The character limit is {char}.");
		updateMessagesYmlString("rename_character_limit.bypass_msg",
				"&aYou just bypassed the character limit of {char}.");

		// format_code_permission
		updateMessagesYmlString("format_code_permission.no_permission",
				"&cYou don't have permission for the format code \"{code}\".");

		// format_code_limit
		updateMessagesYmlString("format_code_limit.min_not_reached",
				"&cYou didn't reach the minimum amount of formatting codes. Please use more than {min} formatting codes.");
		updateMessagesYmlString("format_code_limit.max_reached",
				"&cYou reached the maximum amount of formatting codes. Please use less than {max} formatting codes.");
		updateMessagesYmlString("format_code_limit.bypass_min", "&aYou bypassed the minimum formatting code limit.");
		updateMessagesYmlString("format_code_limit.bypass_max", "&aYou bypassed the maximum formatting code limit.");

		// blacklists
		updateMessagesYmlString("blacklists.material.bypass", "&aYou just bypassed the material blacklist.");
		updateMessagesYmlString("blacklists.text.bypass", "&aYou just bypassed the text blacklist.");
		updateMessagesYmlString("blacklists.existingname.bypass", "&aYou just bypassed the existing name blacklist.");
		updateMessagesYmlString("blacklists.existinglore.bypass", "&aYou just bypassed the existing lore blacklist.");

		// economy
		updateMessagesYmlString("economy.bypass", "&aYou just bypassed the economy requirement.");
		updateMessagesYmlString("economy.transaction_success", "&aJust took {cost} from your balance.");
		updateMessagesYmlString("economy.transaction_error",
				"&cThere was a problem taking money from your balance: {error}");

		// exploit prevention
		updateMessagesYmlString("exploit_prevention.no_grindstone_with_glowing_items",
				"&cYou cannot use a grindstone on a glowing item. Please remove glow from the item first with /removeglow.");
	}

	private static void updateMessagesYmlInteger(String path, int updatedValue) {
		if (messages.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			messages.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to messages.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateMessagesYmlString(String path, String updatedValue) {
		if (messages.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			messages.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to messages.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateMessagesYmlStringList(String path, String... updatedValue) {
		if (messages.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			List<String> stringList = new ArrayList<String>();
			for (String s : updatedValue) {
				stringList.add(s);
			}

			messages.set(path, stringList);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateConfigYmlInteger(String path, int updatedValue) {
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateConfigYmlString(String path, String updatedValue) {
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateConfigYmlBoolean(String path, boolean updatedValue) {
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateConfigYmlStringList(String path, String... updatedValue) {
		if (config.getConfigurationSection(path) == null) {
			// Path doesn't exist.
			List<String> stringList = new ArrayList<String>();
			for (String s : updatedValue) {
				stringList.add(s);
			}

			config.set(path, stringList);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

}
