package com.gmail.justbru00.epic.rename.utils.v3;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * You know, it's pretty interesting.
 * The most recent spigot update (1.13+) has changed all of the Material values.
 * They changed it so that it matches the vanilla display name of the items!
 * This makes formatting the item names very easy.
 * 
 * @author Swedz
 **/
public class FormatItem {
	protected ItemStack item;
	
	/***
	 * Creates instance of FormatItem from an ItemStack
	 * @param item ItemStack you want to get the format of.
	 */
	public FormatItem(ItemStack item) {
		this.item = item;
	}
	
	/***
	 * Creates instance of FormatItem from a Material
	 * @param material Material you want to get the format of.
	 */
	public FormatItem(Material material) {
		this.item = new ItemStack(material);
	}
	
	/***
	 * Creates instance of FormatItem from a display name.
	 * @param displayName String you want to get the format of.
	 */
	public FormatItem(String displayName) {
		try {
			Material material = Material.valueOf(displayName.toUpperCase().replace(" ", "_"));
			this.item = new ItemStack(material);
		} catch (Exception ex) {
			this.item = new ItemStack(Material.AIR);
		}
	}
	
	/***
	 * Returns the ItemStack value of the formatter.
	 * @return ItemStack
	 */
	public ItemStack getItem() { return this.item; }
	
	/***
	 * Private instance for internal usage only.
	 * @return Format of the FormatItem instance
	 */
	private String format() {
		if(this.item == null)
			return null;
		
		ItemMeta im = this.item.getItemMeta();
		if(im != null && im.getDisplayName() != null && im.getDisplayName() != "")
			return im.getDisplayName();
		
		Material material = this.item.getType();
		
		//Format to proper label: "Stone Bricks" instead of STONE_BRICKS
		String lowerCaseDisplay = material.toString().toLowerCase();
		String formattedDisplay = "";
		for(String lcd : lowerCaseDisplay.split("_")) {
			String lcdf = lcd.substring(0, 1).toUpperCase() + lcd.substring(1);
			formattedDisplay += lcdf + " ";
		}
		
		//Remove ending space bar if it has it
		if(formattedDisplay.endsWith(" "))
			formattedDisplay = formattedDisplay.substring(0, formattedDisplay.length()-1);
		
		return formattedDisplay;
	}
	
	/***
	 * Grabs the display name of the ItemFormat instance.
	 * @return String of the display name.
	 */
	public String getName() {
		String format = this.format();
		if(format != null)
			return format;
		return "";
	}
}