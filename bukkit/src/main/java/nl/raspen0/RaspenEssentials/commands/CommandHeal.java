package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHeal implements CommandExecutor {
	
	private RaspenEssentials plugin;
	
	public CommandHeal(RaspenEssentials ess){
		plugin = ess;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("heal")) {
			// Heal yourself
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
					return true;
				}
				
				//Check Permissions
				Player player = (Player) sender;
				if (!player.hasPermission("raspess.heal")) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(player, null, "noPerm"));
					return true;
				}
				
				//Heal Event
				player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				sender.sendMessage(plugin.getManager().getLangHandler().getMessage(player, null, "healed"));
				return true;

				//Heal Others
			} else if (args.length > 0) {
				//Check Permissions
				if (!sender.hasPermission("raspess.heal.others")) {
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
				//Heal other player
				target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "healedOther").replace("%player", target.getName()));
				return true;
			}

		} 
		return true;
	}
}