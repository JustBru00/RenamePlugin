package com.gmail.justbru00.epic.rename.configuration;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import com.gmail.justbru00.epic.rename.main.v3.Main;
import com.gmail.justbru00.epic.rename.utils.v3.Debug;
import com.gmail.justbru00.epic.rename.utils.v3.Messager;
import com.gmail.justbru00.epic.rename.utils.v3.PluginFile;

/**
 * ISSUE #113
 * Is there a better way to do what this class does? Definitely, although google
 * doesn't want to tell me, hence this class.
 * 
 * @author Justin Brubaker
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
		
		updateConfigYmlBoolean("xp.use", false);
		updateConfigYmlInteger("xp.costs.rename", 55);
		updateConfigYmlInteger("xp.costs.lore", 55);
		updateConfigYmlInteger("xp.costs.glow", 55);

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
		updateConfigYmlInteger("formatting_code_limit.insertloreline.min", 0);
		updateConfigYmlInteger("formatting_code_limit.insertloreline.max", 30);		
		updateConfigYmlInteger("formatting_code_limit.addloreline.min", 0);
		updateConfigYmlInteger("formatting_code_limit.addloreline.max", 30);

		updateConfigYmlBoolean("replace_underscores", false);
		updateConfigYmlBoolean("add_trailing_space_to_rename", false);
		updateConfigYmlBoolean("add_leading_space_to_rename", false);

		updateConfigYmlBoolean("per_world", false);

		updateConfigYmlStringList("enabled_worlds", "world", "world_nether", "world_the_end");

		updateConfigYmlBoolean("disable_bypass_messages", false);

		updateConfigYmlBoolean("disable_grindstone_for_glowing_items", false);
		
		updateConfigYmlString("command_argument.prefixes.rename", "");
		updateConfigYmlString("command_argument.prefixes.lore.each_line", "");
		updateConfigYmlString("command_argument.suffixes.rename", "");
		updateConfigYmlString("command_argument.suffixes.lore.each_line", "");
		
		updateConfigYmlBoolean("block_&x_color_codes", true);
		updateConfigYmlBoolean("convert_legacy_glowing_to_modern_glowing", true);
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
				messages.save();
			}
		} else {
			// Config value cannot be found.
			messages.set("messages_yml_version", currentMessagesVersion);
			messages.save();
		}

		// Add new values if needed.
		// /epicrename
		updateMessagesYmlString("epicrename.no_permission",
				"&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("epicrename.no_args", "&cType /epicrename help for commands. (No Arguments)");
		updateMessagesYmlString("epicrename.license", "&6View license information at: http://bit.ly/2eMknxx");
		updateMessagesYmlStringList("epicrename.help",
				"&6/rename <name>", "&6/lore <lore>",
				"&6/setloreline <linenum> <text>",
				"&6/removeloreline <linenum>",
				"&6/insertloreline <beforeLineNum> <text>",
				"&6/glow", "&6/removeglow",
				"&6/export <hand,inventory>",
				"&6/import <hand,inventory> <webLink>",
				"&6/import raw <rawYAML>",
				"&6/removename",
				"&6/removelore",
				"&6/hideenchantments",
				"&6/unhideenchantments",
				"&6/addloreline <text>",
				"&6/editname",
				"&6/editlore",
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
		updateMessagesYmlString("rename.log", "&aThe player &f{player} &ajust renamed an item to: &f\"{new_name}&f\"&a.");
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
		
		// /insertloreline
		updateMessagesYmlString("insertloreline.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("insertloreline.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("insertloreline.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("insertloreline.wrong_args", "&cYou need to place some arguments after /insertloreline. /insertloreline <beforeLineNumber> <text>");
		updateMessagesYmlString("insertloreline.has_no_lore", "&cThat item has no lore.");
		updateMessagesYmlString("insertloreline.success", "&aInserted line of lore successfully.");
		updateMessagesYmlString("insertloreline.invalid_number", "&cSorry that line number is not valid.");
		updateMessagesYmlString("insertloreline.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("insertloreline.blacklisted_existing_name_found",
				"&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("insertloreline.blacklisted_existing_lore_found",
				"&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("insertloreline.blacklisted_word_found", "&cSorry that name contains a blacklisted word.");
		updateMessagesYmlString("insertloreline.no_permission_for_material", "&cSorry you don't have permission for that material.");
		updateMessagesYmlString("insertloreline.cannot_edit_air", "&cSorry you can't relore nothing.");
		updateMessagesYmlString("insertloreline.not_an_int", "&cYou need to provide an integer as the first argument.");

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

		// /removename
		updateMessagesYmlString("removename.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("removename.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("removename.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("removename.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("removename.blacklisted_existing_name_found", "&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("removename.blacklisted_existing_lore_found", "&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("removename.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("removename.no_permission_for_material", "&cSorry you don't have permission for that material.");
		updateMessagesYmlString("removename.success", "&aRemoved the display name from the item successfully.");
		
		// /removelore
		updateMessagesYmlString("removelore.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("removelore.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("removelore.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("removelore.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("removelore.blacklisted_existing_name_found", "&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("removelore.blacklisted_existing_lore_found", "&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("removelore.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("removelore.no_permission_for_material", "&cSorry you don't have permission for that material.");
		updateMessagesYmlString("removelore.success", "&aRemoved the lore from the item successfully.");
		
		// /hideenchantments
		updateMessagesYmlString("hideenchantments.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("hideenchantments.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("hideenchantments.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("hideenchantments.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("hideenchantments.blacklisted_existing_name_found", "&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("hideenchantments.blacklisted_existing_lore_found", "&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("hideenchantments.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("hideenchantments.no_permission_for_material", "&cSorry you don't have permission for that material.");
		updateMessagesYmlString("hideenchantments.success", "&aEnchantments on this item have been hidden.");
		
		// /unhideenchantments
		updateMessagesYmlString("unhideenchantments.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("unhideenchantments.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("unhideenchantments.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("unhideenchantments.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("unhideenchantments.blacklisted_existing_name_found", "&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("unhideenchantments.blacklisted_existing_lore_found", "&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("unhideenchantments.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("unhideenchantments.no_permission_for_material", "&cSorry you don't have permission for that material.");
		updateMessagesYmlString("unhideenchantments.success", "&aEnchantments on this item have been unhidden.");		
		
		// /addloreline
		updateMessagesYmlString("addloreline.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("addloreline.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("addloreline.wrong_args", "&cYou need to place some arguments after /addloreline. /addloreline <text>");
		updateMessagesYmlString("addloreline.blacklisted_word_found", "&cSorry that lore contains a blacklisted word.");
		updateMessagesYmlString("addloreline.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("addloreline.blacklisted_existing_name_found", "&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("addloreline.blacklisted_existing_lore_found", "&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("addloreline.cannot_edit_air", "&cSorry you can't relore nothing.");
		updateMessagesYmlString("addloreline.no_permission_for_material", "&cSorry you don''t have permission for that material.");
		updateMessagesYmlString("addloreline.success", "&aAdded lore to the item successfully.");
		updateMessagesYmlString("addloreline.disabled_world", "&cYou are in a disabled world.");
		
		// /editname
		updateMessagesYmlString("editname.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("editname.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("editname.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("editname.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("editname.blacklisted_existing_name_found", "&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("editname.blacklisted_existing_lore_found", "&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("editname.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("editname.no_permission_for_material", "&cSorry you don't have permission for that material.");
		updateMessagesYmlString("editname.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("editname.no_displayname", "&cSorry that item doesn't have a display name set.");
		updateMessagesYmlString("editname.click_to_edit", "&a&nClick on this message to start editing the name of this item.");
		
		// /editlore
		updateMessagesYmlString("editlore.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("editlore.wrong_sender", "&cSorry only a player can use that command.");
		updateMessagesYmlString("editlore.disabled_world", "&cYou are in a disabled world.");
		updateMessagesYmlString("editlore.blacklisted_material_found", "&cSorry that material is blacklisted.");
		updateMessagesYmlString("editlore.blacklisted_existing_name_found", "&cSorry that item has an existing name that is blacklisted.");
		updateMessagesYmlString("editlore.blacklisted_existing_lore_found", "&cSorry that item has an existing line of lore that is blacklisted.");
		updateMessagesYmlString("editlore.cannot_edit_air", "&cSorry you can't edit nothing.");
		updateMessagesYmlString("editlore.no_permission_for_material", "&cSorry you don't have permission for that material.");
		updateMessagesYmlString("editlore.no_permission", "&cSorry you don't have permission to perform that command.");
		updateMessagesYmlString("editlore.no_lore", "&cSorry that item doesn't have any lore set.");
		updateMessagesYmlString("editlore.lore_content_empty", "&cSorry the lore on this item appears to have no content.");
		updateMessagesYmlString("editlore.click_to_edit", "&a&nClick on this message to start editing the lore of this item.");
		
		// rename_character_limit
		updateMessagesYmlString("rename_character_limit.name_too_long",
				"&cSorry that name is too long. The character limit is {char}.");
		updateMessagesYmlString("rename_character_limit.bypass_msg",
				"&aYou just bypassed the character limit of {char}.");

		// format_code_permission
		updateMessagesYmlString("format_code_permission.no_permission",
				"&cYou don't have permission for the format code \"{code}\".");
		updateMessagesYmlString("format_code_permission.&x_color_code_blocked", "&cYou cannot use the {code} color code.");

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
		
		// xp cost
		updateMessagesYmlString("xp.bypass", "&aYou just bypassed the experience requirement.");
		updateMessagesYmlString("xp.transaction_success", "&aJust took {cost} experience points from your inventory.");
		updateMessagesYmlString("xp.transaction_error",
						"&cThere was a problem taking experience from your inventory: {error}");

		// exploit prevention
		updateMessagesYmlString("exploit_prevention.no_grindstone_with_glowing_items",
				"&cYou cannot use a grindstone on a glowing item. Please remove glow from the item first with /removeglow.");
		updateMessagesYmlString("exploit_prevention.no_anvil_with_glowing_items", "&cYou cannot use an anvil on a glowing item. Please remove glow from the item first with /removeglow.");
	}

	private static void updateMessagesYmlString(String path, String updatedValue) {
		if (!messages.isSet(path)) {
			// Path doesn't exist.
			messages.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to messages.yml.");
			messages.save();
		}
	}

	private static void updateMessagesYmlStringList(String path, String... updatedValue) {
		if (!messages.isSet(path)) {
			// Path doesn't exist.
			List<String> stringList = new ArrayList<String>();
			for (String s : updatedValue) {
				stringList.add(s);
			}

			messages.set(path, stringList);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			messages.save();
		}
	}

	private static void updateConfigYmlInteger(String path, int updatedValue) {
		if (!config.isSet(path)) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateConfigYmlString(String path, String updatedValue) {
		if (!config.isSet(path)) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateConfigYmlBoolean(String path, boolean updatedValue) {
		if (!config.isSet(path)) {
			// Path doesn't exist.
			config.set(path, updatedValue);
			Messager.msgConsole("[ConfigUpdater] Added " + path + " to config.yml.");
			Main.getInstance().saveConfig();
		}
	}

	private static void updateConfigYmlStringList(String path, String... updatedValue) {
		if (!config.isSet(path)) {
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
