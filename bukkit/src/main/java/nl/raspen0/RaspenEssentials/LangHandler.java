package nl.raspen0.RaspenEssentials;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LangHandler {

	RaspenEssentials plugin;
	int localemode = 0;
	
	public LangHandler(RaspenEssentials ess){
		plugin = ess;
	}
	
	public String getLang(Player player){
		if(localemode == 1){
			for(String s : plugin.loadedLangs){
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
	
	public String getMessage(Player player, String lang, String messageID){
		if(player != null){
			//If player != null then get their language, otherwise use given lang.
			lang = getLang(player);
		}
		String message = "";
		try{
			message = ChatColor.translateAlternateColorCodes('&', plugin.langlist.get(lang + messageID));
		} catch (NullPointerException e){
			try{
				message = ChatColor.translateAlternateColorCodes('&', plugin.langlist.get("en" + messageID));
				plugin.log(ChatColor.RED + "String " + messageID + " not found for lang " + lang + "!, using fallback language.");
			} catch (NullPointerException e2){
				plugin.log(ChatColor.RED + "String " + messageID + " not found!");
			}
		}
		
		return message;
	}
}
