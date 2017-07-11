package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFeed implements CommandExecutor {

	private RaspenEssentials plugin;

	public CommandFeed(RaspenEssentials ess){
		plugin = ess;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("feed")) {
			// Feed yourself
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
					return true;
				}
				
				//Check Permissions
				Player player = (Player) sender;
				if (!player.hasPermission("raspess.feed")) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(player, null, "noPerm"));
					return true;
				}
				
				//Feed Event
				player.setFoodLevel(20);
				player.setSaturation(20);
				sender.sendMessage(plugin.getManager().getLangHandler().getMessage(player, null, "fed"));
				return true;

				//Heal Others
			} else if (args.length > 0) {
				if (!sender.hasPermission("raspess.feed.others")) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
					return true;
				}
				//Check if target is online
				@SuppressWarnings("deprecation")
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "notOnline").replace("%player", args[0]));
					return true;
				}

				//Feed other player
				target.setFoodLevel(20);
				target.setSaturation(20);
				sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "feedOther").replace("%player", target.getName()));
				return true;
			}

		}
		return true;
	}
}