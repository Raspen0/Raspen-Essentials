package nl.raspen0.RaspenEssentials.commands;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class CommandSetSpawn implements CommandCallable {

    private final RaspenEssentials plugin;

    public CommandSetSpawn(RaspenEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    //Command
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        if (!src.hasPermission("raspess.setspawn")) {
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
            return CommandResult.success();
        }
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "onlyPlayer")));
            return CommandResult.success();
        }
        Player player = (Player) src;
        Location loc = player.getLocation();
        //Save new spawn to config.
       // player.getWorld().getProperties().setSpawnPosition(new Vector3i(loc.getX(), loc.getY(), loc.getZ()));
        setSpawn(player);
        player.sendMessage(plugin.getManager().getLangHandler().getPlaceholderMessage(player, null, "setSpawn", new String[]{"%x", "%y", "%z"},
                new String[]{String.valueOf(loc.getX()), String.valueOf(loc.getY()), String.valueOf(loc.getZ())}));
        return CommandResult.success();
    }

    private void setSpawn(Player player){
        File dataFile = new File(plugin.configDir, "spawn.conf");
        try {
            if (Files.notExists(dataFile.toPath())) {
                plugin.getGame().getAssetManager().getAsset(plugin, "spawn.conf").get().copyToFile(dataFile.toPath());
            }

            ConfigurationNode root;
            ConfigurationLoader<CommentedConfigurationNode> loader =
                    HoconConfigurationLoader.builder().setFile(dataFile).build();
            root = loader.load();

            Location loc = player.getLocation();
            root.getNode("spawn", "world").setValue(player.getWorld().getName());
            root.getNode("spawn", "x").setValue(loc.getX());
            root.getNode("spawn", "y").setValue(loc.getY());
            root.getNode("spawn", "z").setValue(loc.getZ());
            root.getNode("spawn", "rotationX").setValue(player.getRotation().getX());
            root.getNode("spawn", "rotationY").setValue(player.getRotation().getY());
            root.getNode("spawn", "rotationZ").setValue(player.getRotation().getZ());
            loader.save(root);

            plugin.spawnloc = new Location<>(player.getWorld(), loc.getX(), loc.getY(), loc.getZ());
            plugin.spawnrotation = player.getRotation();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    //Tab Complete
    public List<String> getSuggestions(CommandSource src, String args, @Nullable Location<World> location) throws CommandException {
        return null;
    }

    @Override
    public boolean testPermission(CommandSource src) {
        return false;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Text> desc = Optional.of(Text.of("/setspawn"));

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
        return Text.of("/setspawn");
    }
}
