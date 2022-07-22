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
import me.kondi.justhomes.Home.HomeNames;
import me.kondi.justhomes.Permissions.PermissionChecker;
import me.kondi.justhomes.Utils.Messages;

public class HomeCommand {

	private final JustHomesPlugin plugin;
	private final String prefix;
	private PlayerData playerData;
	private Material[] damageBlocksMaterials = { Material.CACTUS, Material.FIRE, Material.CAMPFIRE, Material.SOUL_FIRE,
			Material.SOUL_CAMPFIRE, Material.MAGMA_BLOCK, Material.SWEET_BERRY_BUSH, Material.WITHER_ROSE,
			Material.LAVA, Material.POWDER_SNOW };
	private List<Material> damageBlocks = Arrays.asList(damageBlocksMaterials);
	private int teleportationDelay;

	public HomeCommand(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.prefix = plugin.prefix;

		this.playerData = plugin.playerData;
		this.teleportationDelay = plugin.teleportationDelay;
	}

	public void executeHomeCommand(Player p, String[] args) {

		String uuid = p.getUniqueId().toString();
		if (playerData.countPlayerHomes(uuid) == 0) {
			p.sendMessage(prefix + Messages.get("UserHasNoHomes"));
			return;
		}

		if (args.length == 0) {
			p.sendMessage(prefix + Messages.get("SpecifyHomeNameException"));
			return;
		}

		String homeName = args[0];
		Home home = null;
		if (homeName.equalsIgnoreCase(Messages.get("WorldSpawn"))) {
			Location location = p.getWorld().getSpawnLocation();
			home = new Home(uuid, homeName, location);
		} else if (homeName.equalsIgnoreCase(Messages.get("Bed"))) {
			Location location = p.getBedSpawnLocation();
			if (location == null) {
				p.sendMessage(prefix + Messages.get("PlayerSpawnNotFound"));
				return;
			}
			home = new Home(uuid, homeName, location);
		} else {
			home = playerData.getHome(uuid, homeName);
		}

		// List<Home> homeList = playerData.listOfHomes(uuid);

		if (home == null) {
			p.sendMessage(prefix + Messages.get("UnknownHomeName"));
			return;
		}

		// This doesn't even seem to be possible. Let's try it without this check.
		// if (homeList.indexOf(home) >= PermissionChecker.checkHomesMaxAmount(p)) {
		// p.sendMessage(prefix + Messages.get("UnavailableHome"));
		// return;
		// }

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

		HomeNames.addHomeName(uuid, homeName);
		plugin.teleportPlayer.teleportPlayer(p, loc, PermissionChecker.checkDelay(p));

	}

}
