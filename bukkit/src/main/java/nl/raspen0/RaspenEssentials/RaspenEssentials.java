package nl.raspen0.RaspenEssentials;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import nl.raspen0.RaspenEssentials.commands.CommandFeed;
import nl.raspen0.RaspenEssentials.commands.CommandFly;
import nl.raspen0.RaspenEssentials.commands.CommandGamemode;
import nl.raspen0.RaspenEssentials.commands.CommandHeal;
import nl.raspen0.RaspenEssentials.commands.CommandReload;
import nl.raspen0.RaspenEssentials.commands.CommandSpawn;

public class RaspenEssentials extends JavaPlugin implements Listener {
	
    public Map<String, String> langlist = new HashMap<String, String>();
	
    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadLangs();
        loadCommands();
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    private void loadLangs(){
    	//Load the languages listed in the config
    	List<String> langs = getConfig().getStringList("langs");
    	for(String s : langs){
    		try {
        		YamlConfiguration defaults = new YamlConfiguration();
				defaults.load(new InputStreamReader(getResource("lang/lang_" + s + ".yml")));
				for(String key : defaults.getKeys(false)){
					langlist.put(s + key, defaults.getString(key));
				}
				getLogger().info("Loaded " + s +  " language!");
			} catch (IOException e) {
				getLogger().warning("Language " + s + " doesn't exist!");
			} catch (InvalidConfigurationException e) {
				getLogger().severe("Error loading language " + s + "!");
			}
    	}
    }
    
    private void loadCommands(){
        if(getConfig().getBoolean("heal", true)){
        	getCommand("heal").setExecutor(new CommandHeal());
        	} else {
        	      getLogger().info(Strings.prefix + "Heal is disabled!");
        	}
		getCommand("feed").setExecutor(new CommandFeed());
		getCommand("gamemode").setExecutor(new CommandGamemode());
		getCommand("fly").setExecutor(new CommandFly());
		getCommand("spawn").setExecutor(new CommandSpawn());
		getCommand("setspawn").setExecutor(new CommandSpawn()); 
		getCommand("rereload").setExecutor(new CommandReload(this));
    }
    
    @Override
    public void onDisable() {
    }
    

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		// On player join send them the message from config
		event.getPlayer().sendMessage(RaspenEssentials.this.getConfig().getString("MOTD"));
	}
}
