package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandFly implements CommandCallable {

	private RaspenEssentials plugin;

	public CommandFly(RaspenEssentials ess){
		plugin = ess;
	}

	@Override
	//Command
	public CommandResult process(CommandSource src, String arguments) throws CommandException {
		if(!src.hasPermission("raspess.fly")){
			src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
			return CommandResult.success();
		}
		String[] args = arguments.isEmpty() ? new String[]{} : arguments.split(" ");
		Player target = null;
		boolean self = false;
		if (arguments.isEmpty()){
            if (!(src instanceof Player)) {
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "onlyPlayer")));
                return CommandResult.success();
            }
            target = (Player) src;
        } else {
		    if(args[0].equals(src.getName())){
		        System.out.println("test2");
                self = true;
            } else {
                if (!src.hasPermission("raspess.fly.others")) {
                    src.sendMessage(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm"));
                    return CommandResult.success();
                }
            }
            for(Player p : plugin.getGame().getServer().getOnlinePlayers()){
                if(p.getName().toLowerCase().contains(args[0].toLowerCase())){
                    target = p;
                }
            }
            if(target == null){
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "notOnline", new String[]{"%player"}, new String[] {args[0]})));
                return CommandResult.success();
            }
		}
        //Get flying state
        boolean flight;
        if (args.length == 2) {
            flight = Boolean.valueOf(args[1]);
        } else {
            flight = !target.get(Keys.CAN_FLY).get();
        }
        target.offer(Keys.CAN_FLY, flight);
        if(!flight) {
            target.offer(Keys.IS_FLYING, false);
        }
        target.sendMessage(plugin.getManager().getLangHandler().getMessage(target, null, "fly" + flight));
        if (args.length >= 1 && !self) {
            System.out.println(args.length);
            src.sendMessage(plugin.getManager().getLangHandler().getMessage(src, null, "flyOther" + flight, new String[]{"%player"}, new String[]{args[0]}));
        }
		return CommandResult.success();
	}

	@Override
	//Tab Complete
	public List<String> getSuggestions(CommandSource src, String args, @Nullable Location<World> location) throws CommandException {
		List<String> list = new ArrayList<>();
		if (args.isEmpty()) {
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
	private final Optional<Text> desc = Optional.of(Text.of("/fly <player>"));

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
		return Text.of("/fly (player) (mode)");
	}
}
