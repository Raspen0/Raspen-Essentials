package nl.raspen0.RaspenEssentials.handlers;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
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
    public Location<World> spawnloc;
    public Vector3d spawnrotation;

    public SpawnHandler(RaspenEssentials plugin){
        this.plugin = plugin;
        loadSpawn();
    }

    public void setRespawn(String playername, World world, Vector3d loc, Vector3d rotation){
        plugin.getManager().getPlayerDataHandler().playerdata.get(playername).setRespawnLoc(loc);
        plugin.getManager().getPlayerDataHandler().playerdata.get(playername).setRespawnRotation(rotation);
        plugin.getManager().getPlayerDataHandler().playerdata.get(playername).setRespawnWorld(world);
        plugin.getManager().getPlayerDataHandler().saveRespawnLoc(playername);
    }

    @Listener
    public void playerSleep(SleepingEvent event){
        Player player = (Player) event.getTargetEntity();
        Location loc = player.getLocation();
        setRespawn(player.getName(), player.getWorld(), loc.getPosition(), new Vector3d(0, 0, 0));
    }

    @Listener
    public void playerRespawn(RespawnPlayerEvent event){
        String playername = event.getTargetEntity().getName();
        Transform<World> transform = new Transform<>(plugin.getManager().getPlayerDataHandler().playerdata.get(playername).getRespawnWorld(),
                plugin.getManager().getPlayerDataHandler().playerdata.get(playername).getRespawnLoc(),
                plugin.getManager().getPlayerDataHandler().playerdata.get(playername).getRespawnRotation());
        event.setToTransform(transform);
    }

    @Listener
    public void playerLeave(ClientConnectionEvent.Disconnect e){
        plugin.getManager().getPlayerDataHandler().playerdata.remove(e.getTargetEntity().getName());
    }


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
                spawnloc = new Location<>(plugin.getGame().getServer().getWorld(plugin.getGame().getServer().getDefaultWorldName()).get(),
                        plugin.getGame().getServer().getDefaultWorld().get().getSpawnPosition());
                spawnrotation = new Vector3d(0, 0, 0);
                return;
            }

            Double x = root.getNode("spawn", "x").getDouble();
            Double y = root.getNode("spawn", "y").getDouble();
            Double z = root.getNode("spawn", "z").getDouble();
            Double rotationX = root.getNode("spawn", "rotationX").getDouble();
            Double rotationY = root.getNode("spawn", "rotationY").getDouble();

            spawnrotation = new Vector3d(rotationX, rotationY, 0);
            spawnloc = new Location<>(world, x, y, z);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
