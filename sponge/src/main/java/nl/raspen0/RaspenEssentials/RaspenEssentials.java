package nl.raspen0.RaspenEssentials;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import nl.raspen0.RaspenEssentials.commands.*;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Plugin(id="raspenessentials", name="RaspenEssentials", version= "1.0")
public class RaspenEssentials {

    @Inject
    private Logger logger;

    public final String version = "1.0";

    @Inject
    private Game game;

    @Inject @ConfigDir(sharedRoot = false)
    public File configDir;

    @Inject @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private ClassManager manager;

    public Logger getLogger() {
        return logger;
    }

    public Game getGame() {
      return game;
    }

    public ClassManager getManager(){
      return manager;
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        getLogger().info("Loading RaspenEssentials " + version);
        manager = new ClassManager(this);
    }


  @Listener
  public void onServerStart(GameStartedServerEvent event) throws IOException, ObjectMappingException {
      manager.serverStart(this);
      if (manager.getConfigHandler().getConfig().localeDetectMode.equals("permission")) {
          manager.getLangHandler().localemode = 1;
      }
      getGame().getEventManager().registerListeners(this, getManager().getSpawnHandler());
      CommandManager cmdService = Sponge.getCommandManager();
      cmdService.register(this, new CommandMain(this), "raspenessentials", "re");

      Map<String, CommandCallable> map = new HashMap<>();
      map.put("heal", new CommandHeal(this));
      map.put("feed", new CommandFeed(this));
      map.put("gamemode,gm", new CommandGamemode(this));
      map.put("fly", new CommandFly(this));
      map.put("spawn", new CommandSpawn(this));
      map.put("setspawn", new CommandSetSpawn(this));
      map.put("spawnpoint", new CommandSpawnPoint(this));

      //Register commands in map
      ConfigurationNode root = null;
      try {
          root = manager.getConfigHandler().getConfigLoader().load();
      } catch (IOException e) {
          logger.error("Error loading config!");
          manager.getConfigHandler().mapDefault();
      }
      if (root != null) {
          for (String s : map.keySet()) {
              String id = s.split(",")[0];
              Boolean value = root.getNode("commands", id).getBoolean(true);
              if (value) {
                  cmdService.register(this, map.get(s), s.split(","));
                  getLogger().info("Registering " + id + " command.");
              }
          }
      }
  }

  //Sends Text to the console
  //Used for sending colored messages to the console
  //Messages will not appear in log files.
  public void logConsole(Text text){
      game.getServer().getConsole().sendMessage(text);
  }


  @Listener
  public void playerJoin(ClientConnectionEvent.Join event){
      event.getTargetEntity().sendMessage(Text.of(manager.getConfigHandler().getConfig().motd));
      getManager().getPlayerDataHandler().loadPlayerData(event.getTargetEntity().getUniqueId().toString(), event.getTargetEntity().getName());
  }


}