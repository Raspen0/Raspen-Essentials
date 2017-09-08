package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawnPoint implements CommandExecutor {

    private final RaspenEssentials plugin;

    public CommandSpawnPoint(RaspenEssentials plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawnpoint")) {
            //Check Permission
            if(!sender.hasPermission("raspess.setspawnpoint")){
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                return true;
            }

            if(args.length == 0){
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
                    return true;
                }
                Player player = (Player) sender;
                Location loc = player.getLocation();
                plugin.getManager().getSpawnHandler().setRespawn(player.getName(), player.getLocation());
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "setPlayerSpawn")
                        .replace("%player", player.getName()).replace("%world", loc.getWorld().getName()).replace("%x", String.valueOf(loc.getBlockX()))
                .replace("%y", String.valueOf(loc.getBlockY())).replace("%z", String.valueOf(loc.getBlockZ())));
                return true;
            }
            if(!sender.hasPermission("raspess.setspawnpoint.others")){
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                return true;
            }
            if(args.length < 4) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
                    return true;
                }
            }
                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    //TODO: When playerfiles are done, offline spawn setting will be possible.
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "notOnline").replace("%player", args[0]));
                    return true;
                }

                Location loc;
                World world = plugin.getServer().getWorlds().get(0);
            if(args.length >= 4){
                loc = new Location(null, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), 0, 0);
                if(args.length == 5){
                    world = plugin.getServer().getWorld(args[4]);
                }
            } else {
                Player player = (Player) sender;
                loc = player.getLocation();
                world = player.getWorld();
            }
            loc.setWorld(world);
            plugin.getManager().getSpawnHandler().setRespawn(target.getName(), loc);
            sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "setPlayerSpawn")
                    .replace("%player", target.getName()).replace("%world", world.getName()).replace("%x", String.valueOf(loc.getBlockX()))
                    .replace("%y", String.valueOf(loc.getBlockY())).replace("%z", String.valueOf(loc.getBlockZ())));
        }
        return true;
    }
}
