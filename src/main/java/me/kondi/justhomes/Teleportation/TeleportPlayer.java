package me.kondi.justhomes.Teleportation;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.clip.placeholderapi.PlaceholderAPI;
import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Utils.Messages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class TeleportPlayer {
	public static HashMap<String, Integer> tpCooldown = new HashMap<>();
	public static HashMap<String, BukkitRunnable> tpCooldownTask = new HashMap<>();
	private final JustHomesPlugin plugin;
	private String prefix;

	public TeleportPlayer(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.prefix = plugin.prefix;

	}

	public void teleportPlayer(Player p, Location loc, int duration) {
		String uuid = p.getUniqueId().toString();

		tpCooldown.put(uuid, duration);

		tpCooldownTask.put(uuid, new BukkitRunnable() {
			@Override
			public void run() {
				if (tpCooldown.get(uuid) > 0) {
					tpCooldown.put(uuid, tpCooldown.get(uuid) - 1);
				}
				if (tpCooldown.get(uuid) == 0) {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
							TextComponent.fromLegacyText(Messages.get("Teleporting")));
					p.teleport(loc);
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
							prefix + PlaceholderAPI.setPlaceholders(p, Messages.get("SuccessfulTeleportation"))));
					tpCooldownTask.get(uuid).cancel();
					tpCooldown.remove(uuid);
					tpCooldownTask.remove(uuid);
				} else {
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
							prefix + PlaceholderAPI.setPlaceholders(p, Messages.get("TeleportingCooldown"))));
				}
			}

		});
		tpCooldownTask.get(uuid).runTaskTimer(plugin, 0, 20);
	}
}
