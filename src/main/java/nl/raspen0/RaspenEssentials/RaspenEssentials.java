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

@Plugin(id="RaspenEssentials", name="RaspenEssentials", version="1.0")
public class RaspenEssentials
{
  private Logger logger;
  
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
    
    CommandSpec healCommandSpec = 
      CommandSpec.builder().description(Text.of("Heal Command")).permission("raspen.heal")
      .arguments(GenericArguments.seq(new CommandElement[] {
      GenericArguments.optional(GenericArguments.onlyOne(GenericArguments.player(Text.of("player")))) }))
      .executor(new CommandHeal()).build();
    getGame().getCommandManager().register(this, healCommandSpec, new String[] { "heal" });
  }
}
