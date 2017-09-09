package nl.raspen0.RaspenEssentials.commands;

import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandHeal implements CommandCallable {

    private RaspenEssentials plugin;

    public CommandHeal(RaspenEssentials ess){
        plugin = ess;
    }

    @Override
    //Command
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        if(!src.hasPermission("raspess.heal")){
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
            return CommandResult.success();
        }
        if (arguments.isEmpty()) {
            if (!(src instanceof Player)) {
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "onlyPlayer")));
                return CommandResult.success();
            }
            Player player = (Player) src;
            player.offer(Keys.HEALTH, player.get(Keys.MAX_HEALTH).get());
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "healed")));
            return CommandResult.success();
        }
        if(!src.hasPermission("raspess.heal.others")) {
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
            return CommandResult.success();
        }
        String[] args = arguments.isEmpty() ? new String[]{} : arguments.split(" ");

        Player player = null;
        for(Player p : plugin.getGame().getServer().getOnlinePlayers()){
            if(p.getName().toLowerCase().contains(args[0].toLowerCase())){
                player = p;
            }
        }
        if(player == null){
            //src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "notOnline").replace("%player", args[0])));
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "notOnline", new String[]{"%player"}, new String[] {args[0]})));
            return CommandResult.success();
        }

        player.offer(Keys.HEALTH, player.get(Keys.MAX_HEALTH).get());
        src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "healed")));
        if (player != src) {
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "healedOther", new String[]{"%player"}, new String[] {args[0]})));
        }
        return CommandResult.success();
    }

    @Override
    //Tab Complete
    public List<String> getSuggestions(CommandSource src, String args, @Nullable Location<World> location) throws CommandException {
        List<String> list = new ArrayList<>();
        if (args.isEmpty()) {
            for (Player p : plugin.getGame().getServer().getOnlinePlayers()) {
                list.add(p.getName());
            }
        }
        return list;
    }

        @Override
        public boolean testPermission(CommandSource src) {
            return false;
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        private final Optional<Text> desc = Optional.of(Text.of("/heal <player>"));

        @Override
        public Optional<Text> getShortDescription(CommandSource src) {
            return desc;
        }

        @Override
        //Hover
        public Optional<Text> getHelp(CommandSource src) {
            return desc;
        }

        @Override
        public Text getUsage(CommandSource src) {
            return Text.of("/heal (player)");
        }
    }
