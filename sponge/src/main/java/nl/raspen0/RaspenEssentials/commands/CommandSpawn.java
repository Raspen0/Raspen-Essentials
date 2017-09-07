package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandSpawn implements CommandCallable {

    private final RaspenEssentials plugin;

    public CommandSpawn(RaspenEssentials plugin){
        this.plugin = plugin;
    }

    @Override
    //Command
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        if(!src.hasPermission("raspess.spawn")){
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
            return CommandResult.success();
        }
        Player target = null;
        if (arguments.isEmpty()) {
            if (!(src instanceof Player)) {
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "onlyPlayer")));
                return CommandResult.success();
            }
            target = (Player)src;
        } else {
            String[] args = arguments.split(" ");
            if(!src.hasPermission("raspess.spawn.others")) {
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
                return CommandResult.success();
            }
            for(Player p : plugin.getGame().getServer().getOnlinePlayers()){
                if(p.getName().toLowerCase().contains(args[0].toLowerCase())){
                    target = p;
                }
            }
            if(target == null){
                //src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "notOnline").replace("%player", args[0])));
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getPlaceholderMessage(src, null, "notOnline", new String[]{"%player"}, new String[] {args[0]})));
                return CommandResult.success();
            }
        }

            if(!target.setLocationAndRotationSafely(plugin.getManager().getSpawnHandler().spawnloc, plugin.getManager().getSpawnHandler().spawnrotation)){
                target.sendMessage(plugin.getManager().getLangHandler().getMessage(target, null, "spawnNotSafe"));
            } else {
                target.sendMessage(plugin.getManager().getLangHandler().getMessage(target, null, "spawn"));
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
    private final Optional<Text> desc = Optional.of(Text.of("/spawn <player>"));

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
        return Text.of("/spawn (player)");
    }
}
