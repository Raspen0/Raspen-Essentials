package nl.raspen0.RaspenEssentials;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class RaspenEssentials extends JavaPlugin {
    @Override
    public void onEnable() {
        // Save a copy of the default config.yml if one is not there
        this.saveDefaultConfig();
        if(getConfig().getBoolean("heal", true)){
        	getCommand("heal").setExecutor(new CommandHeal());
        	} else {
        	      getLogger().info("Heal is disabled!");
        	}
        // TODO Insert logic to be performed when the plugin is enabled
    	getCommand("feed").setExecutor(new CommandFeed());
        getServer().getPluginManager().registerEvents(new Listener() {  
        	
        }, this);
        
        	getCommand("gamemode").setExecutor(new CommandGamemode());
            getServer().getPluginManager().registerEvents(new Listener() { 
            }, this);
            	
            	getCommand("fly").setExecutor(new CommandFly());
                getServer().getPluginManager().registerEvents(new Listener() { 
                }, this);
                
            	getCommand("spawn").setExecutor(new CommandSpawn());
                getServer().getPluginManager().registerEvents(new Listener() { 
                }, this);
                
            	getCommand("setspawn").setExecutor(new CommandSpawn());
                getServer().getPluginManager().registerEvents(new Listener() { 
                	
            @EventHandler
            public void playerJoin(PlayerJoinEvent event) {
                // On player join send them the message from config.yml
                event.getPlayer().sendMessage(RaspenEssentials.this.getConfig().getString("MOTD"));
            }
        }, this);
        
    }


     
    
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }




//reload command
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	ConsoleCommandSender console = Bukkit.getConsoleSender();
		 if (cmd.getName().equalsIgnoreCase("rreload")) {
			    if(sender instanceof Player){
		//reload permission check
					Player player = (Player) sender;
		if(!player.hasPermission("raspen.reload")){
		sender.sendMessage(ChatColor.RED + "[REssentials] You don't have the permission to use this");
		return true;
		}
	    this.reloadConfig();
	    player.sendMessage(ChatColor.GREEN + "[REssentials] The config had been reloaded.");
		 }
			    else if(sender instanceof ConsoleCommandSender)
			    this.reloadConfig();
			 console.sendMessage(ChatColor.GREEN + "[REssentials] The config has been reloaded.");
	    return true;
				}
		return false;
    }
}
