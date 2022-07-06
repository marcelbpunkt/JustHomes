package me.kondi.JustHomes.Listeners;

import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.JustHomes;
import me.kondi.JustHomes.Teleportation.TeleportPlayer;
import me.kondi.JustHomes.Utils.ConfigManager;
import me.kondi.JustHomes.Utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Events implements Listener {
    private JustHomes plugin;
    private TeleportPlayer teleportPlayer;
    private ConfigManager cfgManager;

    private PlayerData playerData;

    public Events(JustHomes plugin) {
        this.plugin = plugin;
        this.cfgManager = plugin.cfgManager;
        this.playerData = plugin.playerData;

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        String uuid = e.getPlayer().getUniqueId().toString();
        teleportPlayer = plugin.teleportPlayer;
        if (teleportPlayer.tpCooldownTask.containsKey(uuid)) {
            teleportPlayer.tpCooldownTask.get(uuid).cancel();
            teleportPlayer.tpCooldownTask.remove(uuid);
            teleportPlayer.tpCooldown.remove(uuid);

        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> playerData.saveHome(uuid));

    }


    @EventHandler
    public void moveEvent(PlayerMoveEvent e) {
        String uuid = e.getPlayer().getUniqueId().toString();
        teleportPlayer = plugin.teleportPlayer;
        if (teleportPlayer.tpCooldownTask.containsKey(uuid)) {
            Location getFrom = e.getFrom().getBlock().getLocation();
            Location getTo = e.getTo().getBlock().getLocation();
            if (getFrom.getX() != getTo.getX() || getFrom.getZ() != getTo.getZ() || getFrom.getY() != getTo.getY()) {
                teleportPlayer.tpCooldownTask.get(uuid).cancel();
                teleportPlayer.tpCooldownTask.remove(uuid);
                teleportPlayer.tpCooldown.remove(uuid);
                e.getPlayer().sendMessage(plugin.prefix + Messages.get("TeleportationCancelled"));
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> playerData.loadPlayerData(e.getPlayer().getUniqueId().toString()));

    }


}
