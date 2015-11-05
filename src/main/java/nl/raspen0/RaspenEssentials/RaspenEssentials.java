package nl.raspen0.RaspenEssentials;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class RaspenEssentials extends JavaPlugin {
	
	public static RaspenEssentials plugin;
	
    @Override
    public void onEnable() {
    	
    	plugin = this;
    	
        // Save a copy of the default config.yml if one is not there
        this.saveDefaultConfig();
        
        if(getConfig().getBoolean("heal", true)){
        	getCommand("heal").setExecutor(new CommandHeal());
        	} else {
        	      getLogger().info(Strings.prefix + "Heal is disabled!");
        	}
        
        
		getCommand("feed").setExecutor(new CommandFeed());
		getServer().getPluginManager().registerEvents(new Listener() {}, this);

		getCommand("gamemode").setExecutor(new CommandGamemode());
		getServer().getPluginManager().registerEvents(new Listener() {}, this);

		getCommand("fly").setExecutor(new CommandFly());
		getServer().getPluginManager().registerEvents(new Listener() {}, this);

		getCommand("spawn").setExecutor(new CommandSpawn());
		getServer().getPluginManager().registerEvents(new Listener() {}, this);

		getCommand("setspawn").setExecutor(new CommandSpawn());
		getServer().getPluginManager().registerEvents(new Listener() {}, this);
      
		getCommand("rereload").setExecutor(new CommandReload());
		getServer().getPluginManager().registerEvents(new Listener() {}, this);
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
