package me.kondi.justhomes.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.SpawnChangeEvent;

import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Data.PlayerData;
import me.kondi.justhomes.Home.Home;
import me.kondi.justhomes.Teleportation.TeleportPlayer;
import me.kondi.justhomes.Utils.Messages;

/**
 * Handles all events that are needed for this plugin.
 * 
 * @author Kondee3, marcelbpunkt
 *
 */
public class Events implements Listener {
	private JustHomesPlugin plugin;
	// private TeleportPlayer teleportPlayer;
	// private ConfigManager cfgManager;

	private PlayerData playerData;

	/**
	 * Initializes the event listener.
	 * 
	 * @param plugin contains all plugin data
	 */
	public Events(JustHomesPlugin plugin) {
		this.plugin = plugin;
		// this.cfgManager = plugin.cfgManager;
		this.playerData = plugin.playerData;

	}

	/**
	 * Loads the player's homes when they join.
	 * 
	 * @param e the player join event providing the player's UUID
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin,
				() -> playerData.loadPlayerData(e.getPlayer().getUniqueId().toString()));
	}

	/**
	 * Cancels a teleport (if any) when the player leaves the server.
	 * 
	 * @param e the player quit event
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		// teleportPlayer = plugin.teleportPlayer;
		if (TeleportPlayer.tpCooldownTask.containsKey(uuid)) {
			TeleportPlayer.tpCooldownTask.get(uuid).cancel();
			TeleportPlayer.tpCooldownTask.remove(uuid);
			TeleportPlayer.tpCooldown.remove(uuid);

		}
		Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> playerData.saveHomes(uuid));

	}

	/**
	 * Changes the worldspawn home to the new world's general spawnpoint.
	 * 
	 * @param e the changed world event
	 */
	@EventHandler
	public void onChangedWorld(PlayerChangedWorldEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		Location newSpawnLoc = e.getPlayer().getWorld().getSpawnLocation();
		// TODO remove if possible
	}

	/**
	 * 
	 * @param e
	 */
	@EventHandler
	public void onWorldSpawnChange(SpawnChangeEvent e) {
		// TODO remove if possible
	}

	/**
	 * Cancels teleportation if the player moves during teleport cooldown, except
	 * when the X, Y and Z coordinate stay the same and just the view angle has
	 * changed.
	 * 
	 * @param e the player move event
	 */
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		String uuid = e.getPlayer().getUniqueId().toString();
		// teleportPlayer = plugin.teleportPlayer;
		if (TeleportPlayer.tpCooldownTask.containsKey(uuid)) {
			Location getFrom = e.getFrom().getBlock().getLocation();
			Location getTo = e.getTo().getBlock().getLocation();
			if (getFrom.getX() != getTo.getX() || getFrom.getZ() != getTo.getZ() || getFrom.getY() != getTo.getY()) {
				TeleportPlayer.tpCooldownTask.get(uuid).cancel();
				TeleportPlayer.tpCooldownTask.remove(uuid);
				TeleportPlayer.tpCooldown.remove(uuid);
				e.getPlayer().sendMessage(plugin.prefix + Messages.get("TeleportationCancelled"));
			}

		}
	}

	/**
	 * Sets the "bed" home location when the player interacts with a bed or a
	 * respawn anchor. If the player interacts with a bed outside the overworld, or
	 * with a respawn anchor outside the Nether, nothing happens. TODO remove if
	 * possible
	 * 
	 * @param e the player interact event
	 */
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		BlockData blockData = e.getClickedBlock().getBlockData();
		if (blockData instanceof Bed) {
			// player right-clicked on a bed
			onInteractWithBed(e, (Bed) blockData);
		} else if (blockData instanceof RespawnAnchor) {
			// player right-clicked on a respawn anchor
			onInteractWithRespawnAnchor(e, (RespawnAnchor) blockData);
		}
		// player clicked on something else which we don't care about
	}

	/**
	 * Sets the home "bed" to the location of the bed if the bed is located in the
	 * Overworld. TODO remove if possible
	 * 
	 * @param e   the player interact event
	 * @param bed the bed on which the player right-clicked
	 */
	private void onInteractWithBed(PlayerInteractEvent e, Bed bed) {
		if (!e.getClickedBlock().getWorld().isBedWorks()) {
			// bed goes BOOM
			return;
		}

		Player p = e.getPlayer();
		String uuid = p.getUniqueId().toString();
		// on respawn anchors too)
		Location loc = p.getBedSpawnLocation();
		if (loc == null) {
			p.sendMessage("Could not find out spawn location. Please contact MarcelBPunkt so he can fix it :P");
			return;
		}
		String homeName = Messages.get("Bed");
		String worldName = e.getClickedBlock().getWorld().getName();
		Home home = new Home(uuid, homeName, worldName, loc.getX(), loc.getY() + 1, loc.getZ(), 0.0f, 0.0f);
		playerData.addHome(home);
	}

	// TODO remove if possible
	private void onInteractWithRespawnAnchor(PlayerInteractEvent e, RespawnAnchor respawnAnchor) {
		if (!e.getClickedBlock().getWorld().isRespawnAnchorWorks()) {
			// respawn anchor goes BOOM
			return;
		}
	}
}
