package nl.raspen0.RaspenEssentials.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.raspen0.RaspenEssentials.Strings;

public class CommandGamemode implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// gamemode command
		if (cmd.getName().equalsIgnoreCase("gamemode")) {
			if (sender.hasPermission("raspen.gamemode")) {
				if (args.length == 0) {
					sender.sendMessage(Strings.problemprefix
							+ "Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR] [PLAYER]");
				}

				if (args.length == 1) {
					if (sender instanceof ConsoleCommandSender) {
						sender.sendMessage(Strings.problemprefix + "You can't change the gamemode of the console");
					}
					if (sender instanceof Player) {
						Player player = (Player) sender;
						this.GamemodeChange(player, args);
					}
				}
				if (args.length == 2) {
					if(sender.hasPermission("raspen.gamemode.others")){
					@SuppressWarnings("deprecation")
					Player target = sender.getServer().getPlayer(args[1]);
					
					if (target == null) {
						 sender.sendMessage(Strings.problemprefix + args[1] + " is currently not online.");
					} else {
					this.GamemodeChange(target, args);
				}
					}
					else{
						sender.sendMessage(Strings.NoPerm);
					}
				}
			}
			else {
				sender.sendMessage(Strings.NoPerm);
			}
				
		}

		return true;
	}

	public void GamemodeChange(Player target, String[] args) {
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		//survival mode
		if (args[0].equals("0") || args[0].equalsIgnoreCase("s") || (args[0].equalsIgnoreCase("survival"))) {
			target.setGameMode(GameMode.SURVIVAL);
			target.sendMessage(Strings.infoprefix + "Gamemode changed to SURVIVAL");
			console.sendMessage(Strings.infoprefix + target.getDisplayName() + "'s gamemode changed to SURVIVAL");
		//creative mode
		} else if (args[0].equals("1") || args[0].equalsIgnoreCase("c") || (args[0].equalsIgnoreCase("creative"))) {
			target.setGameMode(GameMode.CREATIVE);
			target.sendMessage(Strings.infoprefix + "Gamemode changed to CREATIVE");
			console.sendMessage(Strings.infoprefix + target.getDisplayName() + "'s gamemode changed to CREATIVE");
		//adventure mode
		} else if (args[0].equals("2") || args[0].equalsIgnoreCase("a") || (args[0].equalsIgnoreCase("adventure"))) {
			target.setGameMode(GameMode.ADVENTURE);
			target.sendMessage(Strings.infoprefix + "Gamemode changed to ADVENTURE");
			console.sendMessage(Strings.infoprefix + target.getDisplayName() + "'s gamemode changed to ADVENTURE");
		//spectator mode
		} else if (args[0].equals("3") || args[0].equalsIgnoreCase("sp") || (args[0].equalsIgnoreCase("spectator"))) {
			target.setGameMode(GameMode.SPECTATOR);
			target.sendMessage(Strings.infoprefix + "Gamemode changed to SPECTATOR");
			console.sendMessage(Strings.infoprefix + target.getDisplayName() + "'s gamemode changed to SPECTATOR");
			
		} else
			target.sendMessage(
					Strings.problemprefix + "Usage: Gamemode [SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR] [PLAYER]");
	}
}