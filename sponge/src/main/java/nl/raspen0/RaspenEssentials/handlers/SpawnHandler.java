package nl.raspen0.RaspenEssentials.handlers;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import nl.raspen0.RaspenEssentials.RELocation;
import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.SleepingEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.NoSuchElementException;

public class SpawnHandler {

    private final RaspenEssentials plugin;
    public RELocation spawnloc;

    public SpawnHandler(RaspenEssentials plugin){
        this.plugin = plugin;
        loadSpawn();
    }

    /**
     * Sets the respawn point of the given player.
     * @param playername The name of the player.
     * @param loc The location that will be the new respawn point.
     */
    public void setRespawn(String playername, RELocation loc){
        System.out.println("Set respawn: " + loc.getX() + "," + loc.getY() + "," + loc.getZ());
        plugin.getManager().getPlayerDataHandler().playerdata.get(playername).setRespawnLoc(loc);
        plugin.getManager().getPlayerDataHandler().saveRespawnLoc(playername);
    }

    @Listener
    public void playerSleep(SleepingEvent event){
        Player player = (Player) event.getTargetEntity();
        Location loc = player.getLocation();
        setRespawn(player.getName(), new RELocation(player.getWorld().getName(), loc.getX(), loc.getY(), loc.getZ(),
                player.getHeadRotation().getX(), player.getHeadRotation().getY()));
    }

    @Listener
    public void playerRespawn(RespawnPlayerEvent event){
        RELocation loc = plugin.getManager().getPlayerDataHandler().playerdata.get(event.getTargetEntity().getName()).getRespawnLoc();

        Transform<World> transform = new Transform<>(plugin.getGame().getServer().getWorld(loc.getWorld()).get(),
                new Vector3d(loc.getX(), loc.getY(), loc.getZ()), new Vector3d(loc.getPitch(), loc.getYaw(), 0));
        event.setToTransform(transform);
    }

    @Listener
    public void playerLeave(ClientConnectionEvent.Disconnect e){
        plugin.getManager().getPlayerDataHandler().playerdata.remove(e.getTargetEntity().getName());
    }

    /**
     * Loads the spawn location from spawn.conf.
     */
    private void loadSpawn(){
        File dataFile = new File(plugin.configDir, "spawn.conf");
        try {
            if (Files.notExists(dataFile.toPath())) {
                plugin.getGame().getAssetManager().getAsset(this, "spawn.conf").get().copyToFile(dataFile.toPath());
            }

            ConfigurationNode root;
            ConfigurationLoader<CommentedConfigurationNode> loader =
                    HoconConfigurationLoader.builder().setFile(dataFile).build();
            root = loader.load();

            World world;
            try {
                world = plugin.getGame().getServer().getWorld(root.getNode("spawn", "world").getString()).get();
            } catch (NoSuchElementException e){
                //Load world spawn if spawn in config is not set yet or invalid.
                plugin.getLogger().error("Spawn world not found!");
                spawnloc = new RELocation(plugin.getGame().getServer().getDefaultWorldName(), plugin.getGame().getServer().getDefaultWorld().get().getSpawnPosition()
                , new Vector3d(0, 0, 0));
                return;
            }

            double x = root.getNode("spawn", "x").getDouble();
            double y = root.getNode("spawn", "y").getDouble();
            double z = root.getNode("spawn", "z").getDouble();
            double pitch = root.getNode("spawn", "pitch").getFloat();
            double yaw = root.getNode("spawn", "yaw").getFloat();

            spawnloc = new RELocation(world.getName(), x, y, z, pitch, yaw);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
