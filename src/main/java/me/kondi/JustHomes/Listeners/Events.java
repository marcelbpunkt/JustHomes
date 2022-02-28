package me.kondi.JustHomes.Listeners;

import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.JustHomes;
import me.kondi.JustHomes.Teleportation.TeleportPlayer;
import me.kondi.JustHomes.Utils.ConfigManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class Events implements Listener {
    private JustHomes plugin;
    private TeleportPlayer teleportPlayer;
    private ConfigManager cfgManager;
    private HashMap<String, String> messages = new HashMap<>();
    private PlayerData playerData;

    public Events(JustHomes plugin) {
        this.plugin = plugin;
        this.cfgManager = plugin.cfgManager;
        this.messages = plugin.messages;
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
        playerData.saveHome(uuid);

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
                e.getPlayer().sendMessage(plugin.prefix + messages.get("TeleportationCancelled"));
            }

        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        playerData.loadPlayerData(e.getPlayer().getUniqueId().toString());
    }


}
