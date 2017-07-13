package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {

    private RaspenEssentials plugin;

    public CommandFly(RaspenEssentials ess) {
        plugin = ess;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (!sender.hasPermission("raspess.fly")) {
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                return true;
            }
            Player target;
            boolean self = false;
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
                    return true;
                }
                target = (Player) sender;
            } else {
                if(args[0].equals(sender.getName())){
                    //Check if target is sender, if false then check raspess.fly.others permission.
                    self = true;
                } else {
                    if (!sender.hasPermission("raspess.fly.others")) {
                        sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                        return true;
                    }
                }

                target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "notOnline").replace("%player", args[1]));
                    return true;
                }
            }
            //Get flying state
            boolean flight;
            if (args.length == 2) {
                flight = Boolean.valueOf(args[1]);
            } else {
                flight = !target.getAllowFlight();
            }
            target.setAllowFlight(flight);
            target.setFlying(flight);
            target.sendMessage(plugin.getManager().getLangHandler().getMessage(target, null, "fly" + flight));
            if (args.length >= 1 && !self) {
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "flyOther" + flight).replace("%player", args[0]));
            }

        }
        return true;
    }

}
