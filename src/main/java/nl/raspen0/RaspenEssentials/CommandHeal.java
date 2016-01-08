package nl.raspen0.RaspenEssentials;

import java.util.Optional;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class CommandHeal
  implements CommandExecutor
{
  public CommandResult execute(CommandSource src, CommandContext ctx)
    throws CommandException
  {
    Optional<Player> target = ctx.getOne("player");
    if (!target.isPresent())
    {
      if ((src instanceof Player))
      {
        Player player = (Player)src;
        player.offer(Keys.HEALTH, (Double)player.get(Keys.MAX_HEALTH).get());
        player.sendMessage(Text.of("You've been healed"));
      }
    }
    else if ((target.isPresent()) && (src.hasPermission("raspen.heal.others")))
    {
      Player player = (Player)target.get();
      player.offer(Keys.HEALTH, (Double)player.get(Keys.MAX_HEALTH).get());
      player.sendMessage(Text.of("You've been healed"));
      if (player != src) {
        src.sendMessage(Text.of(player.getName() + " has been healed"));
      }
    }
    return CommandResult.success();
  }
}
