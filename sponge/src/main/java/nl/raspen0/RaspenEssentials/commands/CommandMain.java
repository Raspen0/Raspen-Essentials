package nl.raspen0.RaspenEssentials.commands;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import nl.raspen0.RaspenEssentials.RaspenEssentials;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandMain implements CommandCallable {

    private RaspenEssentials plugin;

    public CommandMain(RaspenEssentials ess){
        plugin = ess;
    }

    @Override
    //Command
    public CommandResult process(CommandSource src, String arguments) throws CommandException {
        if (arguments.isEmpty()) {
            src.sendMessage(Text.builder("RaspenEssentials, developed by ").color(TextColors.AQUA).append(Text.builder("raspen0")
                    .color(TextColors.YELLOW).append(Text.builder(".").color(TextColors.AQUA).build()).build()).build());
            src.sendMessage(Text.builder("Version: ").color(TextColors.AQUA).append(Text.builder(plugin.version).color(TextColors.YELLOW).build()).build());
            return CommandResult.success();
        }
        String[] args = arguments.split(" ");
        if (args[0].equalsIgnoreCase("reload")) {
            if (!src.hasPermission("raspess.reload")) {
                src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "noPerm")));
                return CommandResult.success();
            }
            plugin.getManager().getLangHandler().unloadLangs();
            try {
                plugin.getManager().getConfigHandler().reloadConfig();
            } catch (IOException | ObjectMappingException e) {
                e.printStackTrace();
            }
            plugin.getManager().getLangHandler().loadLangs();
            if (plugin.getManager().getConfigHandler().getConfig().localeDetectMode.equals("permission")) {
                plugin.getManager().getLangHandler().localemode = 1;
            } else {
                plugin.getManager().getLangHandler().localemode = 0;
            }
            src.sendMessage(Text.of(plugin.getManager().getLangHandler().getMessage(src, null, "reload")));
        }
        return CommandResult.success();
    }

    @Override
    //Tab Complete
    public List<String> getSuggestions(CommandSource src, String args, @Nullable Location<World> location) throws CommandException {
        List<String> list = new ArrayList<>();
        if (args.length() == 1) {
            list.add("reload");
        }
        return list;
    }

    @Override
    public boolean testPermission(CommandSource src) {
        return false;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Text> desc = Optional.of(Text.of("/raspenessentials (reload)"));

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
        return Text.of("/raspenessentials (reload)");
    }
}
