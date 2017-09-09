package nl.raspen0.RaspenEssentials.handlers;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import nl.raspen0.RaspenEssentials.PlayerData;
import nl.raspen0.RaspenEssentials.RELocation;
import nl.raspen0.RaspenEssentials.RaspenEssentials;

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

    /**
     * Creates the playerdata directory if it doesn't exists.
     */
    private void createDir(){
        File dir = new File(plugin.configDir + "/playerdata/");
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    /**
     * Saves the respawn location to a player's data file.
     * @param playername The name of the player.
     */
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
            node.getNode("respawnLocation", "pitch").setValue(data.getRespawnLoc().getPitch());
            node.getNode("respawnLocation", "yaw").setValue(data.getRespawnLoc().getYaw());
            node.getNode("respawnLocation", "world").setValue(data.getRespawnLoc().getWorld());

            loader.save(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a players data from their data file.
     * @param UUID The UUID of the player.
     * @param playername The name of the player.
     */
    public void loadPlayerData(String UUID, String playername) {
        try {
            File file = new File(plugin.configDir + "/playerdata/" + UUID + ".json");
            ConfigurationNode node;
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder().setFile(file).build();
            node = loader.load();
            processPlayerData(UUID, playername, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes the loaded PlayerData.
     * @param UUID The UUID of the player.
     * @param playername The name of the player.
     * @param node The ConfigurationNode that contains the loaded data.
     */
    private void processPlayerData(String UUID, String playername, ConfigurationNode node){
        RELocation loc;

        if(node.getNode("respawnLocation") == null){
            System.out.println("Respawn Null!");
            loc = plugin.getManager().getSpawnHandler().spawnloc;
        } else {
            loc = new RELocation(node.getNode("respawnLocation", "world").getString(), new Vector3i(node.getNode("respawnLocation", "x").getDouble(), node.getNode("respawnLocation", "y").getDouble(),
                    node.getNode("respawnLocation", "z").getDouble()), new Vector3d(node.getNode("respawnLocation", "pitch").getDouble(),
                    node.getNode("respawnLocation", "yaw").getDouble(), 0));
        }

        PlayerData data = new PlayerData(UUID, loc);
        playerdata.put(playername, data);
    }
}
