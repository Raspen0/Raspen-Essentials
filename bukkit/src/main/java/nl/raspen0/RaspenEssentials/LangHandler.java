package nl.raspen0.RaspenEssentials;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LangHandler {

	RaspenEssentials plugin;
	
	public LangHandler(RaspenEssentials ess){
		plugin = ess;
	}
	
	public String getLang(Player player){
		return player.getLocale().substring(0, 2);
	}
	
	public String getMessage(Player player, String lang, String messageID){
		if(player != null){
			//If player != null then get their language, otherwise use given lang.
			lang = getLang(player);
		}
		String message = "";
		try{
			message = plugin.langlist.get(lang + messageID);
		} catch (NullPointerException e){
			try{
				message = plugin.langlist.get("en" + messageID);
				plugin.getLogger().severe(ChatColor.RED + "String " + messageID + " not found for lang " + lang + "!, using fallback language.");
			} catch (NullPointerException e2){
				plugin.getLogger().severe(ChatColor.RED + "String " + messageID + " not found!");
			}
		}
		
		return message;
	}
}
