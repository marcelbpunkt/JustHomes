package me.kondi.JustHomes.Commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Home.HomeNames;
import me.kondi.JustHomes.JustHomes;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class DeleteHomeCommand {

    private final String prefix;
    private final HashMap<String, String> messages;
    private final PlayerData playerData;

    public DeleteHomeCommand(JustHomes plugin) {
        this.prefix = plugin.prefix;
        this.messages = plugin.messages;
        this.playerData = plugin.playerData;
    }


    public void delete(Player p, String[] args) {
        String uuid = p.getUniqueId().toString();

        if (args.length == 0) {
            p.sendMessage(prefix + messages.get("SpecifyHomeNameException"));
            return;
        }
        if (playerData.countPlayerHomes(uuid) == 0) {
            p.sendMessage(prefix + messages.get("UserHasNoHomes"));
            return;
        }
        String homeName = args[0];
        List<String> homeList = playerData.listOfHomes(uuid);
        if (homeList.contains(homeName)) {
            homeName = homeList.get(homeList.indexOf(homeName));
            HomeNames.addHomeName(uuid, homeName);
            playerData.deleteHome(uuid, homeName);
            p.sendMessage(prefix + PlaceholderAPI.setPlaceholders(p, messages.get("DeletedHome")));
            return;
        }
        p.sendMessage(prefix + messages.get("UnknownHomeName"));
    }
}



