package nl.raspen0.RaspenEssentials.handlers;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SpawnHandler implements Listener{

    private final RaspenEssentials plugin;
    public Location spawnloc;

    public SpawnHandler(RaspenEssentials plugin){
        this.plugin = plugin;
        loadSpawn();
    }

    public void setRespawn(String playername, Location loc){
        plugin.getManager().getPlayerDataHandler().playerdata.get(playername).setRespawnLoc(loc);
        plugin.getManager().getPlayerDataHandler().saveRespawnLoc(playername);
    }

    @EventHandler
    public void playerSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        //Make player look straight
        loc.setYaw(0);
        loc.setPitch(0);
        //TODO: Set Yaw, Pitch??
        setRespawn(player.getName(), loc);
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent event){
        event.setRespawnLocation(plugin.getManager().getPlayerDataHandler().playerdata.get(event.getPlayer().getName()).getRespawnLoc());
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent event){
        plugin.getManager().getPlayerDataHandler().playerdata.remove(event.getPlayer().getName());
    }


    private void loadSpawn(){
        File dataFile = new File(plugin.getDataFolder(), "spawn.conf");
        try {
            if (Files.notExists(dataFile.toPath())) {
                plugin.saveResource("spawn.conf", false);
            }

            ConfigurationNode root;
            ConfigurationLoader<CommentedConfigurationNode> loader =
                    HoconConfigurationLoader.builder().setFile(dataFile).build();
            root = loader.load();

            World world;
            world = plugin.getServer().getWorld(root.getNode("spawn", "world").getString());
            if(world == null){
                //Load world spawn if spawn in config is not set yet or invalid.
                plugin.log("Spawn world not found!");
                World defaultworld = plugin.getServer().getWorlds().get(0);
                spawnloc = defaultworld.getSpawnLocation();
                return;
            }

            Double x = root.getNode("spawn", "x").getDouble();
            Double y = root.getNode("spawn", "y").getDouble();
            Double z = root.getNode("spawn", "z").getDouble();
            Float yaw = root.getNode("spawn", "yaw").getFloat();
            Float pitch = root.getNode("spawn", "pitch").getFloat();

            spawnloc = new Location(world, x, y, z, yaw, pitch);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
