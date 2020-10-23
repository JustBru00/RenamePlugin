package com.gmail.justbru00.epic.rename.utils.v3;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.v3.Main;

public class WorldChecker {

	/**
	 * Checks the config to see if the current world is enabled or not.
	 * @param p The player who is running the command.
	 * @return True if the world is okay false if the world is disabled.
	 */
	public static boolean checkWorld(Player p) {
		
		if (!Main.getInstance().getConfig().getBoolean("per_world")) {
			Debug.send("Per world is disabled.");			
			return true;
		}
		
		Location l = p.getLocation();
		
		List<String> worlds;
		worlds = Main.getInstance().getConfig().getStringList("enabled_worlds");
		
		for (String s : worlds) {
			if (l.getWorld().getName().equals(s)) {
				Debug.send("In an enabled world");
				return true;
			}
		}
		Debug.send("In a disabled world.");
		return false;
	}
	
}
