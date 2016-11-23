/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils;

import org.bukkit.Bukkit;

import com.gmail.justbru00.epic.rename.main.Old_RenameRewrite;
import com.gmail.justbru00.epic.rename.main.v3.Main;

public class Debug {
	public static void send(String msg) {
		if (Main.debug) {
		Bukkit.broadcastMessage(Messager.color(Old_RenameRewrite.Prefix + "&8[&cDebug&8] &c" + msg));
		}
	}
}
