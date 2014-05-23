package com.gmail.rickvinke1.raspenessentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		//heal command
		if (cmd.getName().equalsIgnoreCase("heal")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "[RE] This command can only be run by a player.");
				return true;
			}
			//heal permission check
			Player player = (Player) sender;
			if(!player.hasPermission("raspen.heal")){
			sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
			return true;
			
		}
			//heal self
					if (args.length == 0) {
						player.setHealth(20);
						sender.sendMessage(ChatColor.GREEN +"[RE] You're now healed.");
						return true;
			}
				//heal others
					@SuppressWarnings("deprecation")
					Player target = sender.getServer().getPlayer(args[0]);
					// Make sure the player is online.
					if (target == null) {
						 sender.sendMessage(ChatColor.RED +"[RE] "+ args[0] + " is not currently online.");
						 //heal others permission check
					} else {
					if(!player.hasPermission("raspen.heal.others"))
					sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
					//heal others command
					if (player.hasPermission("raspen.heal.others"))
						if (args.length == 1) {
							target.setHealth(20);
							sender.sendMessage(ChatColor.GREEN +"[RE] "+ args[0] + " is now healed.");
							return true;

							
	}
					}
		}
		return true;
	}
}