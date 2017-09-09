package nl.raspen0.RaspenEssentials;


import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

/**
 * Contains World, Location and Rotation.
 */
public class RELocation {

    private String world;
    private double x;
    private double y;
    private double z;
    private double pitch;
    private double yaw;

    public RELocation(String world, double x, double y, double z, double pitch, double yaw){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public RELocation(String world, Vector3i loc, Vector3d rotation){
        this.world = world;
        this.x = loc.getX();
        this.y = loc.getY();
        this.z = loc.getZ();
        this.pitch = rotation.getX();
        this.yaw = rotation.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getPitch() {
        return pitch;
    }

    public double getYaw() {
        return yaw;
    }

    public String getWorld() {
        return world;
    }
}
