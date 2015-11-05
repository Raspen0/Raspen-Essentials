package nl.raspen0.RaspenEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandReload implements CommandExecutor {
	
	private RaspenEssentials plugin;

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		ConsoleCommandSender console = Bukkit.getConsoleSender();

		if (cmd.getName().equalsIgnoreCase("rereload")) {
			if (sender instanceof Player) {
				// Permission check
				Player player = (Player) sender;
				if (!player.hasPermission("raspen.reload")) {
					sender.sendMessage(plugin.NoPerm);
					return true;
				}

				plugin.reloadConfig();
				player.sendMessage(ChatColor.GREEN + plugin.prefix + "The config had been reloaded.");
				
				//If used from console
			} else if (sender instanceof ConsoleCommandSender)
				plugin.reloadConfig();
			console.sendMessage(ChatColor.GREEN + plugin.prefix + "The config has been reloaded.");
			return true;
		}
		return false;
	}
}
