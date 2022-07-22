package me.kondi.justhomes.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Data.PlayerData;
import me.kondi.justhomes.Home.Home;
import me.kondi.justhomes.Utils.Messages;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ListHomeCommand {

	private JustHomesPlugin plugin;
	private String prefix;

	private PlayerData playerData;

	public ListHomeCommand(JustHomesPlugin plugin) {
		this.plugin = plugin;
		this.prefix = plugin.prefix;

		this.playerData = plugin.playerData;
	}

	public void executeListHomeCommand(Player p) {
		String uuid = p.getUniqueId().toString();
		if (playerData.countPlayerHomes(uuid) == 0) {
			p.sendMessage(prefix + Messages.get("UserHasNoHomes"));
			return;
		}

		// we need a copy since we'll add the spawn point(s) to it for single use
		List<Home> homes = new ArrayList<>(playerData.listOfHomes(uuid));

		Home worldSpawn = new Home(uuid, Messages.get("WorldSpawn"), p.getWorld().getSpawnLocation());
		homes.add(worldSpawn);

		Location playerSpawnLocation = p.getBedSpawnLocation();
		Home playerSpawn = new Home(uuid, Messages.get("Bed"), playerSpawnLocation);
		if (playerSpawn != null) {
			homes.add(playerSpawn);
		}

		// There is at least the world spawn so this list will never be empty.
		// if (homes.size() == 0) {
		// p.sendMessage(prefix + Messages.get("UserHasNoHomes"));
		// return;
		// }

		p.sendMessage(Messages.get("ListHomesTitle"));

		for (int i = 0; i < homes.size(); i++) {
			Home home = homes.get(i);
			TextComponent indexText = new TextComponent(Messages.get("ListColorOfIndexNumber") + (i + 1) + ". ");
			TextComponent homeText = new TextComponent(Messages.get("ListColorOfIndexName") + home.getHomeName());
			homeText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new Text("World= " + Bukkit.getWorld(home.getWorldName()).getEnvironment().name() + "\n" + "X= "
							+ (int) home.getX() + "\n" + "Y= " + (int) home.getY() + "\n" + "Z= "
							+ (int) home.getZ())));
			indexText.addExtra(homeText);
			p.spigot().sendMessage(indexText);
		}
	}
}
