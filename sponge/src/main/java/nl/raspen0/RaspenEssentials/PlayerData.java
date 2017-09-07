package nl.raspen0.RaspenEssentials;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.world.World;

public class PlayerData {

    private final String UUID;
    private Vector3d respawnLoc;
    private Vector3d respawnRotation;
    private World respawnWorld;

    public PlayerData(String UUID, Vector3d respawnLoc, Vector3d respawnRotation, World respawnWorld){
        this.UUID = UUID;
        this.respawnLoc = respawnLoc;
        this.respawnRotation = respawnRotation;
        this.respawnWorld = respawnWorld;
    }

    public String getUUID() {
        return UUID;
    }

    public Vector3d getRespawnLoc() {
        return respawnLoc;
    }

    public Vector3d getRespawnRotation() {
        return respawnRotation;
    }

    public World getRespawnWorld() {
        return respawnWorld;
    }

    public void setRespawnLoc(Vector3d respawnLoc) {
        this.respawnLoc = respawnLoc;
    }

    public void setRespawnRotation(Vector3d respawnRotation) {
        this.respawnRotation = respawnRotation;
    }

    public void setRespawnWorld(World respawnWorld) {
        this.respawnWorld = respawnWorld;
    }
}
