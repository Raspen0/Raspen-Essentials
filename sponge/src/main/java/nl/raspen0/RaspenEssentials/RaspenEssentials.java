package nl.raspen0.RaspenEssentials;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import nl.raspen0.RaspenEssentials.commands.CommandFeed;
import nl.raspen0.RaspenEssentials.commands.CommandGamemode;
import nl.raspen0.RaspenEssentials.commands.CommandHeal;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Plugin(id="raspenessentials", name="RaspenEssentials", version= Strings.version)
public class RaspenEssentials {

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    @Inject @DefaultConfig(sharedRoot = false)
    public Path dir;

    @Inject @DefaultConfig(sharedRoot = false)
    private ConfigurationLoader<CommentedConfigurationNode> configLoader;

    private Config config;
    private ClassManager manager;

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
        manager = new ClassManager(this);
        if(config.localeDetectMode.equals("permission")){
            manager.getLangHandler().localemode = 1;
        }
    }


  @Listener
  public void onServerStart(GameStartedServerEvent event) throws IOException, ObjectMappingException {
      CommandManager cmdService = Sponge.getCommandManager();

      Map<String, CommandCallable> map = new HashMap<>();
      map.put("heal", new CommandHeal(this));
      map.put("feed", new CommandFeed(this));
      map.put("gamemode,gm", new CommandGamemode(this));

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
              Boolean value = root.getNode("commands", s).getBoolean(true);
              if (value) {
                  cmdService.register(this, map.get(s), s.split(","));
                  getLogger().info("Registering " + s.split(",")[0] + " command.");
              }
          }
      }




 //     for(String s : map.keySet()){
 //         try {
 //             Class aClass = config.commands.getClass();
 //             Method method = aClass.getMethod(s, null);
 //             boolean returnValue = (boolean) method.invoke(null, null);
 //             if(returnValue) {
 //                 cmdService.register(this, map.get(s), s);
 //             }
 //         } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
 //             e.printStackTrace();
 //         }
  //    }


/*    CommandSpec mainCommandSpec =
    CommandSpec.builder().description(Text.of("Main Command")).permission("raspen.main")
    .arguments(GenericArguments.seq(new CommandElement[] {
    GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.string(Text.of("argument")))) }))
    .executor(new CommandMain()).build();

    CommandSpec feedCommandSpec = 
    CommandSpec.builder().description(Text.of("Feed Command")).permission("raspen.feed")
    .arguments(GenericArguments.seq(new CommandElement[] {
    GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))) }))
    .executor(new CommandFeed()).build();
    
    CommandSpec gamemodeCommandSpec = 
    CommandSpec.builder().description(Text.of("Gamemode Command")).permission("raspen.gamemode")
    .arguments(GenericArguments.seq(new CommandElement[] {
    GenericArguments.onlyOne(GenericArguments.string(Text.of("mode"))),
    GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))) }))
    .executor(new CommandGamemode()).build();
    
    CommandSpec TPCommandSpec =
    CommandSpec.builder().description(Text.of("TP Command")).permission("raspen.tp.main")
    .arguments(GenericArguments.seq(new CommandElement[] {
    GenericArguments.onlyOne(GenericArguments.player(Text.of("arg1"))),
    GenericArguments.optional(GenericArguments.string(Text.of("arg2"))), 
    GenericArguments.optional(GenericArguments.string(Text.of("arg3"))),
    GenericArguments.optional(GenericArguments.string(Text.of("arg4")))}))
    .executor(new CommandTP()).build();

    CommandSpec flyCommandSpec = 
    CommandSpec.builder().description(Text.of("Fly Command")).permission("raspen.fly")
    .executor(new CommandFly()).build();
    
 */
  /*  getGame().getCommandManager().register(this, feedCommandSpec, new String[] { "feed" });
    getGame().getCommandManager().register(this, gamemodeCommandSpec, new String[] { "gamemode", "gm" });
    getGame().getCommandManager().register(this, mainCommandSpec, new String[] { "raspenessentials", "re" });
    getGame().getCommandManager().register(this, TPCommandSpec, new String[] { "tp", "teleport" });
    getGame().getCommandManager().register(this, flyCommandSpec, new String[] { "fly" });
    */
  }

  //Sends Text to the console
  //Used for sending colored messages to the console
  //Messages will not appear in log files.
  public void logConsole(Text text){
      game.getServer().getConsole().sendMessage(text);
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
}