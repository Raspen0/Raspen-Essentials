package nl.raspen0.RaspenEssentials.handlers;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import nl.raspen0.RaspenEssentials.PlayerData;
import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.spongepowered.api.world.World;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataHandler {

    private final RaspenEssentials plugin;
    public Map<String, PlayerData> playerdata = new HashMap<>();

    public PlayerDataHandler(RaspenEssentials plugin){
        this.plugin = plugin;
    }

    private void createDir(){
        File dir = new File(plugin.configDir + "/playerdata/");
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    public void saveRespawnLoc(String playername) {
        PlayerData data = playerdata.get(playername);
        try {
            createDir();
            File file = new File(plugin.configDir + "/playerdata/" + data.getUUID() + ".json");
            ConfigurationNode node;
            if (!file.exists()) {
                file.createNewFile();
            }
            ConfigurationLoader<CommentedConfigurationNode> loader =
                    HoconConfigurationLoader.builder().setFile(file).build();
            node = loader.load();
            node.getNode("respawnLocation", "x").setValue(data.getRespawnLoc().getX());
            node.getNode("respawnLocation", "y").setValue(data.getRespawnLoc().getY());
            node.getNode("respawnLocation", "z").setValue(data.getRespawnLoc().getZ());
            node.getNode("respawnLocation", "rotationX").setValue(data.getRespawnRotation().getX());
            node.getNode("respawnLocation", "rotationY").setValue(data.getRespawnRotation().getY());
            node.getNode("respawnLocation", "world").setValue(data.getRespawnWorld().getName());

            loader.save(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerData(String UUID, String playername) {
        try {
            File file = new File(plugin.configDir + "/playerdata/" + UUID + ".json");
            ConfigurationNode node;
//            if (!file.exists()) {
//                node = loadDefault();
//            } else {
                ConfigurationLoader<CommentedConfigurationNode> loader =
                        HoconConfigurationLoader.builder().setFile(file).build();
                node = loader.load();
 //           }
            processPlayerData(UUID, playername, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processPlayerData(String UUID, String playername, ConfigurationNode node){
        Vector3d loc;
        Vector3d rotation;
        //TODO: Make world string??
        World world;
        if(node.getNode("respawnLocation") == null){
            System.out.println("Respawn Null!");
            loc = plugin.getManager().getSpawnHandler().spawnloc.getPosition();
            rotation = plugin.getManager().getSpawnHandler().spawnrotation;
            world = plugin.getManager().getSpawnHandler().spawnloc.getExtent();
        } else {
            loc = new Vector3d(node.getNode("respawnLocation", "x").getDouble(), node.getNode("respawnLocation", "y").getDouble(),
                    node.getNode("respawnLocation", "z").getDouble());
            rotation = new Vector3d(node.getNode("respawnLocation", "rotationX").getDouble(), node.getNode("respawnLocation", "rotationY").getDouble(), 0);
            world =  plugin.getGame().getServer().getWorld(node.getNode("respawnLocation", "world").getString()).get();
        }

        PlayerData data = new PlayerData(UUID, loc, rotation, world);
        playerdata.put(playername, data);
    }
}
