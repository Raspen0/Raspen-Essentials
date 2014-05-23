package com.gmail.rickvinke1.raspenessentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandGamemode implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// gamemode command
    	ConsoleCommandSender console = Bukkit.getConsoleSender();
		if (cmd.getName().equalsIgnoreCase("gamemode")) {
			//if the sender is console
		 if(sender instanceof ConsoleCommandSender){
			 if (args.length == 1) {
			sender.sendMessage(ChatColor.RED + "[RE] You can't change the gamemode of the console");
			 }
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED
						+ "[RE] Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE] [PLAYER]");
			}
				if (args.length == 2) {
					sender.sendMessage(ChatColor.RED
							+ "[RE] Changing gamemodes from the console is a planned function");
			}
		 }
		 //if sender is player
		 if ((sender instanceof Player)) {
			// gamemode permission check
			Player player = (Player) sender;
			if (!player.hasPermission("raspen.gamemode")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have the permission to use this");
				return true;
			}
			// gamemode arguments check
			// if only /gamemode is typed
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED
						+ "[RE] Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE] [PLAYER]");
			} else {
			// if /gamemode [type] is typed
			if (args.length == 1) {
				//creative mode
				if (args[0].equals("1")|| args[0].equalsIgnoreCase("c")|| (args[0].equalsIgnoreCase("creative"))) {
					player.setGameMode(GameMode.CREATIVE);
					sender.sendMessage(ChatColor.GREEN + "[RE] Gamemode changed to CREATIVE");
					console.sendMessage("[REssentials] " + (sender.getName() + "'s gamemode was changed to CREATIVE"));
					//survival mode
				} else if (args[0].equals("0")|| args[0].equalsIgnoreCase("s")|| (args[0].equalsIgnoreCase("survival"))) {
					player.setGameMode(GameMode.SURVIVAL);
					sender.sendMessage(ChatColor.GREEN + "[RE] Gamemode changed to SURVIVAL");
					console.sendMessage("[REssentials] " + (sender.getName() + "'s gamemode was changed to SURVIVAL"));
					//adventure mode
				} else if (args[0].equals("2")|| args[0].equalsIgnoreCase("a")|| (args[0].equalsIgnoreCase("adventure"))) {
					player.setGameMode(GameMode.ADVENTURE);
					sender.sendMessage(ChatColor.GREEN + "[RE] Gamemode changed to ADVENTURE");
					console.sendMessage("[REssentials] " + (sender.getName() + "'s gamemode was changed to ADVENTURE"));
					//if mode is false return usage
				} else
					sender.sendMessage(ChatColor.RED
							+ "[RE] Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE] [PLAYER]");
				return true;
			}
			//gamemode others permission check
			else if (!player.hasPermission("raspen.gamemode.others")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have the permission to use this");
				return true;
			}
			//gamemode other players
			@SuppressWarnings("deprecation")
			Player target = sender.getServer().getPlayer(args[1]);
			if (target == null) {
				 sender.sendMessage("[RE] " + args[1]
						+ " is not currently online.");
			} else {
			if (args.length == 2) {
				//creative others
				if (args[0].equals("1")|| args[0].equalsIgnoreCase("c")|| (args[0].equalsIgnoreCase("creative"))) {
					target.setGameMode(GameMode.CREATIVE);
					sender.sendMessage(ChatColor.GREEN + "[RE] " + args[1] + "'s gamemode changed to CREATIVE");
					target.sendMessage(ChatColor.GREEN + "[RE] Your Gamemode has changed to CREATIVE");
					console.sendMessage("[REssentials] " + (args[1] + "'s gamemode was changed to CREATIVE"));
					//survival others
				} else if (args[0].equals("0")|| args[0].equalsIgnoreCase("s")|| (args[0].equalsIgnoreCase("survival"))) {
					target.setGameMode(GameMode.SURVIVAL);
					sender.sendMessage(ChatColor.GREEN + "[RE] " + args[1] + "'s gamemode changed to SURVIVAL");
					target.sendMessage(ChatColor.GREEN + "[RE] Your Gamemode has changed to SURVIVAL");
					console.sendMessage("[REssentials] " + (args[1] + "'s gamemode was changed to SURVIVAL"));
					//adventure others
				} else if (args[0].equals("2")|| args[0].equalsIgnoreCase("a")|| (args[0].equalsIgnoreCase("adventure"))) {
					target.setGameMode(GameMode.ADVENTURE);
					sender.sendMessage(ChatColor.GREEN + "[RE] " + args[1] + "'s gamemode changed to ADVENTURE");
					target.sendMessage(ChatColor.GREEN + "[RE] Your Gamemode has changed to ADVENTURE");
					console.sendMessage("[REssentials] " + (args[1] + "'s gamemode was changed to ADVENTURE"));
					// if gamemode is false, return usage
				} else
					sender.sendMessage(ChatColor.RED
							+ "[RE] Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE] [PLAYER]");
				return true;
		
	}
	}
	}

		 }
			}
		return true;
	}
}