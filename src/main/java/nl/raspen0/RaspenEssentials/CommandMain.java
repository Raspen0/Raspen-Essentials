package nl.raspen0.RaspenEssentials;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CommandMain implements CommandExecutor{

	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
		
	    Optional<String> target = ctx.<String> getOne("argument");
		
		if(!target.isPresent()){
			//Todo
		}
		
		else if(target.isPresent()){
			String targetString = target.get();
			if(targetString.equals("version")){
				src.sendMessage(Text.of(RaspenEssentials.name + " " + RaspenEssentials.version));
			}
			else {
	    		src.sendMessage(Text.of(TextColors.RED,("/raspenessentials [version/reload]")));
			}
		}
		return CommandResult.success();
	}

}
