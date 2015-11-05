package nl.raspen0.RaspenEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {
	
	private RaspenEssentials plugin;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("heal")) {

			@SuppressWarnings("deprecation")
			Player target = sender.getServer().getPlayer(args[0]);
			Player player = (Player) sender;

			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + plugin.prefix + "This command can only be run by a player.");
				return true;

			}
			// Permission check
			if (!player.hasPermission("raspen.heal")) {
				sender.sendMessage(plugin.NoPerm);
				return true;

			}
			// heal self
			if (args.length == 0) {
				player.setHealth(20);
				sender.sendMessage(ChatColor.GREEN + plugin.prefix +"You're now healed.");
				return true;
			} else if (args.length > 0) {
				if (!player.hasPermission("raspen.heal.others")) {
					sender.sendMessage(plugin.NoPerm);
				}
				if (target == null) {
					sender.sendMessage(ChatColor.RED + plugin.prefix + args[0] + " is not currently online.");
				}
				target.setHealth(20);
				sender.sendMessage(ChatColor.GREEN + plugin.prefix + args[0] + " is now healed.");
				return true;
			}

		}
		return true;
	}
}