package com.gmail.justbru00.epic.rename.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.justbru00.epic.rename.main.RenameRewrite;

import net.milkbowl.vault.economy.EconomyResponse;

public class Lore implements CommandExecutor {

	public RenameRewrite main;

	public Lore(RenameRewrite main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args) {
		if (command.getName().equalsIgnoreCase("lore")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("epicrename.lore")) {
					if (args.length > 0) {
						if (main.useEconomy) {
							EconomyResponse r = RenameRewrite.econ.withdrawPlayer(player, main.getConfig().getInt("economy.costs.lore"));
							   if (r.transactionSuccess()) {
								player.sendMessage(String.format(RenameRewrite.Prefix + RenameRewrite.color("&6Withdrawed &a%s &6from your balance. Your current balance is now: &a%s"), RenameRewrite.econ.format(r.amount), RenameRewrite.econ.format(r.balance)));
								int i = 0;
								ArrayList<String> lore = new ArrayList<String>();
								while (args.length > i) {
									if (main.checkBlacklist(args[i])) {
										RenameRewrite.msg(player, main.config.getString("found blacklisted word"));
										return true;
									}						
									
									lore.add(RenameRewrite.color(args[i]));
									i++;
								}
								// Check Material Blacklist
								if (!main.checkMaterialBlacklist(player.getItemInHand().getType())) {
									RenameRewrite.msg(player, main.config.getString("found blacklisted material"));
									return true;
								}
								player.setItemInHand(main.renameItemStack(player, lore, player.getItemInHand()));
								RenameRewrite.msg(player, main.config.getString("lore complete"));
								return true;
							   } else {
								   sender.sendMessage(String.format(RenameRewrite.Prefix + RenameRewrite.color("&6An error occured:&c %s"), r.errorMessage));
									return true;
							   }
							   } 
							   int i = 0;
								ArrayList<String> lore = new ArrayList<String>();
								while (args.length > i) {
									if (main.checkBlacklist(args[i])) {
										RenameRewrite.msg(player, main.config.getString("found blacklisted word"));
										return true;
									}
									lore.add(RenameRewrite.color(args[i]));
									i++;
								}
								// Check Material Blacklist
								if (!main.checkMaterialBlacklist(player.getItemInHand().getType())) {
									RenameRewrite.msg(player, main.config.getString("found blacklisted material"));
									return true;
								}
								player.setItemInHand(main.renameItemStack(player, lore, player.getItemInHand()));
								RenameRewrite.msg(player, main.config.getString("lore complete"));
							   return true;
						
					} else {
						RenameRewrite.msg(player, main.config.getString("not enough or too many args"));
						return true;
					}
				} else {
					RenameRewrite.msg(player, main.config.getString("no permission"));
					return true;
				}
			} else {
				sender.sendMessage(RenameRewrite.Prefix + RenameRewrite.color("&4Sorry you can't use this command."));
				return true;
			}
		} // End of command Lore
		return false;
	}

}
