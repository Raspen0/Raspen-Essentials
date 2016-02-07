package nl.raspen0.RaspenEssentials.Sponge;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class CommandFly implements CommandExecutor{

	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
		
		if(src instanceof Player){
			Player player = (Player) src;
			if(player.get(Keys.CAN_FLY).isPresent()){
				boolean canFly = player.get(Keys.CAN_FLY).get();
				
				if(canFly){
					player.offer(Keys.IS_FLYING, false);
					player.offer(Keys.CAN_FLY, false);
					src.sendMessage(Text.of("Flying disabled"));
				}
				else if(!canFly){
					player.offer(Keys.CAN_FLY, true);
					src.sendMessage(Text.of("Flying enabled"));
				}
			}
		}
		return CommandResult.success();
	}

}
