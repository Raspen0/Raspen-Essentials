package nl.raspen0.RaspenEssentials;

import com.flowpowered.math.vector.Vector3d;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import nl.raspen0.RaspenEssentials.commands.*;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Plugin(id="raspenessentials", name="RaspenEssentials", version= Strings.version)
public class RaspenEssentials {

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    @Inject @DefaultConfig(sharedRoot = false)
    public Path dir;

    @Inject @ConfigDir(sharedRoot = false)
    public File configDir;

    @Inject @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private Config config;
    private ClassManager manager;


    public Location<World> spawnloc;
    public Vector3d spawnrotation;

  public Logger getLogger() {
    return logger;
  }

  public Game getGame() {
    return game;
  }

  public Config getConfig(){
      return config;
  }

  public ClassManager getManager(){
      return manager;
  }

    @Listener
    public void onPreInit(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        getLogger().info("Loading RaspenEssentials " + Strings.version);
        loadConfig();
        loadSpawn();
        manager = new ClassManager(this);
        if(config.localeDetectMode.equals("permission")){
            manager.getLangHandler().localemode = 1;
        }
    }


  @Listener
  public void onServerStart(GameStartedServerEvent event) throws IOException, ObjectMappingException {
      CommandManager cmdService = Sponge.getCommandManager();
      cmdService.register(this, new CommandMain(this), "raspenessentials", "re");

      Map<String, CommandCallable> map = new HashMap<>();
      map.put("heal", new CommandHeal(this));
      map.put("feed", new CommandFeed(this));
      map.put("gamemode,gm", new CommandGamemode(this));
      map.put("fly", new CommandFly(this));
      map.put("spawn", new CommandSpawn(this));
      map.put("setspawn", new CommandSetSpawn(this));

      //Register commands in map
      ConfigurationNode root = null;
      try {
          root = configLoader.load();
      } catch (IOException e) {
          logger.error("Error loading config!");
          mapDefault();
      }
      if(root != null) {
          for (String s : map.keySet()) {
              String id = s.split(",")[0];
              Boolean value = root.getNode("commands", id).getBoolean(true);
              if (value) {
                  cmdService.register(this, map.get(s), s.split(","));
                  getLogger().info("Registering " + id + " command.");
              }
          }
      }


/*
    CommandSpec TPCommandSpec =
    CommandSpec.builder().description(Text.of("TP Command")).permission("raspen.tp.main")
    .arguments(GenericArguments.seq(new CommandElement[] {
    GenericArguments.onlyOne(GenericArguments.player(Text.of("arg1"))),
    GenericArguments.optional(GenericArguments.string(Text.of("arg2"))), 
    GenericArguments.optional(GenericArguments.string(Text.of("arg3"))),
    GenericArguments.optional(GenericArguments.string(Text.of("arg4")))}))
    .executor(new CommandTP()).build();

    
 */
  /*  getGame().getCommandManager().register(this, TPCommandSpec, new String[] { "tp", "teleport" });
    */
  }

  //Sends Text to the console
  //Used for sending colored messages to the console
  //Messages will not appear in log files.
  public void logConsole(Text text){
      game.getServer().getConsole().sendMessage(text);
  }

  public void reloadConfig() throws IOException, ObjectMappingException {
      config = new Config();
      try {
          loadConfig();
      } catch (IOException ex) {
          logger.error("Error loading config!");
          mapDefault();
          throw ex;
      } catch (ObjectMappingException ex) {
          logger.error("Invalid config file!");
          mapDefault();
          throw ex;
      }
  }

  private void loadConfig() throws IOException, ObjectMappingException{
      Asset conf = game.getAssetManager().getAsset(this, "raspenessentials.conf").get();
      ConfigurationNode root;
      try {
          if (!Files.exists(dir)) {
              conf.copyToFile(dir);
          }
          root = configLoader.load();
          config = root.getValue(Config.type);
      } catch (IOException ex) {
          logger.error("Error loading config!");
          mapDefault();
          throw ex;
      } catch (ObjectMappingException ex) {
          logger.error("Invalid config file!");
          mapDefault();
          throw ex;
      }
  }
    private void mapDefault() throws IOException, ObjectMappingException {
        try {
            config = loadDefault().getValue(Config.type);
        } catch (IOException | ObjectMappingException ex) {
            logger.error("Could not load the embedded default config! Disabling plugin.");
            game.getEventManager().unregisterPluginListeners(this);
            throw ex;
        }
    }

    private ConfigurationNode loadDefault() throws IOException {
        return HoconConfigurationLoader.builder().setURL(game.getAssetManager().getAsset(this, "raspenessentials.conf").get().getUrl()).build().load(configLoader.getDefaultOptions());
    }

  @Listener
  public void playerJoin(ClientConnectionEvent.Join event){
        event.getTargetEntity().sendMessage(Text.of(config.motd));
  }

  private void loadSpawn(){
      File dataFile = new File(configDir, "spawn.conf");
      try {
          if (Files.notExists(dataFile.toPath())) {
              getGame().getAssetManager().getAsset(this, "spawn.conf").get().copyToFile(dataFile.toPath());
          }

          ConfigurationNode root;
          ConfigurationLoader<CommentedConfigurationNode> loader =
                  HoconConfigurationLoader.builder().setFile(dataFile).build();
          root = loader.load();

          World world;
          try {
              world = getGame().getServer().getWorld(root.getNode("spawn", "world").getString()).get();
          } catch (NoSuchElementException e){
              //Load world spawn if spawn in config is not set yet or invalid.
              getLogger().error("Spawn world not found!");
              spawnloc = new Location<>(getGame().getServer().getWorld(getGame().getServer().getDefaultWorldName()).get(),
                      getGame().getServer().getDefaultWorld().get().getSpawnPosition());
              spawnrotation = new Vector3d(0, 0, 0);
              return;
          }

          Double x = root.getNode("spawn", "x").getDouble();
          Double y = root.getNode("spawn", "y").getDouble();
          Double z = root.getNode("spawn", "z").getDouble();
          Double rotationX = root.getNode("spawn", "rotationX").getDouble();
          Double rotationY = root.getNode("spawn", "rotationY").getDouble();
          Double rotationZ = root.getNode("spawn", "rotationZ").getDouble();

          spawnrotation = new Vector3d(rotationX, rotationY, rotationZ);
          spawnloc = new Location<>(world, x, y, z);

      } catch (IOException e) {
          e.printStackTrace();
      }
  }
}