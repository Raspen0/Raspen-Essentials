package nl.raspen0.RaspenEssentials.Sponge;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class CommandTP implements CommandExecutor{
	

	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String arg1 = args.<String> getOne("arg1").get();
		Optional<String> arg2 = args.<String> getOne("arg2");
		Optional<String> arg3 = args.<String> getOne("arg3");
		Optional<String> arg4 = args.<String> getOne("arg4");
		
		//If there's 1 argument
		if(!arg2.isPresent()){
			//tp src to player
			if(Sponge.getServer().getPlayer(arg1).isPresent()){
				Player srcplayer = (Player) src;
				Optional<Player> player = Sponge.getServer().getPlayer(arg1);
		    	Location<World> targetloc = player.get().getLocation();
		    	srcplayer.setLocation(targetloc);
			} else {
				src.sendMessage(Text.of(TextColors.RED, "Can't find player " + arg1));
			}
		}
		//If there's 2 arguments
		else if (!arg3.isPresent()){
			//tp to world
			if(arg1.equalsIgnoreCase("-w")){
				if(src.hasPermission("raspen.tp.world.self")){
				Player srcplayer = (Player) src;
				Optional<World> world = Sponge.getServer().getWorld(arg2.get());
				srcplayer.transferToWorld(arg2.get(), world.get().getSpawnLocation().getPosition());
				}
			} else if(Sponge.getServer().getPlayer(arg1).isPresent()){ 
				//tp player1 to player2
				if(src.hasPermission("raspen.tp.others")){
				if(Sponge.getServer().getPlayer(arg2.get()).isPresent()){ 
				Optional<Player> player = Sponge.getServer().getPlayer(arg1);
		    	Optional<Player> player2 = Sponge.getServer().getPlayer(arg2.get());
		    	Location<World> targetloc = player2.get().getLocation();
		    	player.get().setLocation(targetloc);
			} else {
				src.sendMessage(Text.of(TextColors.RED, "Can't find player " + arg2.get()));
			}
			} else { 
				src.sendMessage(Text.of(TextColors.RED, "Can't find player " + arg1));
			}
		}
		}
		//If there's 3 arguments
		else if (!arg4.isPresent()){
			//tp other player to world
			if (arg2.get().equalsIgnoreCase("-w")){
				if(src.hasPermission("raspen.tp.world.others")){
				if(Sponge.getServer().getPlayer(arg1).isPresent()){ 
					Optional<Player> player = Sponge.getServer().getPlayer(arg1);
					Optional<World> world = Sponge.getServer().getWorld(arg3.get());
					player.get().transferToWorld(arg3.get(), world.get().getSpawnLocation().getPosition());
				}
			}
			}
			else if(isInt(arg1) && isInt(arg2.get()) && isInt(arg3.get())){
				//tp src to location
				if(src.hasPermission("raspen.tp.location.self")){
				Player srcplayer = (Player) src;
				srcplayer.setLocation(new Location<>(srcplayer.getWorld(), Integer.parseInt(arg1),
						Integer.parseInt(arg2.get()), Integer.parseInt(arg3.get())));
			}
		}
		else if (arg4.isPresent()){ 
			//tp player to location
			if(Sponge.getServer().getPlayer(arg1).isPresent()){ 
				if(src.hasPermission("raspen.tp.location.others")){
				Optional<Player> player = Sponge.getServer().getPlayer(arg1);
				if(isInt(arg2.get()) && isInt(arg3.get()) && isInt(arg4.get())){
					player.get().setLocation(new Location<>(player.get().getWorld(), Integer.parseInt(arg2.get()),
							Integer.parseInt(arg3.get()), Integer.parseInt(arg4.get())));
				}
				}
			}
		}
		}

	
	    return CommandResult.success();
	}
	
	public static boolean isInt(String s) {
	    try {
	        Integer.parseInt(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}

}
