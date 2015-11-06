package nl.raspen0.RaspenEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("heal")) {
			// Heal yourself
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "[RE] This command can only be run by a player.");
					return true;
				}
				
				//Check Permissions
				Player player = (Player) sender;
				if (!player.hasPermission("raspen.heal")) {
					sender.sendMessage(Strings.NoPerm);
				}
				
				//Heal Event
				double health = player.getMaxHealth();
				player.setHealth(health);
				sender.sendMessage(Strings.infoprefix + "You're now healed.");
				return true;

				//Heal Others
			} else if (args.length > 0) {
				@SuppressWarnings("deprecation")
				Player target = sender.getServer().getPlayer(args[0]);

				//If Console
				if (sender instanceof ConsoleCommandSender) {
					if (!(target == null)) {
						double health = target.getMaxHealth();
						target.setHealth(health);
						sender.sendMessage(Strings.infoprefix + args[0] +  " has been healed");
					} else
					sender.sendMessage(ChatColor.RED + "[RE] " + args[0] + " is not currently online.");
				}
				//If Player
				if ((sender instanceof Player)) {
					if (!(target == null)) {
					Player player = (Player) sender;
					if (!player.hasPermission("raspen.heal.others")) {
						sender.sendMessage(Strings.NoPerm);
					}

					double health = target.getMaxHealth();
					target.setHealth(health);
					sender.sendMessage(Strings.infoprefix + args[0] + " is now healed.");
					return true;
				} else
				sender.sendMessage(ChatColor.RED + "[RE] " + args[0] + " is not currently online.");
					
				}

			}

		} 
		return true;
	}
}