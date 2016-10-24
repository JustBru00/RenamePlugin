/**
 * @author Justin "JustBru00" Brubaker
 * 
 * This is licensed under the MPL Version 2.0. See license info in LICENSE.txt
 */
package com.gmail.justbru00.epic.rename.utils;

import org.bukkit.Bukkit;

import com.gmail.justbru00.epic.rename.main.RenameRewrite;
import com.gmail.justbru00.epic.rename.main.v3.V3_Main;

public class Debug {
	public static void send(String msg) {
		if (V3_Main.debug) {
		Messager.msgConsole(RenameRewrite.Prefix + "&8[&cDebug&8] &c" + msg);	
		Bukkit.broadcastMessage(Messager.color(RenameRewrite.Prefix + "&8[&cDebug&8] &c" + msg));
		}
	}
}
