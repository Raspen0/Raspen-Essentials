package nl.raspen0.RaspenEssentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import nl.raspen0.RaspenEssentials.Strings;

public class CommandReload implements CommandExecutor {
	
	RaspenEssentials plugin;
	
	public CommandReload(RaspenEssentials ess){
		plugin = ess;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//ConsoleCommandSender console = Bukkit.getConsoleSender();

		if (cmd.getName().equalsIgnoreCase("rereload")) {
			if (sender.hasPermission("raspen.reload")) {
				plugin.reloadConfig();
				sender.sendMessage(ChatColor.GREEN + "[RE] " + "The config had been reloaded.");
				return true;
			}
			sender.sendMessage(Strings.NoPerm);
			}
		
		return false;
	}
}
