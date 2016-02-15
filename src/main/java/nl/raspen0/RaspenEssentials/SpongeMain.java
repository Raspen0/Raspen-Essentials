package nl.raspen0.RaspenEssentials;

import javax.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import nl.raspen0.RaspenEssentials.Sponge.CommandFeed;
import nl.raspen0.RaspenEssentials.Sponge.CommandFly;
import nl.raspen0.RaspenEssentials.Sponge.CommandGamemode;
import nl.raspen0.RaspenEssentials.Sponge.CommandHeal;
import nl.raspen0.RaspenEssentials.Sponge.CommandMain;
import nl.raspen0.RaspenEssentials.Sponge.CommandTP;

@Plugin(id="RaspenEssentials", name="RaspenEssentials", version="1.0")
public class SpongeMain
{
  private Logger logger;
  
  public static String version = "1.0";
  public static String name = "RaspenEssentials";
  
  @Inject
  private void setLogger(Logger logger)
  {
    this.logger = logger;
  }
  
  public Logger getLogger()
  {
    return this.logger;
  }
  
  public Game getGame()
  {
    return Sponge.getGame();
  }
  
  @Listener
  public void onServerStart(GameStartedServerEvent event)
  {
    getLogger().info("Starting");
    
    CommandSpec mainCommandSpec = 
    CommandSpec.builder().description(Text.of("Main Command")).permission("raspen.main")
    .arguments(GenericArguments.seq(new CommandElement[] {
    GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.string(Text.of("argument")))) }))
    .executor(new CommandMain()).build();
    
    CommandSpec healCommandSpec = 
    CommandSpec.builder().description(Text.of("Heal Command")).permission("raspen.heal")
    .arguments(GenericArguments.seq(new CommandElement[] {
    GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))) }))
    .executor(new CommandHeal()).build();
    
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
    GenericArguments.onlyOne(GenericArguments.string(Text.of("arg1"))),
    GenericArguments.optional(GenericArguments.string(Text.of("arg2"))), 
    GenericArguments.optional(GenericArguments.string(Text.of("arg3"))),
    GenericArguments.optional(GenericArguments.string(Text.of("arg4")))}))
    .executor(new CommandTP()).build();
    
    CommandSpec flyCommandSpec = 
    CommandSpec.builder().description(Text.of("Fly Command")).permission("raspen.fly")
    .executor(new CommandFly()).build();
    
    getGame().getCommandManager().register(this, TPCommandSpec, new String[] { "tp", "teleport" });
    getGame().getCommandManager().register(this, flyCommandSpec, new String[] { "fly" });
    getGame().getCommandManager().register(this, healCommandSpec, new String[] { "heal" });
    getGame().getCommandManager().register(this, feedCommandSpec, new String[] { "feed" });
    getGame().getCommandManager().register(this, gamemodeCommandSpec, new String[] { "gamemode", "gm" });
    getGame().getCommandManager().register(this, mainCommandSpec, new String[] { "raspenessentials", "re" });
  }
}
