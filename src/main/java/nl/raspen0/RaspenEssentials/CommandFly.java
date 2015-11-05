package nl.raspen0.RaspenEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		// heal command
		if (cmd.getName().equalsIgnoreCase("fly")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED
						+ "[RE] This command can only be run by a player.");
				return true;
			}
			// heal permission check
			Player player = (Player) sender;
			if (!player.hasPermission("raspen.fly")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have the permission to use this");
				return true;

			}
			// fly self
			if (args.length == 0) {
				if (!player.isFlying()) {
					player.setAllowFlight(true);
					player.setFlying(true);
					player.sendMessage(ChatColor.GREEN + ("[RE] Fly Mode Enabled")); // example
				} else {
					if (player.isFlying()) {
						player.setAllowFlight(false);
						player.setFlying(false);
						player.sendMessage(ChatColor.RED + ("[RE] Fly Mode Disabled")); // example
						
						if (args.length == 1) {
							sender.sendMessage(ChatColor.RED + ("[RE] This feature is planned"));
						}
					}

				}
			}
		}

		return true;
	}

}
