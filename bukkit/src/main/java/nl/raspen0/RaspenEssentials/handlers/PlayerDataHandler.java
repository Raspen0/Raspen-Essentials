package nl.raspen0.RaspenEssentials.handlers;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import nl.raspen0.RaspenEssentials.PlayerData;
import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.bukkit.Location;

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
        File dir = new File(plugin.getDataFolder() + "/playerdata/");
        if(!dir.exists()){
            dir.mkdir();
        }
    }

    public void saveRespawnLoc(String playername) {
        PlayerData data = playerdata.get(playername);
        try {
            createDir();
            File file = new File(plugin.getDataFolder() + "/playerdata/" + data.getUUID() + ".json");
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
            node.getNode("respawnLocation", "yaw").setValue(data.getRespawnLoc().getYaw());
            node.getNode("respawnLocation", "pitch").setValue(data.getRespawnLoc().getPitch());
            node.getNode("respawnLocation", "world").setValue(data.getRespawnLoc().getWorld().getName());

            loader.save(node);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayerData(String UUID, String playername) {
        try {
            File file = new File(plugin.getDataFolder() + "/playerdata/" + UUID + ".json");
            ConfigurationLoader<CommentedConfigurationNode> loader =
                    HoconConfigurationLoader.builder().setFile(file).build();
            ConfigurationNode node = loader.load();
            processPlayerData(UUID, playername, node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processPlayerData(String UUID, String playername, ConfigurationNode node){
        Location loc;
        if(node.getNode("respawnLocation").getValue() == null){
            System.out.println("Respawn Null!");
            loc = plugin.getManager().getSpawnHandler().spawnloc;
        } else {
            loc = new Location(plugin.getServer().getWorld(node.getNode("respawnLocation", "world").getString()),
                    node.getNode("respawnLocation", "x").getDouble(), node.getNode("respawnLocation", "y").getDouble(),
                    node.getNode("respawnLocation", "z").getDouble(), node.getNode("respawnLocation", "yaw").getFloat(),
                    node.getNode("respawnLocation", "pitch").getFloat());
        }

        PlayerData data = new PlayerData(UUID, loc);
        playerdata.put(playername, data);
    }
}
