package me.kondi.justhomes.Commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Data.PlayerData;
import me.kondi.justhomes.Home.Home;
import me.kondi.justhomes.Permissions.PermissionChecker;
import me.kondi.justhomes.Utils.Messages;

public class SpawnCommand {

	private final JustHomesPlugin plugin;
	private final String prefix;
	private PlayerData playerData;
	private Material[] damageBlocksMaterials = { Material.CACTUS, Material.FIRE, Material.CAMPFIRE, Material.SOUL_FIRE,
			Material.SOUL_CAMPFIRE, Material.MAGMA_BLOCK, Material.SWEET_BERRY_BUSH, Material.WITHER_ROSE,
			Material.LAVA, Material.POWDER_SNOW };
	private List<Material> damageBlocks = Arrays.asList(damageBlocksMaterials);
	private int teleportationDelay;

	public SpawnCommand(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.prefix = plugin.prefix;

		this.playerData = plugin.playerData;
		this.teleportationDelay = plugin.teleportationDelay;
	}

	public void executeSpawnCommand(Player p) {

		String uuid = p.getUniqueId().toString();
		Home home = playerData.getHome(uuid, Messages.get("WorldSpawn"));
		List<Home> homeList = playerData.listOfHomes(uuid);

		// no checks necessary since the world spawn home is created each time a player
		// joins a world
		Location loc = new Location(Bukkit.getWorld(home.getWorldName()), home.getX(), home.getY(), home.getZ(),
				home.getYaw(), home.getPitch());

		if (plugin.simpleProtection) {
			Material middle = loc.getBlock().getType();
			Material below = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() - 1, loc.getBlockZ()).getType();
			if (damageBlocks.contains(below) || damageBlocks.contains(middle)) {
				p.sendMessage(prefix + Messages.get("CorruptedHome"));
				return;
			}
		}

		plugin.teleportPlayer.teleportPlayer(p, loc, PermissionChecker.checkDelay(p));

	}

}
