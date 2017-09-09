package nl.raspen0.RaspenEssentials;

public class PlayerData {

    private final String UUID;
    private RELocation respawnLoc;

    /**
     * Contains data for the player.
     * @param UUID The UUID of the player.
     * @param respawnLoc The respawn location of the player.
     */
    public PlayerData(String UUID, RELocation respawnLoc){
        this.UUID = UUID;
        this.respawnLoc = respawnLoc;
    }

    public String getUUID() {
        return UUID;
    }

    public RELocation getRespawnLoc() {
        return respawnLoc;
    }

    public void setRespawnLoc(RELocation respawnLoc) {
        this.respawnLoc = respawnLoc;
    }
}
