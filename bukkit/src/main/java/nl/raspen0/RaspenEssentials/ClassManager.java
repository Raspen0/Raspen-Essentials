package nl.raspen0.RaspenEssentials;

import nl.raspen0.RaspenEssentials.handlers.PlayerDataHandler;
import nl.raspen0.RaspenEssentials.handlers.SpawnHandler;

public class ClassManager {

	public ClassManager(RaspenEssentials plugin){
		lang = new LangHandler(plugin);
		playerdata = new PlayerDataHandler(plugin);
		spawn = new SpawnHandler(plugin);
	}

	private LangHandler lang;
	private PlayerDataHandler playerdata;
	private SpawnHandler spawn;
	
	public LangHandler getLangHandler(){
		return lang;
	}

    public PlayerDataHandler getPlayerDataHandler() {
        return playerdata;
    }

    public SpawnHandler getSpawnHandler() {
        return spawn;
    }
}
