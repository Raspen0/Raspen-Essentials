package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandMain implements CommandExecutor {

    private RaspenEssentials plugin;

    public CommandMain(RaspenEssentials ess) {
        plugin = ess;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("raspenessentials")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "RaspenEssentials, developed by "+ ChatColor.YELLOW +"raspen0"+ ChatColor.AQUA  + ".");
                sender.sendMessage(ChatColor.AQUA +"Version: " + ChatColor.YELLOW + plugin.getDescription().getVersion());
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "helpUse"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("raspess.reload")) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                    return true;
                }
                plugin.getManager().getLangHandler().unloadLangs();
                plugin.reloadConfig();
                plugin.getManager().getLangHandler().loadLangs();
                if (plugin.getConfig().getString("localeDetectMode").equals("permission")) {
                    plugin.getManager().getLangHandler().localemode = 1;
                } else {
                    plugin.getManager().getLangHandler().localemode = 0;
                }
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "reload"));
            }
        }
        return true;
    }

}