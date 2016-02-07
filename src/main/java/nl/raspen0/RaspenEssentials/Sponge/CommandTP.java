package nl.raspen0.RaspenEssentials.Sponge;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class CommandTP implements CommandExecutor{
	

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Optional<String> arg1 = args.<String> getOne("arg1");
		Optional<String> arg2 = args.<String> getOne("arg2");
	    
	    if(!arg2.isPresent()){
	    	Player srcplayer = (Player) src;
	    	Optional<Player> player = Sponge.getServer().getPlayer(arg1.get());
	    	Location<World> targetloc = player.get().getLocation();
	    	srcplayer.setLocation(targetloc);
	    }
	    
	    else if(arg2.isPresent()){
	    	Optional<Player> player = Sponge.getServer().getPlayer(arg1.get());
	    	Optional<Player> player2 = Sponge.getServer().getPlayer(arg2.get());
	    	Location<World> targetloc = player2.get().getLocation();
	    	
	    	player.get().setLocation(targetloc);
	    }
		
	    return CommandResult.success();
	}

}
