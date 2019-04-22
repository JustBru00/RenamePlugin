/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created for issue #105, #106
 */
public class ItemSerialization {
	
	/**
	 * @author Justin Brubaker
	 * @param inv Inventory to convert to string.
	 * @return
	 */
	public static String toString(Inventory inv) {
		YamlConfiguration config = new YamlConfiguration();
		
		for (int i = 0; i < inv.getSize(); i++) {
			config.set(String.valueOf(i), inv.getItem(i));
		}
		
		return config.saveToString();
	}
	
	/**
	 * This method will completely clear the players inventory.
	 * @author Justin Brubaker
	 * @param s The string of inventory items.
	 * @param p The player who will get the items.
	 */
	public static void fillInventoryFromString(String s, Player p) {
		Inventory inv = p.getInventory();
		inv.clear();
		
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.loadFromString(s);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			return;
		}
		
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, config.getItemStack(String.valueOf(i), null));
		}		
	}
	
	/**
	 * Single {@link ItemStack} to a {@link String}
	 * @param is
	 * @return
	 */
	public static String toString(ItemStack is) {
		return toString(is, "i");
	}
	
	/**
	 * Single {@link ItemStack} from a {@link String}
	 * @param string
	 * @return
	 */
	public static ItemStack toItem(String string) {
		return toItem(string, "i");
	}

	/**
	 * This code is from Hellgast23's comment at
	 * https://www.spigotmc.org/threads/serializing-itemstack-to-string.80233/#post-889181
	 * 
	 * @author Hellgast23
	 * @param itemStack
	 * @param key The name of the configuration section.
	 * @return A string of text that contains item information.
	 */
	private static String toString(ItemStack itemStack, String key) {
		YamlConfiguration config = new YamlConfiguration();
		config.set(key, itemStack);
		return config.saveToString();
	}

	/**
	 * This code is from Hellgast23's comment at
	 * https://www.spigotmc.org/threads/serializing-itemstack-to-string.80233/#post-889181
	 * 
	 * @author Hellgast23
	 * @param stringBlob
	 * @return
	 */
	private static ItemStack toItem(String string, String key) {
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.loadFromString(string);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return config.getItemStack(key, null);
	}

}
