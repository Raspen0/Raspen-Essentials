package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CommandSpawn implements CommandExecutor {

	private final RaspenEssentials plugin;

	public CommandSpawn(RaspenEssentials plugin){
		this.plugin = plugin;
	}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//spawn command
		if (cmd.getName().equalsIgnoreCase("spawn")) {
			//Check Permission
			if(!sender.hasPermission("raspess.spawn")){
				sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
				return true;
			}
			Player target;
			//Get sender
			if(args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
					return true;
				}
				target = (Player) sender;

			} else {
				if(!sender.hasPermission("raspess.spawn.others")){
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
					return true;
				}
				//Get target player
                //noinspection deprecation
                target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "notOnline").replace("%player", args[0]));
					return true;
				}
			}
			//Teleport player to spawn
			target.teleport(plugin.getManager().getSpawnHandler().spawnloc, PlayerTeleportEvent.TeleportCause.COMMAND);
			target.sendMessage(plugin.getManager().getLangHandler().getMessage(target, null, "spawn"));
		}
		return true;
		
	}
}
										