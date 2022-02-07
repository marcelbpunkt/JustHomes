package me.kondi.JustHomes.Commands;


import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.JustHomes;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class ListHomeCommand {

    private JustHomes plugin;
    private String prefix;
    private HashMap<String, String> messages;
    private PlayerData playerData;

    public ListHomeCommand(JustHomes plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
        this.messages = plugin.messages;
        this.playerData = plugin.playerData;
    }

    public void getList(Player p) {
        String uuid = p.getUniqueId().toString();
        if (playerData.countPlayerHomes(uuid) == 0) {
            p.sendMessage(prefix + messages.get("UserHasNoHomes"));
            return;
        }

        List<String> keys = playerData.listOfHomes(uuid);
        int maxHomesAmount = plugin.permissionChecker.checkHomesMaxAmount(p);
        if (keys.size() < maxHomesAmount) maxHomesAmount = keys.size();

        if (keys.size() == 0) {
            p.sendMessage(prefix + messages.get("UserHasNoHomes"));
            return;
        }
        p.sendMessage(messages.get("ListHomesTitle"));

        for (int i = 1; i < maxHomesAmount + 1; i++) {
            p.sendMessage(messages.get("ListColorOfIndexNumber") + i + ". " + messages.get("ListColorOfIndexName") + keys.get(i - 1));
        }


    }

}
