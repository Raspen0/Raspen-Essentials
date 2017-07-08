package nl.raspen0.RaspenEssentials.Bukkit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import nl.raspen0.RaspenEssentials.Strings;

public class CommandFeed implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("feed")) {
			// Feed yourself
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "[RE] This command can only be run by a player.");
					return true;
				}
				
				//Check Permissions
				Player player = (Player) sender;
				if (!player.hasPermission("raspen.feed")) {
					sender.sendMessage(Strings.NoPerm);
				}
				
				//Feed Event
				player.setFoodLevel(20);
				player.setSaturation(20);
				sender.sendMessage(Strings.infoprefix + "You're now feeded.");
				return true;

				//Heal Others
			} else if (args.length > 0) {
				@SuppressWarnings("deprecation")
				Player target = sender.getServer().getPlayer(args[0]);

				//If Console
				if (sender instanceof ConsoleCommandSender) {
					if (!(target == null)) {
						target.setFoodLevel(20);
						target.setSaturation(20);
						sender.sendMessage(Strings.infoprefix + args[0] +  " has been feeded");
					} else
					sender.sendMessage(ChatColor.RED + "[RE] " + args[0] + " is not currently online.");
				}
				//If Player
				if ((sender instanceof Player)) {
					if (!(target == null)) {
					Player player = (Player) sender;
					if (!player.hasPermission("raspen.feed.others")) {
						sender.sendMessage(Strings.NoPerm);
					}

					target.setFoodLevel(20);
					target.setSaturation(20);
					sender.sendMessage(Strings.infoprefix + args[0] + " is now feeded.");
					return true;
				} else
				sender.sendMessage(ChatColor.RED + "[RE] " + args[0] + " is not currently online.");
				}

			}

		}
		return true;
	}
}