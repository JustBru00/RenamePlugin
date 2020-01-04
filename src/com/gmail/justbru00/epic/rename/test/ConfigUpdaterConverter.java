package com.gmail.justbru00.epic.rename.test;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUpdaterConverter {
	public static void main(String[] args) throws InvalidConfigurationException {		
		YamlConfiguration config = new YamlConfiguration();
		config.loadFromString("rename:\r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  no_args: '&cYou need to place at least one argument after /rename.' \r\n" + 
				"  blacklisted_word_found: '&cSorry that name contains a blacklisted word.'\r\n" + 
				"  blacklisted_material_found: '&cSorry that material is blacklisted.'\r\n" + 
				"  blacklisted_existing_name_found: '&cSorry that item has an existing name that is blacklisted.'\r\n" + 
				"  blacklisted_existing_lore_found: '&cSorry that item has an existing line of lore that is blacklisted.'\r\n" + 
				"  cannot_rename_air: '&cSorry you can''t rename nothing.'  \r\n" + 
				"  no_permission_for_material: '&cSorry you don''t have permission for that material.'\r\n" + 
				"  success: '&aRenamed the item successfully.'\r\n" + 
				"  log: '&aThe player &f{player} &ajust renamed an item to: &f\"{name}&f\"&a.'\r\n" + 
				"  disabled_world: '&cYou are in a disabled world.'  \r\n" + 
				"\r\n" + 
				"lore:\r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  no_args: '&cYou need to place at least one argument after /lore.'\r\n" + 
				"  blacklisted_word_found: '&cSorry that name contains a blacklisted word.'\r\n" + 
				"  blacklisted_material_found: '&cSorry that material is blacklisted.'\r\n" + 
				"  blacklisted_existing_name_found: '&cSorry that item has an existing name that is blacklisted.'\r\n" + 
				"  blacklisted_existing_lore_found: '&cSorry that item has an existing line of lore that is blacklisted.'\r\n" + 
				"  cannot_lore_air: '&cSorry you can''t relore nothing.'\r\n" + 
				"  no_permission_for_material: '&cSorry you don''t have permission for that material.'\r\n" + 
				"  success: '&aRelored the item successfully.'\r\n" + 
				"  disabled_world: '&cYou are in a disabled world.'\r\n" + 
				"  \r\n" + 
				"setloreline:\r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  wrong_args: '&cYou need to place some arguments after /setloreline. /setloreline <linenumber> <text>'\r\n" + 
				"  not_an_int: '&cYou need to provide an integer as the first argument.'\r\n" + 
				"  invalid_number: '&cSorry that line number is not valid.'\r\n" + 
				"  provide_text: '&cPlease provide some text. /setloreline <linenumber> <text>'\r\n" + 
				"  blacklisted_word_found: '&cSorry that name contains a blacklisted word.'\r\n" + 
				"  blacklisted_material_found: '&cSorry that material is blacklisted.'\r\n" + 
				"  blacklisted_existing_name_found: '&cSorry that item has an existing name that is blacklisted.'\r\n" + 
				"  blacklisted_existing_lore_found: '&cSorry that item has an existing line of lore that is blacklisted.'\r\n" + 
				"  cannot_edit_air: '&cSorry you can''t relore nothing.'\r\n" + 
				"  no_permission_for_material: '&cSorry you don''t have permission for that material.'\r\n" + 
				"  success: '&aRelored the item successfully.'  \r\n" + 
				"  disabled_world: '&cYou are in a disabled world.'\r\n" + 
				"  \r\n" + 
				"removeloreline: \r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  wrong_args: '&cYou need to place some arguments after /removeloreline. /removeloreline <linenumber>'  \r\n" + 
				"  not_an_int: '&cYou need to provide an integer as the first argument.'\r\n" + 
				"  invalid_number: '&cSorry that line number is not valid.'\r\n" + 
				"  has_no_lore: '&cThat item has no lore.'\r\n" + 
				"  out_of_bounds: '&cThat line number does not exist in this lore.'\r\n" + 
				"  blacklisted_material_found: '&cSorry that material is blacklisted.'\r\n" + 
				"  blacklisted_existing_name_found: '&cSorry that item has an existing name that is blacklisted.'\r\n" + 
				"  blacklisted_existing_lore_found: '&cSorry that item has an existing line of lore that is blacklisted.'\r\n" + 
				"  cannot_edit_air: '&cSorry you can''t edit nothing.'\r\n" + 
				"  no_permission_for_material: '&cSorry you don''t have permission for that material.'\r\n" + 
				"  success: '&aRelored the item successfully.'    \r\n" + 
				"  disabled_world: '&cYou are in a disabled world.'\r\n" + 
				"  \r\n" + 
				"glow: \r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  cannot_edit_air: '&cSorry you can''t edit nothing.'\r\n" + 
				"  no_permission_for_material: '&cSorry you don''t have permission for that material.'\r\n" + 
				"  success: '&aAdded glow to the item successfully.'      \r\n" + 
				"  has_enchants: '&cSorry that item has enchantments or is already glowing.'\r\n" + 
				"  disabled_world: '&cYou are in a disabled world.'\r\n" + 
				"  blacklisted_material_found: '&cSorry that material is blacklisted.'\r\n" + 
				"  blacklisted_existing_name_found: '&cSorry that item has an existing name that is blacklisted.'\r\n" + 
				"  blacklisted_existing_lore_found: '&cSorry that item has an existing line of lore that is blacklisted.'\r\n" + 
				"  \r\n" + 
				"removeglow: \r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  cannot_edit_air: '&cSorry you can''t edit nothing.'\r\n" + 
				"  no_permission_for_material: '&cSorry you don''t have permission for that material.'\r\n" + 
				"  success: '&aRemoved glow from the item successfully.'      \r\n" + 
				"  not_glowing: '&cThat item is not glowing or has other enchantments.'  \r\n" + 
				"  disabled_world: '&cYou are in a disabled world.'\r\n" + 
				"  blacklisted_material_found: '&cSorry that material is blacklisted.'\r\n" + 
				"  blacklisted_existing_name_found: '&cSorry that item has an existing name that is blacklisted.'\r\n" + 
				"  blacklisted_existing_lore_found: '&cSorry that item has an existing line of lore that is blacklisted.'\r\n" + 
				"\r\n" + 
				"import:\r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  no_args: '&cPlease provide at least one argument after /import. /import <hand, inventory, raw>'\r\n" + 
				"  wrong_args: '&cPlease follow the correct argument usage. /import <hand, inventory, raw>'\r\n" + 
				"  success: '&aImported successfully.'\r\n" + 
				"  failed_to_get_data: '&cFailed to import from \"{link}\". Error: {error}'\r\n" + 
				"  confirmed: '&aYou confirmed that you understand your items will be deleted by /import inventory. You can now use /import inventory normally until the next server restart.'\r\n" + 
				"  not_confirmed: '&c/import inventory deletes all items in your inventory. Please type /import confirm to confirm that you understand.'\r\n" + 
				"  full_hand: '&cYour main hand must be empty to import an item. Try again after clearing your main hand.'\r\n" + 
				"  yaml_fail: '&cFailed to import an item from the provided yaml. Make sure the yaml text is formatted correctly.'\r\n" + 
				"  wrong_args_hand: '&cYour arguments provided to /import hand <link> are not correct. /import hand <link>' \r\n" + 
				"  wrong_args_inventory: '&cYour arguments provided to /import inventory <link> are not correct. /import inventory <link>'\r\n" + 
				"  \r\n" + 
				"export:\r\n" + 
				"  no_permission: '&cSorry you don''t have permission to perform that command.'\r\n" + 
				"  wrong_sender: '&cSorry only a player can use that command.'\r\n" + 
				"  no_args: '&cPlease provide at least one argument after /export. /export <hand, inventory>'\r\n" + 
				"  wrong_args: '&cPlease follow the correct argument usage. /export <hand, inventory>'\r\n" + 
				"  warn_public: '&6/export sends your item(s) to a public EpicRename.com link. By using /export you are agreeing to EpicRename.com''s privacy policy which can be found at https://epicrename.com/privacy. Type \"/export confirm\" to confirm you are okay with this action. You will not be asked again until after the next server restart.'\r\n" + 
				"  success: '&aExported item(s) successfully. Use the following link to /import the item(s). The link expires in one month. {link}'\r\n" + 
				"  post_fail: '&cFailed to post to EpicRenameOnline: {error}'\r\n" + 
				"  confirmed: '&aYou confirmed that it is okay to post your item(s) publicly. You can now use /export normally until the next server restart.'\r\n" + 
				"  no_air: '&cYou cannot export nothing.'\r\n" + 
				"  \r\n" + 
				"rename_character_limit:\r\n" + 
				"  name_too_long: '&cSorry that name is too long. The character limit is {char}.'\r\n" + 
				"  bypass_msg: '&aYou just bypassed the character limit of {char}.'\r\n" + 
				"  \r\n" + 
				"format_code_permission:\r\n" + 
				"  no_permission: '&cYou don''t have permission for the format code \"{code}\".'  \r\n" + 
				"  \r\n" + 
				"format_code_limit:\r\n" + 
				"  min_not_reached: '&cYou didn''t reach the minimum amount of formatting codes. Please use more than {min} formatting codes.'\r\n" + 
				"  max_reached: '&cYou reached the maximum amount of formatting codes. Please use less than {max} formatting codes.'\r\n" + 
				"  bypass_min: '&aYou bypassed the minimum formatting code limit.'\r\n" + 
				"  bypass_max: '&aYou bypassed the maximum formatting code limit.'\r\n" + 
				"\r\n" + 
				"blacklists:\r\n" + 
				"  material:\r\n" + 
				"    bypass: '&aYou just bypassed the material blacklist.'\r\n" + 
				"  text:\r\n" + 
				"    bypass: '&aYou just bypassed the text blacklist.'\r\n" + 
				"  existingname:\r\n" + 
				"    bypass: '&aYou just bypassed the existing name blacklist.'\r\n" + 
				"  existinglore:\r\n" + 
				"    bypass: '&aYou just bypassed the existing lore blacklist.'    \r\n" + 
				"\r\n" + 
				"economy:\r\n" + 
				"  bypass: '&aYou just bypassed the economy requirement.'\r\n" + 
				"  transaction_success: '&aJust took {cost} from your balance.' \r\n" + 
				"  transaction_error: '&cThere was a problem taking money from your balance: {error}'\r\n" + 
				"  \r\n" + 
				"exploit_prevention:\r\n" + 
				"  no_grindstone_with_glowing_items: '&cYou cannot use a grindstone on a glowing item. Please remove glow from the item first with /removeglow.'  ");
				
				for (String key : config.getRoot().getKeys(true)) {
					System.out.println("updateMessagesYmlString(\"" + key + "\", \"" + config.getString(key) + "\");");
					//updateMessagesYmlString("rename.blacklisted_material_found", "&cSorry that material is blacklisted.");
				}
	}
}
