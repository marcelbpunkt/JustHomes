package me.kondi.JustHomes.Commands;

import me.kondi.JustHomes.Data.PlayerData;
import me.kondi.JustHomes.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class DeleteHomeCommand {

    private Main plugin;
    private String prefix;
    private HashMap<String, String> messages = new HashMap<>();
    private PlayerData playerData;

    public DeleteHomeCommand(Main plugin) {
        this.plugin = plugin;
        this.prefix = plugin.prefix;
        this.messages = plugin.messages;
        this.playerData = plugin.playerData;
    }

    public DeleteHomeCommand() {

    }


    public void delete(Player p, String[] args) {
        String uuid = p.getUniqueId().toString();

        if (playerData.countPlayerHomes(uuid) == 0) {
            p.sendMessage(prefix + ChatColor.RED + messages.get("UserHasNoHomes"));
            return;
        }

        if (args.length == 0) {
            p.sendMessage(prefix + ChatColor.RED + messages.get("SpecifyHomeNameException"));
            return;
        }


        Set<String> keys = playerData.listOfHomes(uuid);
        String homeName = args[0];
        for (String key : keys) {
            if (homeName.equalsIgnoreCase(key)) {
                playerData.deleteHome(uuid, homeName);
                p.sendMessage(prefix + ChatColor.GOLD + args[0] + ChatColor.GRAY + messages.get("DeletedHome"));
                return;
            }
        }
        p.sendMessage(prefix + ChatColor.RED + messages.get("UnknownHomeName"));


    }


}
