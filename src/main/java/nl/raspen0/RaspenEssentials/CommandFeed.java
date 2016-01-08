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

public class CommandFeed implements CommandExecutor
{
  public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
    Optional<Player> target = ctx.getOne("player");
    if (!target.isPresent()) {
      if (src instanceof Player) {
        Player player = (Player)src;
        player.offer(Keys.FOOD_LEVEL, 20);
        player.offer(Keys.SATURATION, (double) 20);
        player.sendMessage(Text.of("You're now feeded"));
      }
      if (src instanceof ConsoleSource || (src instanceof CommandBlockSource)){
    	  src.sendMessage(Text.of(TextColors.RED,("This command can only be run by a player")));
      }
    }
    else if(target.isPresent()) {
    		if(src.hasPermission("raspen.feed.others")) {
      Player player = (Player)target.get();
      player.offer(Keys.FOOD_LEVEL, 20);
      player.offer(Keys.SATURATION, (double) 20);
      player.sendMessage(Text.of("You're now feeded"));
      if (player != src) {
        src.sendMessage(Text.of(player.getName() + " has been feeded"));
      }
    } else {
    		src.sendMessage(Text.of(TextColors.RED,("You do not have permission to feed other players")));
    }
    } 
    return CommandResult.success();
  }
}
