package nl.raspen0.RaspenEssentials.handlers;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import nl.raspen0.RaspenEssentials.Config;
import nl.raspen0.RaspenEssentials.RaspenEssentials;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ConfigHandler {

    private final RaspenEssentials plugin;

    public ConfigHandler(RaspenEssentials plugin){
        this.plugin = plugin;
        try {
            loadConfig();
        } catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    private Config config;

    public Config getConfig(){
        return config;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getConfigLoader() {
        return plugin.configLoader;
    }

    public void reloadConfig() throws IOException, ObjectMappingException {
        config = new Config();
        try {
            loadConfig();
        } catch (IOException ex) {
            plugin.getLogger().error("Error loading config!");
            mapDefault();
            throw ex;
        } catch (ObjectMappingException ex) {
            plugin.getLogger().error("Invalid config file!");
            mapDefault();
            throw ex;
        }
    }

    private void loadConfig() throws IOException, ObjectMappingException{
        File dataFile = new File(plugin.configDir, "raspenessentials.conf");
        try {
            if (Files.notExists(dataFile.toPath())) {
                plugin.getGame().getAssetManager().getAsset(plugin, "raspenessentials.conf").get().copyToFile(dataFile.toPath());
            }
            ConfigurationNode root;
            root = plugin.configLoader.load();
            config = root.getValue(Config.type);
        } catch (IOException ex) {
            plugin.getLogger().error("Error loading config!");
            mapDefault();
            throw ex;
        } catch (ObjectMappingException ex) {
            plugin.getLogger().error("Invalid config file!");
            mapDefault();
            throw ex;
        }
    }

    public void mapDefault() throws IOException, ObjectMappingException {
        try {
            config = loadDefault().getValue(Config.type);
        } catch (IOException | ObjectMappingException ex) {
            plugin.getLogger().error("Could not load the embedded default config! Disabling plugin.");
            plugin.getGame().getEventManager().unregisterPluginListeners(this);
            throw ex;
        }
    }

    private ConfigurationNode loadDefault() throws IOException {
        return HoconConfigurationLoader.builder().setURL(plugin.getGame().getAssetManager().getAsset(this, "raspenessentials.conf").get().getUrl()).build().load(plugin.configLoader.getDefaultOptions());
    }

}
