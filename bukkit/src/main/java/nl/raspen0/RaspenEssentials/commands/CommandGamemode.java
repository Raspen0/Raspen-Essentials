package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGamemode implements CommandExecutor {

    private RaspenEssentials plugin;

    public CommandGamemode(RaspenEssentials ess){
        plugin = ess;
    }

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gamemode")) {
			if (!sender.hasPermission("raspess.gamemode")) {
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "unknownGamemode"));
                return true;
            }

            if (args.length == 1) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "gamemodeOnlyPlayers"));
                    return true;
                } else {
                    Player player = (Player) sender;
                    //Change Gamemode
                    GamemodeChange(player, sender, false, args[0]);
                }
            }
            if (args.length == 2) {
                if (!sender.hasPermission("raspess.gamemode.others")) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                    return true;
                }
                @SuppressWarnings("deprecation")
                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "notOnline").replace("%player", args[1]));
                    return true;
                }
                //Change Target's Gamemode
                GamemodeChange(target, sender, true, args[0]);
                return true;
            }
				
		}

		return true;
	}

    /**
     *
     * @param target The player who's gamemode will be changed.
     * @param gamemode The gamemode
     * @param sender The sender of the command
     * @param other When true it will send a message to the sender that the gamemode of the target has been changed.
     */
	private void GamemodeChange(Player target, CommandSender sender, boolean other, String gamemode) {
	    //Handle abbreviations (s, c, a, sp)
        if(gamemode.length() <=2 ) {
            switch (gamemode) {
                case "s":
                    gamemode = "survival";
                    break;
                case "c":
                    gamemode = "creative";
                    break;
                case "a":
                    gamemode = "adventure";
                    break;
                case "sp":
                    gamemode = "spectator";
                    break;
            }
        }
        if(StringUtils.isNumeric(gamemode)){
            //Handle numbers (0, 1, 2, 3)
            int gm = Integer.valueOf(gamemode);
            switch(gm){
                case 0:
                    gamemode = "survival";
                    break;
                case 1:
                    gamemode = "creative";
                    break;
                case 2:
                    gamemode = "adventure";
                    break;
                case 3:
                    gamemode = "spectator";
                    break;
            }
        }
        try {
            target.setGameMode(GameMode.valueOf(gamemode.toUpperCase()));
            target.sendMessage(plugin.getManager().getLangHandler().getMessage(target, null, "gamemodeChange").replace("%gm", gamemode.toLowerCase()));
            plugin.log(target.getDisplayName() + "'s gamemode changed to " + gamemode.toLowerCase());
            if(other){
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "gamemodeChangeOther").replace("%player", target.getName()).replace
                        ("%gm", gamemode.toLowerCase()));
            }
        } catch(IllegalArgumentException e){
            sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "unknownGamemode"));
        }
	}
}