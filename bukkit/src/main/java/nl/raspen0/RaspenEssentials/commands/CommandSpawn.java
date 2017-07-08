package nl.raspen0.RaspenEssentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		//spawn command
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "[RE] This command can only be run by a player.");
				return true;
			}
			//spawn permission check
			Player player = (Player) sender;
			if(!player.hasPermission("raspen.spawn")){
			sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
			return true;
			
		}
			//spawn
						player.teleport(player.getWorld().getSpawnLocation()); {
						sender.sendMessage(ChatColor.GREEN +"[RE] Teleporting to spawn.");
						return true;
						}
		}
						
						
						
						//setspawn command
						else if (cmd.getName().equalsIgnoreCase("setspawn")) {
							Player player = (Player) sender;
							int LocX = (int) player.getLocation().getX() + 1;
							int LocY = (int) player.getLocation().getY() + 1;
							int LocZ = (int) player.getLocation().getZ();
							if (!(sender instanceof Player)) {
								sender.sendMessage(ChatColor.RED + "[RE] This command can only be run by a player.");
								return true;
							}
							//setspawn permission check
							if(!player.hasPermission("raspen.setspawn")){
							sender.sendMessage(ChatColor.RED + "You don't have the permission to use this");
							return true;
							
						}
							//setspawn
							player.getServer().getWorld("world").setSpawnLocation(LocX, LocY, LocZ);
							sender.sendMessage(ChatColor.GREEN + "[RE] Spawn Set at: x, " + LocX
									+ " y, " + LocY + " z, " + LocZ);
						}
		return true;
		
	}
}
										