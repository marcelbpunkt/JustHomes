package me.kondi.JustHomes.Home;

public class Home {

    private String owner;
    private String homeName;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;


    public Home(String owner, String homeName, String worldName, double x, double y, double z, float pitch, float yaw) {
        this.owner = owner;
        this.homeName = homeName;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }
    public Home(String homeName, String worldName, double x, double y, double z) {
        this.homeName = homeName;
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
    }



    public String getOwner() {
        return owner;
    }


    public String getHomeName() {
        return homeName;
    }


    public String getWorldName() {
        return worldName;
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


    public float getPitch() {
        return pitch;
    }


    public float getYaw() {
        return yaw;
    }


}
