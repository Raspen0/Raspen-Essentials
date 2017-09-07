package nl.raspen0.RaspenEssentials;

import nl.raspen0.RaspenEssentials.handlers.ConfigHandler;
import nl.raspen0.RaspenEssentials.handlers.PlayerDataHandler;
import nl.raspen0.RaspenEssentials.handlers.SpawnHandler;

public class ClassManager {

	public ClassManager(RaspenEssentials plugin){
		config = new ConfigHandler(plugin);
	}

	public void serverStart(RaspenEssentials plugin){
		lang = new LangHandler(plugin);
		spawn = new SpawnHandler(plugin);
		playerdata = new PlayerDataHandler(plugin);
	}

	private ConfigHandler config;
	private LangHandler lang;
	private SpawnHandler spawn;
	private PlayerDataHandler playerdata;
	
	public LangHandler getLangHandler(){
		return lang;
	}

    public SpawnHandler getSpawnHandler() {
        return spawn;
    }

	public PlayerDataHandler getPlayerDataHandler() {
		return playerdata;
	}

	public ConfigHandler getConfigHandler() {
		return config;
	}
}
