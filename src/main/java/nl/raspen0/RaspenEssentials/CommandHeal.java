package nl.raspen0.RaspenEssentials;

import java.util.Optional;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CommandHeal implements CommandExecutor
{
  public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
    Optional<Player> target = ctx.getOne("player");
    if (!target.isPresent()) {
      if (src instanceof Player) {
        Player player = (Player)src;
        player.offer(Keys.HEALTH, (Double)player.get(Keys.MAX_HEALTH).get());
        player.sendMessage(Text.of("You've been healed"));
      }
      if (src instanceof ConsoleSource || (src instanceof CommandBlockSource)){
    	  src.sendMessage(Text.of(TextColors.RED,("This command can only be run by a player")));
      }
    }
    else if(target.isPresent()) {
    		if(src.hasPermission("raspen.heal.others")) {
      Player player = (Player)target.get();
      player.offer(Keys.HEALTH, (Double)player.get(Keys.MAX_HEALTH).get());
      player.sendMessage(Text.of("You've been healed"));
      if (player != src) {
        src.sendMessage(Text.of(player.getName() + " has been healed"));
      }
    } else {
    		src.sendMessage(Text.of(TextColors.RED,("You do not have permission to heal other players")));
    }
    } 
    return CommandResult.success();
  }
}
