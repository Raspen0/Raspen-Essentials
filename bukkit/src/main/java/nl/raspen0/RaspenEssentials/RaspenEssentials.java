package nl.raspen0.RaspenEssentials;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.permission.Permission;
import nl.raspen0.RaspenEssentials.commands.CommandFeed;
import nl.raspen0.RaspenEssentials.commands.CommandFly;
import nl.raspen0.RaspenEssentials.commands.CommandGamemode;
import nl.raspen0.RaspenEssentials.commands.CommandHeal;
import nl.raspen0.RaspenEssentials.commands.CommandReload;
import nl.raspen0.RaspenEssentials.commands.CommandSpawn;

public class RaspenEssentials extends JavaPlugin implements Listener {
	
	public List<String> loadedLangs;
    public Map<String, String> langlist = new HashMap<String, String>();
    private static Permission perms = null;
    private ClassManager manager;
	
    @Override
    public void onEnable() {
        saveDefaultConfig();
        manager = new ClassManager(this);
        if(getConfig().getString("localeDetectMode").equals("permission")){
            if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
            	setupPermissions();
            	manager.getLangHandler().localemode = 1;
            } else {
            	log(ChatColor.RED + "Warning: Vault is not enabled!, permission based locale detection is not availible!");
            	log(ChatColor.RED + "Setting Locale Detection to default.");
            }
        }

        loadLangs();
        loadCommands();
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    
    private void loadLangs(){
    	//Load the languages listed in the config
    	loadedLangs = getConfig().getStringList("langs");
    	List<String> langs = new ArrayList<String>(loadedLangs);
    	//Load enabled language overrides from /RaspenEssentials/lang/ if the folder exists
    	File dir = new File(getDataFolder() + "/lang/");
    	if(dir.exists()){
    		Iterator<String> iterator = langs.iterator();
    		while(iterator.hasNext()){
    			String it = iterator.next();
        		File file = new File(dir + "/lang_" + it + ".yml");
				if(!file.exists()){
					continue;
				}
				FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
				for(String key : lang.getKeys(false)){
					langlist.put(it + key, lang.getString(key));
				}
				log("Loaded custom " + it +  " language!");
				//Remove lang from list so it doesn't get loaded 2 times.
				iterator.remove();
    		}
    	}
    	//Load enabled languages from jar
    	for(String s : langs){
    		try {
        		YamlConfiguration defaults = new YamlConfiguration();
				defaults.load(new InputStreamReader(getResource("lang/lang_" + s + ".yml")));
				for(String key : defaults.getKeys(false)){
					langlist.put(s + key, defaults.getString(key));
				}
				log("Loaded " + s +  " language!");
			} catch (IOException e) {
				log(ChatColor.RED + "Language " + s + " doesn't exist!");
			} catch (NullPointerException e) {
				log(ChatColor.RED + "Language " + s + " doesn't exist!");
			} catch (InvalidConfigurationException e) {
				log(ChatColor.RED + "Error loading language " + s + "!");
			}
    	}
    }
    
    public ClassManager getManager(){
    	return manager;
    }
    
    private void loadCommands(){
        if(getConfig().getBoolean("heal", true)){
        	getCommand("heal").setExecutor(new CommandHeal());
        	} else {
        		log("Heal is disabled!");
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
    
    public void log(String s){
    	Bukkit.getConsoleSender().sendMessage("[RE] " + s);
    }
    

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		// On player join send them the message from config
		event.getPlayer().sendMessage(RaspenEssentials.this.getConfig().getString("MOTD"));
		event.getPlayer().sendMessage(getManager().getLangHandler().getMessage(event.getPlayer(), null, "startup"));
	}
	
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
