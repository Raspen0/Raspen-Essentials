package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetSpawn implements CommandExecutor {

    private RaspenEssentials plugin;

    public CommandSetSpawn(RaspenEssentials ess){
        plugin = ess;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
                return true;
            }

            //Check Permission
            if(!sender.hasPermission("raspess.setspawn")){
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                return true;
            }

            Player player = (Player) sender;
            int X = (int) player.getLocation().getX();
            int Y = (int) player.getLocation().getY();
            int Z = (int) player.getLocation().getZ();

            player.getWorld().setSpawnLocation(X, Y, Z);
            sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "setSpawn").replace("%x", String.valueOf(X)).replace("%y", String.valueOf(Y))
                    .replace("%z", String.valueOf(Z)));
        }

        return true;

    }
}
