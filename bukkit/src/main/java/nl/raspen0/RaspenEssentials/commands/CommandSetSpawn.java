package nl.raspen0.RaspenEssentials.commands;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CommandSetSpawn implements CommandExecutor {

    private final RaspenEssentials plugin;

    public CommandSetSpawn(RaspenEssentials plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setspawn")) {
            //Check Permission
            if(!sender.hasPermission("raspess.setspawn")){
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "noPerm"));
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage(plugin.getManager().getLangHandler().getMessage(sender, null, "onlyPlayer"));
                return true;
            }

            Player player = (Player) sender;
            Location loc = player.getLocation();
            //player.getWorld().setSpawnLocation(X, Y, Z);
            setSpawn(player);
            player.sendMessage(plugin.getManager().getLangHandler().getMessage(player, null, "setSpawn").replace("%x", String.valueOf(loc.getX()))
                    .replace("%y", String.valueOf(loc.getY())).replace("%z", String.valueOf(loc.getZ())));
        }
        return true;

    }

    private void setSpawn(Player player){
        File dataFile = new File(plugin.getDataFolder(), "spawn.conf");
        try {
            if (Files.notExists(dataFile.toPath())) {
                plugin.saveResource("spawn.conf", false);
            }

            ConfigurationLoader<CommentedConfigurationNode> loader =
                    HoconConfigurationLoader.builder().setFile(dataFile).build();
            ConfigurationNode root = loader.load();

            Location loc = player.getLocation();
            root.getNode("spawn", "world").setValue(loc.getWorld().getName());
            root.getNode("spawn", "x").setValue(loc.getX());
            root.getNode("spawn", "y").setValue(loc.getY());
            root.getNode("spawn", "z").setValue(loc.getZ());
            root.getNode("spawn", "yaw").setValue(loc.getYaw());
            root.getNode("spawn", "pitch").setValue(loc.getPitch());
            loader.save(root);

            plugin.getManager().getSpawnHandler().spawnloc = new Location (loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
