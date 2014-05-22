package com.gmail.rickvinke1.raspenessentials;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGamemode implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		//gamemode command
 if (cmd.getName().equalsIgnoreCase("gamemode")) {
	if (!(sender instanceof Player)) {
		sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
		return true;
	}
	//gamemode permission check
	Player player = (Player) sender;
	if(!player.hasPermission("raspen.gamemode")){
	sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
	return true;
}
	//gamemode self
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE] [PLAYER]");
	}
			if (args.length == 1) {	
				if (args[0].equalsIgnoreCase("1")) {
					player.setGameMode(GameMode.CREATIVE);
					sender.sendMessage(ChatColor.GREEN + "Gamemode Changed");
				}
				else if (args[0].equalsIgnoreCase("0")) {
					player.setGameMode(GameMode.SURVIVAL);
					sender.sendMessage(ChatColor.GREEN + "Gamemode Changed");
				}
				else if (args[0].equalsIgnoreCase("2")) {
					player.setGameMode(GameMode.ADVENTURE);
					sender.sendMessage(ChatColor.GREEN + "Gamemode Changed");
				}
				return true;
			}
		//gamemode others
			@SuppressWarnings("deprecation")
			Player target = sender.getServer().getPlayer(args[0]);
			// Make sure the player is online.
			if (target == null) {
				 sender.sendMessage(ChatColor.RED + "That player isn't online");
				 return true;
				 //gamemode others permission check
			} else {
			if(!player.hasPermission("raspen.gamemode.others"))
			sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
			//gamemode others command
			if (player.hasPermission("raspen.gamemode.others"))
				if (args.length == 2) {
						if (args[0].equalsIgnoreCase("1")) {
							target.setGameMode(GameMode.CREATIVE);
							 sender.sendMessage(args[2]
										+ "'s gamemode has changed.");
						}
						if (args[0].equalsIgnoreCase("0")) {
							target.setGameMode(GameMode.SURVIVAL);
							 sender.sendMessage(args[2]
										+ "'s gamemode has changed.");
						}
						if (args[0].equalsIgnoreCase("2")) {
							target.setGameMode(GameMode.ADVENTURE);
							 sender.sendMessage(args[2]
										+ "'s gamemode has changed.");
				}
						return true;
				}
						if (args.length >2) {
							sender.sendMessage(ChatColor.RED + "Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE] [PLAYER]");
						}
				
			}
 }
return true;
	}
}