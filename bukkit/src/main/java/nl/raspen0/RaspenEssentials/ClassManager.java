package nl.raspen0.RaspenEssentials;

public class ClassManager {
	
	RaspenEssentials plugin;
	
	public ClassManager(RaspenEssentials ess){
		plugin = ess;
		lang = new LangHandler(plugin);
	}

	private LangHandler lang;
	
	public LangHandler getLangHandler(){
		return lang;
	}
}
