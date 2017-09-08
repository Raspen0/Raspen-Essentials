package nl.raspen0.RaspenEssentials;


import org.bukkit.Location;

public class PlayerData {

    private final String UUID;
    private Location respawnLoc;

    public PlayerData(String UUID, Location respawnLoc){
        this.UUID = UUID;
        this.respawnLoc = respawnLoc;
    }

    public String getUUID() {
        return UUID;
    }

    public Location getRespawnLoc() {
        return respawnLoc;
    }

    public void setRespawnLoc(Location respawnLoc) {
        this.respawnLoc = respawnLoc;
    }
}
