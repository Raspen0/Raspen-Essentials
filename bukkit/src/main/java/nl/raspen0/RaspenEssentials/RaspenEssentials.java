package nl.raspen0.RaspenEssentials;

import nl.raspen0.RaspenEssentials.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RaspenEssentials extends JavaPlugin implements Listener {

    private ClassManager manager;
	
    @Override
    public void onEnable() {
        saveDefaultConfig();
        manager = new ClassManager(this);
        if(getConfig().getString("localeDetectMode").equals("permission")){
            manager.getLangHandler().localemode = 1;
        }
        loadCommands();
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    public ClassManager getManager(){
    	return manager;
    }
    
    private void loadCommands(){
        if(getConfig().getBoolean("heal", true)){
        	getCommand("heal").setExecutor(new CommandHeal(this));
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
}
