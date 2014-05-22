package com.gmail.rickvinke1.raspenessentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RaspenEssentialsCommandExecutor implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		//heal command
		if (cmd.getName().equalsIgnoreCase("heal")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
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
						sender.sendMessage("You're now healed.");
						return true;
			}
				//heal others
					@SuppressWarnings("deprecation")
					Player target = sender.getServer().getPlayer(args[0]);
					// Make sure the player is online.
					if (target == null) {
						 sender.sendMessage(args[0]
								+ " is not currently online.");
						 //heal others permission check
					} else {
					if(!player.hasPermission("raspen.heal.others"))
					sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
					//heal others command
					if (player.hasPermission("raspen.heal.others"))
						if (args.length == 1) {
							target.setHealth(20);
							sender.sendMessage(args[0] + " is now healed.");
							return true;
						} else {


						}
			}
					//feed command
		} else if (cmd.getName().equalsIgnoreCase("feed")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
				return true;
			}
			//feed permission check
			Player player = (Player) sender;
			if(!player.hasPermission("raspen.feed")){
			sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
			return true;
			
		}
			//feed self
					if (args.length == 0) {
						player.setFoodLevel(20);
						sender.sendMessage("You're now feeded.");
						return true;
			}
				//feed others
					@SuppressWarnings("deprecation")
					Player target = sender.getServer().getPlayer(args[0]);
					// Make sure the player is online.
					if (target == null) {
						 sender.sendMessage(args[0]
								+ " is not currently online.");
						 //feed others permission check
					} else {
					if(!player.hasPermission("raspen.feed.others"))
					sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
					//feed others command
					if (player.hasPermission("raspen.feed.others"))
						if (args.length == 1) {
							target.setFoodLevel(20);
							sender.sendMessage(args[0] + " is now feeded.");
							return true;
					}
					
					}
		}

							
				
		sender.sendMessage(ChatColor.RED + "Something went wrong");
		return true;
	}
}