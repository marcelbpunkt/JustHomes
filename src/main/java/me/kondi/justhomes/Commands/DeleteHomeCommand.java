package me.kondi.justhomes.Commands;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.kondi.justhomes.JustHomesPlugin;
import me.kondi.justhomes.Data.PlayerData;
import me.kondi.justhomes.Home.Home;
import me.kondi.justhomes.Home.HomeNames;
import me.kondi.justhomes.Utils.Messages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class DeleteHomeCommand {

	private final String prefix;

	private final PlayerData playerData;

	public DeleteHomeCommand(JustHomesPlugin plugin) {
		this.prefix = plugin.prefix;

		this.playerData = plugin.playerData;
	}

	/**
	 * Deletes a specified home.
	 * 
	 * @param p    the player whose home is being deleted
	 * @param args the arguments of the command
	 */
	public void executeDelhomeCommand(Player p, String[] args) {
		String uuid = p.getUniqueId().toString();

		if (args.length == 0) {
			p.sendMessage(prefix + Messages.get("SpecifyHomeNameException"));
			return;
		}

		// this shouldn't be possible as of v1.20 since there exists at least the world
		// spawn home
		// if (playerData.countPlayerHomes(uuid) == 0) {
		// p.sendMessage(prefix + Messages.get("UserHasNoHomes"));
		// return;
		// }

		String homeName = args[0];
		if (homeName.equalsIgnoreCase(Messages.get("WorldSpawn")) || homeName.equalsIgnoreCase(Messages.get("Bed"))) {
			p.sendMessage(prefix + Messages.get("ManualModifySpawnHomeException"));
			return;
		}

		Home home = playerData.getHome(uuid, homeName);

		if (home == null) {
			p.sendMessage(prefix + Messages.get("UnknownHomeName"));
			return;
		}

		HomeNames.addHomeName(uuid, homeName);
		playerData.deleteHome(home);
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
				TextComponent.fromLegacyText(prefix + PlaceholderAPI.setPlaceholders(p, Messages.get("DeletedHome"))));
	}

}
