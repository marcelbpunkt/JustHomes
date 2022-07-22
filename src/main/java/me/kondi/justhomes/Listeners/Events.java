package me.kondi.justhomes.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Data.PlayerData;
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
}
