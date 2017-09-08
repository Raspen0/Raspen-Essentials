package nl.raspen0.RaspenEssentials;

import nl.raspen0.RaspenEssentials.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class RaspenEssentials extends JavaPlugin implements Listener {

    private ClassManager manager;
	
    @Override
    public void onEnable() {
        saveDefaultConfig();
        manager = new ClassManager(this);
        if(getConfig().getString("localeDetectMode").equals("permission")){
            manager.getLangHandler().localemode = 1;
        }
        registerCommands();
        registerEvents();
    }
    
    public ClassManager getManager(){
    	return manager;
    }

    private void registerEvents(){
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(manager.getSpawnHandler(), this);
    }
    
    private void registerCommands(){
        Map<String, CommandExecutor> map = new HashMap<>();
        map.put("heal", new CommandHeal(this));
        map.put("feed", new CommandFeed(this));
        map.put("gamemode", new CommandGamemode(this));
        map.put("fly", new CommandFly(this));
        map.put("spawn", new CommandSpawn(this));
        map.put("setspawn", new CommandSetSpawn(this));

        for (String s : map.keySet()) {
            Boolean value = getConfig().getBoolean("commands." + s, true);
            if (value) {
                getCommand(s).setExecutor(map.get(s));
                log("Registering " + s + " command.");
            }
        }
        //Will get a config option later.
        getCommand("spawnpoint").setExecutor(new CommandSpawnPoint(this));

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
        manager.getPlayerDataHandler().loadPlayerData(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
	}
}
