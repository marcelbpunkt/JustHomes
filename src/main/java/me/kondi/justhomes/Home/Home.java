package me.kondi.justhomes.Home;

import org.bukkit.Location;

public class Home {

	private String owner;
	private String homeName;
	private String worldName;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;

	public Home(String homeName, Location location) {
		this(null, homeName, location);
	}

	public Home(String owner, String homeName, Location location) {
		this(owner, homeName, location.getWorld().getName(), location.getX(), location.getY(), location.getZ(),
				location.getPitch(), location.getYaw());
	}

	public Home(String homeName, String worldName, double x, double y, double z) {
		this(null, homeName, worldName, x, y, z, 0.0f, 0.0f);
	}

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
