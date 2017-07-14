package nl.raspen0.RaspenEssentials;

import nl.raspen0.RaspenEssentials.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        if(getConfig().getBoolean("commands.heal", true)){
        	getCommand("heal").setExecutor(new CommandHeal(this));
        }
        if(getConfig().getBoolean("commands.feed", true)) {
            getCommand("feed").setExecutor(new CommandFeed(this));
        }
        if(getConfig().getBoolean("commands.gamemode", true)) {
            getCommand("gamemode").setExecutor(new CommandGamemode(this));
        }
        if(getConfig().getBoolean("commands.fly", true)) {
            getCommand("fly").setExecutor(new CommandFly(this));
        }
        if(getConfig().getBoolean("commands.spawn", true)) {
            getCommand("spawn").setExecutor(new CommandSpawn(this));
        }
        if(getConfig().getBoolean("commands.setspawn", true)) {
            getCommand("setspawn").setExecutor(new CommandSetSpawn(this));
        }
		getCommand("raspenessentials").setExecutor(new CommandMain(this));
    }
    
    @Override
    public void onDisable() {
        getManager().getLangHandler().unloadLangs();
    }
    
    public void log(String s){
    	Bukkit.getConsoleSender().sendMessage("[RE] " + ChatColor.translateAlternateColorCodes('&', s));
    }
    

	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		// On player join send them the message from config
		event.getPlayer().sendMessage(RaspenEssentials.this.getConfig().getString("MOTD"));
	}
}
