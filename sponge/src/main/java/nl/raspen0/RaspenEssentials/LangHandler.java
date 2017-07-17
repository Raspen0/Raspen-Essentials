package nl.raspen0.RaspenEssentials;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LangHandler {

	private RaspenEssentials plugin;
    private Map<String, String> langlist = new HashMap<>();
	private List<String> loadedLangs;
	public int localemode = 0;

	public LangHandler(RaspenEssentials ess){
		plugin = ess;
		loadLangs();
	}
	
	private String getLang(CommandSource sender){
		if(!(sender instanceof Player)){
			return "en";
		}
		Player player = (Player) sender;
		if(localemode == 1){
			for(String s : loadedLangs){
				if(player.hasPermission("raspess.lang." + s)){
					return s;
				}
			}
		} else {
			return player.getLocale().toString().substring(0, 2);
		}
		plugin.getLogger().error(Text.builder("Could not detect language of player " + player.getName() + "!, using fallback language.").color(TextColors.RED).build().toPlain());
		return "en";
	}

	/**
	 * @param sender The receiver of the message (Is used to get the language).
	 * @param lang The language (Is used to override the language).
	 * @param messageID The ID message to get.
	 * @return The localised message.
	 */
	public Text getMessage(CommandSource sender, @Nullable String lang, String messageID){
		if(sender != null){
			//If player != null then get their language, otherwise use given lang.
			lang = getLang(sender);
		}
		Text message = null;
		try{
            message = TextSerializers.formattingCode('&').deserialize(langlist.get(lang + messageID)).toText();
        } catch (NullPointerException e){
			try{
                message = TextSerializers.formattingCode('&').deserialize(langlist.get(lang + messageID)).toText();
                plugin.getLogger().error(Text.builder("String " + messageID + " not found for lang " + lang + "!, using fallback language.").color(TextColors.RED).build().toPlain());
			} catch (NullPointerException e2){
                plugin.getLogger().error(Text.builder("String " + messageID + " not found!").color(TextColors.RED).build().toPlain());
			}
		}
		
		return message;
	}

	public Text getPlaceholderMessage(CommandSource sender, @Nullable String lang, String messageID, String[] placeholders, String[] replacements){
        if(sender != null){
            //If player != null then get their language, otherwise use given lang.
            lang = getLang(sender);
        }
        Text message = null;
        String s = langlist.get(lang + messageID);
        int i = 0;
            for(String p : placeholders){
                s = s.replace(p, replacements[i]);
                i++;
            }
        try{
            message = TextSerializers.formattingCode('&').deserialize(s).toText();
        } catch (NullPointerException e2){
            plugin.getLogger().error(Text.builder("String " + messageID + " not found!").color(TextColors.RED).build().toPlain());
        }
        return message;
    }

	public void unloadLangs(){
	    plugin.getLogger().info(Text.builder("Languages unloaded!").color(TextColors.AQUA).build().toPlain());
        langlist.clear();
        loadedLangs.clear();
    }
	
    private void loadLangs(){
    	//Load the languages listed in the config
    	loadedLangs = plugin.getConfig().langs;
    	List<String> langs = new ArrayList<>(loadedLangs);
    	//Load enabled language overrides from /RaspenEssentials/lang/ if the folder exists
        File dir = new File(plugin.dir.toFile() + "/lang/");
    	if(dir.exists()){
    		Iterator<String> iterator = langs.iterator();
    		while(iterator.hasNext()){
    			String it = iterator.next();
        		File file = new File(dir + "/lang_" + it + ".conf");
				if(!file.exists()){
					continue;
				}
                try {
                    YAMLConfigurationLoader load = YAMLConfigurationLoader.builder().setFile(file).build();
                    ConfigurationNode rootNode = load.load();
                    for(ConfigurationNode key : rootNode.getChildrenList()){
                        langlist.put(it + key, key.getValue().toString());
                        System.out.println("Override: " + it + key + ": " + key.getValue().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                plugin.getLogger().info("Loaded custom " + it +  " language!");
				//Remove lang from list so it doesn't get loaded 2 times.
				iterator.remove();
    		}
    	}
    	//Load enabled languages from jar
    	for(String s : langs){
    		try {
                URL jarConfigFile = plugin.getGame().getAssetManager().getAsset(plugin, "lang_" + s + ".yml").get().getUrl();
                YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder().setURL(jarConfigFile).build();
				ConfigurationNode rootNode = loader.load();
                Map<String, String> map = rootNode.getValue(new TypeToken<Map<String, String>>() {});
				for(String key : map.keySet()){
					langlist.put(s + key, map.get(key));
				}
				plugin.getLogger().info("Loaded " + s +  " language!");
			} catch (IOException | NullPointerException e) {
                plugin.getLogger().error(Text.builder("Language " + s + " doesn't exist!").color(TextColors.RED).build().toPlain());
			} catch (NoSuchElementException e){
                plugin.getLogger().error(Text.builder("Error loading language " + s + "!").color(TextColors.RED).build().toPlain());
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        }
    }
}
