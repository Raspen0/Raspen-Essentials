package nl.raspen0.RaspenEssentials.Sponge;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CommandGamemode implements CommandExecutor{

	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
		String gm = ctx.<String> getOne("mode").get();
	    Optional<Player> target = ctx.getOne("player");
	    
	    if(!target.isPresent()){
	    	if(src instanceof Player){
	    		Player player = (Player) src;
	    		GamemodeChange(player, gm);
	    	}
	    	if(src instanceof ConsoleSource || (src instanceof CommandBlockSource)){
	    		src.sendMessage(Text.of(TextColors.RED, "You must specify which player you wish to preform this action on."));
	    	}
	    }
	    else if(target.isPresent()) {
	    	if(src.hasPermission("raspen.gamemode.others")){
	    		Player player = target.get();
	    		GamemodeChange(player, gm);
	    	}
	    }
	    	
	    return CommandResult.success();
	}
	
	public void GamemodeChange(Player target, String mode){
		ConsoleSource console = Sponge.getGame().getServer().getConsole();
		if(mode.equals("0")||(mode.equalsIgnoreCase("survival")||(mode.equalsIgnoreCase("s")))){
			target.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
			target.sendMessage(Text.of("Gamemode changed to SURVIVAL"));
			console.sendMessage(Text.of(target.getName() + "'s gamemode changed to SURVIVAL"));
		}
		else if(mode.equals("1")||(mode.equalsIgnoreCase("creative")||(mode.equalsIgnoreCase("c")))){
			target.offer(Keys.GAME_MODE, GameModes.CREATIVE);
			target.sendMessage(Text.of("Gamemode changed to CREATIVE"));
			console.sendMessage(Text.of(target.getName() + "'s gamemode changed to CREATIVE"));
		}
		else if(mode.equals("2")||(mode.equalsIgnoreCase("adventure")||(mode.equalsIgnoreCase("a")))){
			target.offer(Keys.GAME_MODE, GameModes.ADVENTURE);
			target.sendMessage(Text.of("Gamemode changed to ADVENTURE"));
			console.sendMessage(Text.of(target.getName() + "'s gamemode changed to ADVENTURE"));
		}
		else if(mode.equals("3")||(mode.equalsIgnoreCase("spectator")||(mode.equalsIgnoreCase("sp")))){
			target.offer(Keys.GAME_MODE, GameModes.SPECTATOR);
			target.sendMessage(Text.of("Gamemode changed to SPECTATOR"));
			console.sendMessage(Text.of(target.getName() + "'s gamemode changed to SPECTATOR"));
		}
		else 
			target.sendMessage(Text.of(TextColors.RED, "This isn't a valid gamemode"));
		
	}

}
