package me.kondi.justhomes.Commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Data.PlayerData;
import me.kondi.justhomes.Home.Home;
import me.kondi.justhomes.Home.HomeNames;
import me.kondi.justhomes.Permissions.PermissionChecker;
import me.kondi.justhomes.Utils.Messages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SetHomeCommand {

	private JustHomesPlugin plugin;
	private String prefix;

	private PlayerData playerData;
	private List<Material> damageBlocks = Arrays.asList(Material.CACTUS, Material.FIRE, Material.CAMPFIRE,
			Material.SOUL_FIRE, Material.SOUL_CAMPFIRE, Material.MAGMA_BLOCK, Material.SWEET_BERRY_BUSH,
			Material.WITHER_ROSE, Material.LAVA, Material.POWDER_SNOW);

	public SetHomeCommand(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.prefix = plugin.prefix;

		this.playerData = plugin.playerData;
	}

	public SetHomeCommand() {
	}

	/**
	 * Creates a new or edits an existing home. This method is called when a player
	 * enters {@code /sethome <homename>}.
	 * 
	 * @param p    the player that entered the command
	 * @param args the arguments of the command
	 */
	public void executeSethomeCommand(Player p, String[] args) {
		executeSethomeCommand(p, args, false);
	}

	/**
	 * Creates a new or edits an existing home. This method is only called directly
	 * when one of the spawn homes is created or edited.
	 * 
	 * @param p       the player whose spawn point changed
	 * @param args    the arguments of the command
	 * @param isSpawn true if and only if the home is a spawn point
	 */
	public void executeSethomeCommand(Player p, String[] args, boolean isSpawn) {

		String uuid = p.getUniqueId().toString();

		if (args.length == 0) {
			p.sendMessage(prefix + Messages.get("SpecifyHomeNameException"));
			return;
		}
		String homeName = args[0];

		// isSpawn is always false when a player enters a /sethome command
		if ((homeName.equalsIgnoreCase(Messages.get("WorldSpawn")) || homeName.equalsIgnoreCase(Messages.get("Bed")))
				&& !isSpawn) {
			p.sendMessage(prefix + Messages.get("ManualModifySpawnHomeException"));
			return;
		}

		if (plugin.simpleProtection) {
			Material middle = p.getLocation().getBlock().getType();
			Material below = p.getWorld().getBlockAt(p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1,
					p.getLocation().getBlockZ()).getType();
			if (damageBlocks.contains(below) || damageBlocks.contains(middle) || middle == Material.NETHER_PORTAL) {
				p.sendMessage(
						prefix + Messages.get(isSpawn ? "SpawnOnlyOnGroundException" : "SetOnlyOnGroundException"));
				return;
			}
		}

		List<Home> playerHomes = playerData.listOfHomes(uuid);

		if (playerHomes.size() == 0) {
			saveLoc(p, homeName);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent
					.fromLegacyText(prefix + PlaceholderAPI.setPlaceholders(p, Messages.get("CreatedHome"))));
			return;
		}

		Home home = playerData.getHome(uuid, homeName);
		if (home != null) {
			replaceLoc(p, home);
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent
					.fromLegacyText(prefix + PlaceholderAPI.setPlaceholders(p, Messages.get("EditedHome"))));
			return;
		}
		if (playerData.countPlayerHomes(uuid) >= PermissionChecker.checkHomesMaxAmount(p)) {
			p.sendMessage(prefix + Messages.get("TooMuchHomesException"));
			return;
		}

		saveLoc(p, homeName);
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
				TextComponent.fromLegacyText(prefix + PlaceholderAPI.setPlaceholders(p, Messages.get("CreatedHome"))));
	}

	public void saveLoc(Player p, String homeName) {
		String uuid = p.getUniqueId().toString();
		HomeNames.addHomeName(uuid, homeName);
		Home home = new Home(uuid, homeName, p.getLocation());
		playerData.addHome(home);
	}

	public void replaceLoc(Player p, Home home) {
		String uuid = p.getUniqueId().toString();
		HomeNames.addHomeName(uuid, home.getHomeName());
		Home newHome = new Home(uuid, home.getHomeName(), p.getLocation());
		playerData.replaceHome(home, newHome);
	}

}
