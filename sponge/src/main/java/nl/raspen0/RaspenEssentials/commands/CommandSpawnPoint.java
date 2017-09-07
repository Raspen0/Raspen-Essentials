package nl.raspen0.RaspenEssentials.commands;

import com.flowpowered.math.vector.Vector3d;
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

public class CommandSpawnPoint implements CommandCallable {

    private final RaspenEssentials plugin;

    public CommandSpawnPoint(RaspenEssentials plugin){
        this.plugin = plugin;
    }

    @Override
    //Command
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        if(!src.hasPermission("raspess.setspawnpoint")){
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
            return CommandResult.success();
        }
        if (arguments.isEmpty()) {
            if (!(src instanceof Player)) {
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "onlyPlayer")));
                return CommandResult.success();
            }
            Player player = (Player) src;
            //Set player spawnpoint
            Location loc = player.getLocation();
            plugin.getManager().getSpawnHandler().setRespawn(player.getName(), player.getWorld(), player.getLocation().getPosition(), player.getRotation());
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getPlaceholderMessage(src, null, "setPlayerSpawn",
                    new String[] {"%player", "%world", "%x", "%y", "%z"}, new String[]{player.getName(), player.getWorld().getName(), String.valueOf(loc.getPosition().getFloorX()),
                            String.valueOf(loc.getPosition().getFloorY()), String.valueOf(loc.getPosition().getFloorZ())})));
            return CommandResult.success();
        }
        if(!src.hasPermission("raspess.setspawnpoint.others")) {
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
            return CommandResult.success();
        }
        String[] args = arguments.split(" ");
        if(args.length < 4){
            if (!(src instanceof Player)) {
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "onlyPlayer")));
                return CommandResult.success();
            }
        }
        Player target = null;
        for(Player p : plugin.getGame().getServer().getOnlinePlayers()){
            if(p.getName().toLowerCase().contains(args[0].toLowerCase())){
                target = p;
            }
        }
        if(target == null){
            //TODO: When playerfiles are done, offline spawn setting will be possible.
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getPlaceholderMessage(src, null, "notOnline", new String[]{"%player"}, new String[] {args[0]})));
            return CommandResult.success();
        }

        Vector3d loc;
        Vector3d rotation;
        World world;

        if(args.length >= 4){
            loc = new Vector3d(Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]));
            rotation = new Vector3d(0, 0, 0);
            if(args.length == 5){
                world = plugin.getGame().getServer().getWorld(args[4]).get();
            } else {
                world = plugin.getGame().getServer().getWorld(plugin.getGame().getServer().getDefaultWorldName()).get();
            }
        } else {
            Player player = (Player) src;
            loc = player.getLocation().getPosition();
            rotation = player.getRotation();
            world = player.getWorld();
        }
        plugin.getManager().getSpawnHandler().setRespawn(target.getName(), world, loc, rotation);
        src.sendMessage(Text.of(plugin.getManager().getLangHandler().getPlaceholderMessage(src, null, "setPlayerSpawn",
                new String[] {"%player", "%world", "%x", "%y", "%z"}, new String[]{target.getName(), world.getName(), String.valueOf(loc.getFloorX()),
                        String.valueOf(loc.getFloorY()), String.valueOf(loc.getFloorZ())})));
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
    private final Optional<Text> desc = Optional.of(Text.of("/spawnpoint <@player> <@x> <@y> <@z> <@world>"));

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
        return Text.of("/spawnpoint (player) (x) (y) (z) (world)");
    }
}
