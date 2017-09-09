package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class CommandGamemode implements CommandCallable {

	private RaspenEssentials plugin;

	public CommandGamemode(RaspenEssentials ess){
		plugin = ess;
	}

	@Override
	//Command
	public CommandResult process(CommandSource src, String arguments) throws CommandException {
        if (!src.hasPermission("raspess.gamemode")) {
            src.sendMessage(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm"));
            return CommandResult.success();
        }
        String[] args = arguments.split(" ");
        if (arguments.isEmpty()) {
            src.sendMessage(plugin.getManager().getLangHandler().getMessage(src, null, "unknownGamemode"));
            return CommandResult.success();
        }
        if (args.length == 1) {
            if (!(src instanceof Player)) {
                src.sendMessage(plugin.getManager().getLangHandler().getMessage(src, null, "gamemodeOnlyPlayers"));
                return CommandResult.success();
            } else {
                Player player = (Player) src;
                //Change Gamemode
                GamemodeChange(player, src, false, args[0]);
                return CommandResult.success();
            }
        }
        if (args.length == 2) {
            if (!src.hasPermission("raspess.gamemode.others")) {
                src.sendMessage(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm"));
                return CommandResult.success();
            }
            Player player = null;
            for (Player p : plugin.getGame().getServer().getOnlinePlayers()) {
                if (p.getName().toLowerCase().contains(args[0].toLowerCase())) {
                    player = p;
                }
            }
            if (player == null) {
                //src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "notOnline").replace("%player", args[0])));
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "notOnline", new String[]{"%player"}, new String[] {args[1]})));
                return CommandResult.success();
            }

            //Change Target's Gamemode
            GamemodeChange(player, src, true, args[0]);
        }
        return CommandResult.success();
    }

	/**
	 *
	 * @param target The player who's gamemode will be changed.
	 * @param gamemode The gamemode
	 * @param sender The sender of the command
	 * @param other When true it will send a message to the sender that the gamemode of the target has been changed.
	 */
	private void GamemodeChange(Player target, CommandSource sender, boolean other, String gamemode) {
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
		Map<String, GameMode> map = new HashMap<>();
		map.put("survival", GameModes.SURVIVAL);
        map.put("creative", GameModes.CREATIVE);
        map.put("adventure", GameModes.ADVENTURE);
        map.put("spectator", GameModes.SPECTATOR);

		try {
            target.offer(Keys.GAME_MODE, map.get(gamemode.toLowerCase()));
			target.sendMessage(plugin.getManager().getLangHandler().getMessage(target, null, "gamemodeChange",
                    new String[]{"%gm"}, new String[]{gamemode.toLowerCase()}));
			plugin.getLogger().info(target.getName() + "'s gamemode changed to " + gamemode.toLowerCase());
			if(other){
				sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "gamemodeChangeOther", new String[]{"%gm", "%player"},
                        new String[] {gamemode.toLowerCase(), target.getName()}));
			}
		} catch(IllegalArgumentException e){
			sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "unknownGamemode"));
		}
	}

	@Override
	//Tab Complete
	public List<String> getSuggestions(CommandSource src, String args, @Nullable Location<World> location) throws CommandException {
		List<String> list = new ArrayList<>();
        if (args.isEmpty()) {
            list = Arrays.asList("survival", "creative", "adventure", "spectator");
        }
		if (args.length() == 1) {
			for (Player p : plugin.getGame().getServer().getOnlinePlayers()) {
				list.add(p.getName());
			}
		}
		return list;
	}

	@Override
	public boolean testPermission(CommandSource src) {
		return false;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	private final Optional<Text> desc = Optional.of(Text.of("/gamemode <mode> <player>"));

	@Override
	public Optional<Text> getShortDescription(CommandSource src) {
		return desc;
	}

	@Override
	//Hover
	public Optional<Text> getHelp(CommandSource src) {
		return desc;
	}

	@Override
	public Text getUsage(CommandSource src) {
		return Text.of("/gamemode <mode> <player>");
	}
}
