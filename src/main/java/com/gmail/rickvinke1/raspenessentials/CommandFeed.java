package com.gmail.rickvinke1.raspenessentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// feed command
		if (cmd.getName().equalsIgnoreCase("feed")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED
						+ "[RE] This command can only be run by a player.");
				return true;
			}
			// feed permission check
			Player player = (Player) sender;
			if (!player.hasPermission("raspen.feed")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have the permission to use this");
				return true;

			}
			// feed self
			if (args.length == 0) {
				player.setFoodLevel(20);
				sender.sendMessage(ChatColor.GREEN +"[RE] You're now feeded.");
				return true;
			}
			// feed others
			@SuppressWarnings("deprecation")
			Player target = sender.getServer().getPlayer(args[0]);
			// Make sure the player is online.
			if (target == null) {
				sender.sendMessage(ChatColor.RED +"[RE] " + args[0] + " is not currently online.");
				// feed others permission check
			} else {
				if (!player.hasPermission("raspen.feed.others"))
					sender.sendMessage(ChatColor.RED
							+ "You don't have the permission to use this");
				// feed others command
				if (player.hasPermission("raspen.feed.others"))
					if (args.length == 1) {
						target.setFoodLevel(20);
						sender.sendMessage(ChatColor.GREEN + "[RE] " + args[0] + " is now feeded.");
						return true;
					}

			}
		}
		return true;

	}

}
