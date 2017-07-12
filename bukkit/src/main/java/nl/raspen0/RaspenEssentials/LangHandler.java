package nl.raspen0.RaspenEssentials;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LangHandler {

	private RaspenEssentials plugin;
    private Map<String, String> langlist = new HashMap<>();
	private List<String> loadedLangs;
	int localemode = 0;

	public LangHandler(RaspenEssentials ess){
		plugin = ess;
		loadLangs();
	}
	
	private String getLang(CommandSender sender){
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
			return player.getLocale().substring(0, 2);
		}
		plugin.log(ChatColor.RED + "Could not detect language of player " + player.getName() + "!, using fallback language.");
		return "en";
	}
	
	public String getMessage(CommandSender sender, String lang, String messageID){
		if(sender != null){
			//If player != null then get their language, otherwise use given lang.
			lang = getLang(sender);
		}
		String message = "";
		try{
			message = ChatColor.translateAlternateColorCodes('&', langlist.get(lang + messageID));
		} catch (NullPointerException e){
			try{
				message = ChatColor.translateAlternateColorCodes('&', langlist.get("en" + messageID));
				plugin.log(ChatColor.RED + "String " + messageID + " not found for lang " + lang + "!, using fallback language.");
			} catch (NullPointerException e2){
				plugin.log(ChatColor.RED + "String " + messageID + " not found!");
			}
		}
		
		return message;
	}
	
    private void loadLangs(){
    	//Load the languages listed in the config
    	loadedLangs = plugin.getConfig().getStringList("langs");
    	List<String> langs = new ArrayList<>(loadedLangs);
    	//Load enabled language overrides from /RaspenEssentials/lang/ if the folder exists
    	File dir = new File(plugin.getDataFolder() + "/lang/");
    	if(dir.exists()){
    		Iterator<String> iterator = langs.iterator();
    		while(iterator.hasNext()){
    			String it = iterator.next();
        		File file = new File(dir + "/lang_" + it + ".yml");
				if(!file.exists()){
					continue;
				}
				FileConfiguration lang = YamlConfiguration.loadConfiguration(file);
				for(String key : lang.getKeys(false)){
					langlist.put(it + key, lang.getString(key));
				}
				plugin.log("Loaded custom " + it +  " language!");
				//Remove lang from list so it doesn't get loaded 2 times.
				iterator.remove();
    		}
    	}
    	//Load enabled languages from jar
    	for(String s : langs){
    		try {
        		YamlConfiguration defaults = new YamlConfiguration();
				defaults.load(new InputStreamReader(plugin.getResource("lang/lang_" + s + ".yml")));
				for(String key : defaults.getKeys(false)){
					langlist.put(s + key, defaults.getString(key));
				}
				plugin.log("Loaded " + s +  " language!");
			} catch (IOException | NullPointerException e) {
				plugin.log(ChatColor.RED + "Language " + s + " doesn't exist!");
			} catch (InvalidConfigurationException e) {
				plugin.log(ChatColor.RED + "Error loading language " + s + "!");
			}
    	}
    }
}
